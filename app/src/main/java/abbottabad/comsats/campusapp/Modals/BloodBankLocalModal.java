package abbottabad.comsats.campusapp.Modals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import abbottabad.comsats.campusapp.Controllers.BloodBankController;
import abbottabad.comsats.campusapp.Controllers.RequestsViewAdapter;
import abbottabad.comsats.campusapp.Helper_Classes.RequestsInfo;

/**
 * Created by Kamran Ramzan on 7/10/16.
 */
public class BloodBankLocalModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "BLOODBANK.db";
    private static String SERIAL = "SERIAL_NO";
    private static String TABLE_NAME = "BLOOD_REQUESTS";
    private static String COL_NAME = "NAME";
    private static String COL_REG = "REG_NO";
    private static String COL_CONTACT = "CONTACT";
    private static String COL_BLOOD_TYPE = "BLLOD_TYPE";
    private Context context;

    public BloodBankLocalModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
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
    public void viewBloodRequests(RecyclerView RV_bloodRequests){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME+ " ORDER BY " +SERIAL+ " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        List<RequestsInfo> requestsInfoList = new ArrayList<>();
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
        RV_bloodRequests.setAdapter(new RequestsViewAdapter(requestsInfoList));
    }

}
