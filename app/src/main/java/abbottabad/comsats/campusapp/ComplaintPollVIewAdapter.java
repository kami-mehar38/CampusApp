package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
class ComplaintPollVIewAdapter extends RecyclerView.Adapter<ComplaintPollVIewAdapter.ViewHolder>{


    private List<ComplaintsInfo> complaintsInfoList = new ArrayList<>();
    private Context context;

    ComplaintPollVIewAdapter(Context context) {
        this.context = context;
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
        holder.TV_imageUri.setText(complaintsInfo.getImageUrl());
        holder.btn_callComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + holder.TV_complaintContact.getText().toString().trim()));
                v.getContext().startActivity(intent);
            }
        });
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoader.getInstance().displayImage("http://hostellocator.com/images/" + complaintsInfo.getImageUrl() + ".JPG",
                holder.IV_evidencePicture, defaultOptions);
    }

    @Override
    public int getItemCount() {
        return complaintsInfoList.size();
    }

     void add(ComplaintsInfo complaintsInfo, int position){
        complaintsInfoList.add(position,complaintsInfo);
        notifyItemInserted(position);
        if (ComplaintPollView.TV_noComplaints != null){
            if (getItemCount() > 0){
                ComplaintPollView.TV_noComplaints.setVisibility(View.GONE);
            }
        }
    }

    void remove(int position) {
        complaintsInfoList.remove(position);
        notifyItemRemoved(position);
        if (ComplaintPollView.TV_noComplaints != null){
            if (getItemCount() == 0){
                ComplaintPollView.TV_noComplaints.setVisibility(View.VISIBLE);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TV_complaintName;
        private TextView TV_complaintRegistrtion;
        private TextView TV_complaintContact;
        private TextView TV_complaintDescription;
        private TextView TV_imageUri;
        private ImageView IV_evidencePicture;
        private Button btn_callComplaint;
        private Button btn_deleteComplaint;
        ViewHolder(final View itemView) {
            super(itemView);
            //this.setIsRecyclable(false);
            TV_complaintName = (TextView) itemView.findViewById(R.id.TV_ComplaintName);
            TV_complaintRegistrtion = (TextView) itemView.findViewById(R.id.TV_ComplaintRegistration);
            TV_complaintContact = (TextView) itemView.findViewById(R.id.TV_ComplaintContact);
            TV_complaintDescription = (TextView) itemView.findViewById(R.id.TV_ComplaintDescription);
            TV_imageUri = (TextView) itemView.findViewById(R.id.TV_imageUri);
            btn_callComplaint = (Button) itemView.findViewById(R.id.btn_callComplaint);
            IV_evidencePicture = (ImageView) itemView.findViewById(R.id.IV_evidencePicture);
            IV_evidencePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationsUtills.setGroupName(TV_complaintName.getText().toString());
                    NotificationsUtills.setImageUri("http://hostellocator.com/images/" + TV_imageUri.getText().toString() + ".JPG");
                    context.startActivity(new Intent(view.getContext(), NotificationsGroupImage.class));
                }
            });
            btn_deleteComplaint = (Button) itemView.findViewById(R.id.btn_deleteComplaint);
            btn_deleteComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationsUtills.setPosition(getAdapterPosition());
                    new ComplaintPollModal(context).deleteComplaint(TV_imageUri.getText().toString());
                }
            });
        }
    }
}
