package abbottabad.comsats.campusapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
public class ComplaintPollVIewAdapter extends RecyclerView.Adapter<ComplaintPollVIewAdapter.ViewHolder>{


    private List<ComplaintsInfo> complaintsInfoList;

    public ComplaintPollVIewAdapter(List<ComplaintsInfo> complaintsInfoList) {
        this.complaintsInfoList = complaintsInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_complaint, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ComplaintsInfo complaintsInfo = complaintsInfoList.get(position);
        holder.TV_complaintName.setText(complaintsInfo.getName());
        holder.TV_complaintRegistrtion.setText(complaintsInfo.getRegistration());
        holder.TV_complaintContact.setText(complaintsInfo.getContact());
        String description = complaintsInfo.getDescription();
        if (description != null && !description.isEmpty()){
            holder.TV_complaintDescription.setText(description);
        }
    }

    @Override
    public int getItemCount() {
        return complaintsInfoList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TV_complaintName;
        private TextView TV_complaintRegistrtion;
        private TextView TV_complaintContact;
        private TextView TV_complaintDescription;
        private ImageView IV_proof;
        private Button btn_callComplaint;
        public ViewHolder(View itemView) {
            super(itemView);
            TV_complaintName = (TextView) itemView.findViewById(R.id.TV_ComplaintName);
            TV_complaintRegistrtion = (TextView) itemView.findViewById(R.id.TV_ComplaintRegistration);
            TV_complaintContact = (TextView) itemView.findViewById(R.id.TV_ComplaintContact);
            TV_complaintDescription = (TextView) itemView.findViewById(R.id.TV_ComplaintDescription);
            IV_proof = (ImageView) itemView.findViewById(R.id.IV_proof);
            btn_callComplaint = (Button) itemView.findViewById(R.id.btn_callComplaint);
        }
    }
}
