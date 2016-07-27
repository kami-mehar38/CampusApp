package abbottabad.comsats.campusapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodRequestsFragment extends Fragment {


    private BloodBankLocalModal bloodBankLocalModal;
    private  static RecyclerView RV_bloodRequests;

    public BloodRequestsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloodBankLocalModal = new BloodBankLocalModal(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_blood_requests, container, false);
    }

    // This event is triggered soon after onCreateView().

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);

        RV_bloodRequests = (RecyclerView) view.findViewById(R.id.RV_bloodRequests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_bloodRequests.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        RV_bloodRequests.addItemDecoration(itemDecoration);
        bloodBankLocalModal.viewBloodRequests();
    }

    public static class BloodBankLocalModal extends SQLiteOpenHelper {

        private static int VERSION = 1;
        private static String DATABASE_NAME = "BLOODBANK.db";
        private static String SERIAL = "SERIAL_NO";
        private static String TABLE_NAME = "BLOOD_REQUESTS";
        private static String COL_NAME = "NAME";
        private static String COL_REG = "REG_NO";
        private static String COL_CONTACT = "CONTACT";
        private static String COL_BLOOD_TYPE = "BLLOD_TYPE";

        public BloodBankLocalModal(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " +TABLE_NAME+ " ( "
                    +SERIAL+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +COL_NAME+ " TEXT,"
                    +COL_REG+ " TEXT,"
                    +COL_CONTACT+ " TEXT,"
                    +COL_BLOOD_TYPE+ " TEXT"
                    + " )";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String dropTableQuery = "DROP TABLE IF EXISTS " +TABLE_NAME;
            db.execSQL(dropTableQuery);
            onCreate(db);
        }
        public void addBloodRequest(){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_NAME, BloodBankController.getStdName());
            contentValues.put(COL_REG, BloodBankController.getStdReg());
            contentValues.put(COL_CONTACT, BloodBankController.getStdContact());
            contentValues.put(COL_BLOOD_TYPE, BloodBankController.getBloodType());
            db.insert(TABLE_NAME, null, contentValues);
            db.close();
        }
        public void viewBloodRequests(){
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery= "SELECT * FROM " +TABLE_NAME+ " ORDER BY " +SERIAL+ " DESC";
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            final List<RequestsInfo> requestsInfoList = new ArrayList<>();
            RequestsInfo[] requestsInfos = new RequestsInfo[cursor.getCount()];
            while (!cursor.isAfterLast()){
                requestsInfos[cursor.getPosition()] = new RequestsInfo();
                requestsInfos[cursor.getPosition()].setName(cursor.getString(1));
                requestsInfos[cursor.getPosition()].setRegistration(cursor.getString(2));
                requestsInfos[cursor.getPosition()].setBloodType(cursor.getString(4));
                requestsInfos[cursor.getPosition()].setContact(cursor.getString(3));
                requestsInfoList.add(requestsInfos[cursor.getPosition()]);
                cursor.moveToNext();
            }
            cursor.close();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (RV_bloodRequests != null) {
                        RV_bloodRequests.setAdapter(new RequestsViewAdapter(requestsInfoList));
                    }
                }
            });
            db.close();
        }

        public void deleteRequest(String reg){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, COL_REG + " = ?", new String[]{reg});
            viewBloodRequests();
        }

    }
    /**
     * Created by Kamran Ramzan on 7/6/16.
     */
    public static class RequestsViewAdapter extends RecyclerView.Adapter <RequestsViewAdapter.ViewHolder> {

        private List<RequestsInfo> requestsInfoList;

        public RequestsViewAdapter(List<RequestsInfo> requestsInfoList){
            this.requestsInfoList = requestsInfoList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_requests, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            RequestsInfo requestsInfo = requestsInfoList.get(position);
            holder.TV_requesterName.setText(requestsInfo.getName());
            holder.TV_requesterReg.setText(requestsInfo.getRegistration());
            holder.TV_requesterBloodType.setText(requestsInfo.getBloodType());
            holder.TV_requesterContact.setText(requestsInfo.getContact());
        }


        @Override
        public int getItemCount() {
            return requestsInfoList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            private TextView TV_requesterName;
            private TextView TV_requesterReg;
            private TextView TV_requesterBloodType;
            private TextView TV_requesterContact;
            private Button btnCall;
            private Button btnMessage;
            private Button btnDelete;

            public ViewHolder(final View itemView) {
                super(itemView);
                TV_requesterName = (TextView) itemView.findViewById(R.id.TV_requesterName);
                TV_requesterReg = (TextView) itemView.findViewById(R.id.TV_requesterReg);
                TV_requesterBloodType = (TextView) itemView.findViewById(R.id.TV_requesterBloodType);
                TV_requesterContact = (TextView) itemView.findViewById(R.id.TV_requesterContact);
                btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
                btnCall = (Button) itemView.findViewById(R.id.btnCall);
                btnMessage = (Button) itemView.findViewById(R.id.btnMessage);

                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL,
                                Uri.parse("tel:" + TV_requesterContact.getText().toString().trim()));
                        itemView.getContext().startActivity(intent);
                    }
                });

                btnMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("sms:" + TV_requesterContact.getText().toString().trim()));
                        itemView.getContext().startActivity(intent);
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new BloodBankLocalModal(itemView.getContext()).deleteRequest(TV_requesterReg.getText().toString());
                    }
                });
            }
        }
    }
}