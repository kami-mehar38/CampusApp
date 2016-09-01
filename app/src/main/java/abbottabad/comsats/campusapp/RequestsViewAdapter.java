package abbottabad.comsats.campusapp;

import android.content.Intent;
import android.net.Uri;
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
 * This project CampusApp is created by Kamran Ramzan on 9/1/16.
 */
public class RequestsViewAdapter extends RecyclerView.Adapter <RequestsViewAdapter.ViewHolder> {

    private List<RequestsInfo> requestsInfoList = new ArrayList<>();

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

    public void remove(int position) {
        requestsInfoList.remove(position);
        notifyItemRemoved(position);
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
                    remove(getAdapterPosition());
                }
            });
        }
    }
}
