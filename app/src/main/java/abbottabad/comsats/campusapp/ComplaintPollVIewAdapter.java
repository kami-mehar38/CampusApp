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
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
class ComplaintPollVIewAdapter extends RecyclerView.Adapter<ComplaintPollVIewAdapter.ViewHolder>{


    private List<ComplaintsInfo> complaintsInfoList = new ArrayList<>();

    ComplaintPollVIewAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_complaint, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ComplaintsInfo complaintsInfo = complaintsInfoList.get(position);
        holder.TV_complaintName.setText(complaintsInfo.getName());
        holder.TV_complaintRegistrtion.setText(complaintsInfo.getRegistration());
        holder.TV_complaintContact.setText(complaintsInfo.getContact());
        String description = complaintsInfo.getDescription();
        if (description != null && !description.isEmpty()){
            holder.TV_complaintDescription.setText(description);
        }

        holder.btn_callComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + holder.TV_complaintContact.getText().toString().trim()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complaintsInfoList.size();
    }

    public void add(ComplaintsInfo complaintsInfo, int position){
        complaintsInfoList.add(position,complaintsInfo);
        notifyItemInserted(position);
    }

    private void remove(int position) {
        complaintsInfoList.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TV_complaintName;
        private TextView TV_complaintRegistrtion;
        private TextView TV_complaintContact;
        private TextView TV_complaintDescription;
        private Button btn_callComplaint;
        private Button btn_deleteComplaint;
        ViewHolder(View itemView) {
            super(itemView);
            TV_complaintName = (TextView) itemView.findViewById(R.id.TV_ComplaintName);
            TV_complaintRegistrtion = (TextView) itemView.findViewById(R.id.TV_ComplaintRegistration);
            TV_complaintContact = (TextView) itemView.findViewById(R.id.TV_ComplaintContact);
            TV_complaintDescription = (TextView) itemView.findViewById(R.id.TV_ComplaintDescription);
            btn_callComplaint = (Button) itemView.findViewById(R.id.btn_callComplaint);
            btn_deleteComplaint = (Button) itemView.findViewById(R.id.btn_deleteComplaint);
            btn_deleteComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ComplaintPollLocalModal(v.getContext()).
                            deleteComplaint(TV_complaintRegistrtion.getText().toString().trim());
                    remove(getAdapterPosition());
                }
            });
        }
    }
}
