package abbottabad.comsats.campusapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 03-Oct-16.
 */

class ResponseViewAdapter extends RecyclerView.Adapter <ResponseViewAdapter.ViewHolder> {

    private List<ResponseInfo> responseInfoList = new ArrayList<>();
    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_response, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ResponseInfo responseInfo = responseInfoList.get(position);
        holder.TV_requesterName.setText(responseInfo.getName());
        holder.TV_requesterReg.setText(responseInfo.getRegistration());
        holder.TV_requesterBloodType.setText(responseInfo.getBloodType());
        holder.TV_requesterContact.setText(responseInfo.getContact());
        String distance = "Distance from you " + String.valueOf(responseInfo.getDistance()) + " meters";
        holder.TV_Distance.setText(distance);
    }

    @Override
    public int getItemCount() {
        return responseInfoList.size();
    }

    public void add(ResponseInfo responseInfo, int position){
        responseInfoList.add(position,responseInfo);
        notifyItemInserted(position);
    }

    private void remove(int position) {
        responseInfoList.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TV_requesterName;
        private TextView TV_requesterReg;
        private TextView TV_requesterBloodType;
        private TextView TV_requesterContact;
        private TextView TV_Distance;
        private TextView btnAccept;
        private TextView btnCancel;
        private TextView btnDelete;
        private Button btnCall;

        ViewHolder(final View itemView) {
            super(itemView);
            TV_requesterName = (TextView) itemView.findViewById(R.id.TV_requesterName);
            TV_requesterReg = (TextView) itemView.findViewById(R.id.TV_requesterReg);
            TV_requesterBloodType = (TextView) itemView.findViewById(R.id.TV_requesterBloodType);
            TV_requesterContact = (TextView) itemView.findViewById(R.id.TV_requesterContact);
            TV_Distance = (TextView) itemView.findViewById(R.id.TV_Distance);
            btnAccept = (TextView) itemView.findViewById(R.id.btnAccept);
            btnCancel = (TextView) itemView.findViewById(R.id.btnCancel);
            btnDelete = (TextView) itemView.findViewById(R.id.btnDelete);
            btnCall = (Button) itemView.findViewById(R.id.btnCall);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BloodBankResponseModal(itemView.getContext()).deleteResponse(TV_requesterReg.getText().toString());
                    remove(getAdapterPosition());
                }
            });
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BloodBankModal(itemView.getContext()).acceptResponse(TV_requesterReg.getText().toString());
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BloodBankModal(itemView.getContext()).rejectResponse(TV_requesterReg.getText().toString());
                }
            });

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:" + TV_requesterContact.getText().toString().trim()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
