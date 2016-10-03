package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 9/1/16.
 */
class RequestsViewAdapter extends RecyclerView.Adapter <RequestsViewAdapter.ViewHolder> {

    private List<RequestsInfo> requestsInfoList = new ArrayList<>();
    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
   /* public RequestsViewAdapter(List<RequestsInfo> requestsInfoList){
        this.requestsInfoList = requestsInfoList;
    }*/

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

    public void add(RequestsInfo requestsInfo, int position){
        requestsInfoList.add(position,requestsInfo);
        notifyItemInserted(position);
    }

    private void remove(int position) {
        requestsInfoList.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TV_requesterName;
        private TextView TV_requesterReg;
        private TextView TV_requesterBloodType;
        private TextView TV_requesterContact;
        private TextView btnReply;
        private TextView btnDelete;

        ViewHolder(final View itemView) {
            super(itemView);
            TV_requesterName = (TextView) itemView.findViewById(R.id.TV_requesterName);
            TV_requesterReg = (TextView) itemView.findViewById(R.id.TV_requesterReg);
            TV_requesterBloodType = (TextView) itemView.findViewById(R.id.TV_requesterBloodType);
            TV_requesterContact = (TextView) itemView.findViewById(R.id.TV_requesterContact);
            btnReply = (TextView) itemView.findViewById(R.id.btnReply);
            btnDelete = (TextView) itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BloodBankLocalModal(itemView.getContext()).deleteRequest(TV_requesterReg.getText().toString());
                    remove(getAdapterPosition());
                }
            });
            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                    String name = sharedPreferences.getString("NAME", "");
                    String reg_id = sharedPreferences.getString("REG_ID", "");
                    String bloodGroup = sharedPreferences.getString("BLOOD_GROUP", "");
                    String contact = sharedPreferences.getString("CONTACT", "");
                    String latitude = String.valueOf(LocationController.getLatitide());
                    String longitude = String.valueOf(LocationController.getLongitude());
                    new BloodBankModal(itemView.getContext()).replyTorequest(name, reg_id, bloodGroup, contact,
                            TV_requesterReg.getText().toString(), latitude, longitude);
                    Log.i("TAG", "onClick: " + latitude+ longitude);
                }
            });
        }
    }
}
