package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
class ComplaintPollVIewAdapter extends RecyclerView.Adapter<ComplaintPollVIewAdapter.ViewHolder>{


    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
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
        if (complaintsInfo.getComplaintType().equals("2")){
            holder.btn_processComplaint.setVisibility(View.GONE);
        }
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoader.getInstance().displayImage("http://hostellocator.com/images/" + complaintsInfo.getImageUrl() + ".JPG",
                holder.IV_evidencePicture, defaultOptions);

        String timeDateStamp = complaintsInfo.getTimeStamp();
        if (timeDateStamp != null) {
            String[] timeStamp = timeDateStamp.split("/");
            try {
                DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                String currentDateString = df.format(Calendar.getInstance().getTime());
                Date currentDate = df.parse(currentDateString);
                Date messageDate = df.parse(timeStamp[0]);
                if (messageDate.equals(currentDate)) {
                    holder.TV_timeStamp.setText(timeStamp[1]);
                } else {
                    Calendar messageCalendar = Calendar.getInstance();
                    messageCalendar.setTime(messageDate);
                    Calendar currentCalendar = Calendar.getInstance();
                    currentCalendar.setTime(currentDate);
                    if (messageCalendar.get(Calendar.DATE) != currentCalendar.get(Calendar.DATE)
                            && messageCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
                            && messageCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
                        String[] thisDate = timeStamp[0].split(" ");
                        holder.TV_timeStamp.setText("0" + thisDate[0] + " At " + timeStamp[1]);
                    } else if (messageCalendar.get(Calendar.DATE) != currentCalendar.get(Calendar.DATE)
                            && messageCalendar.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)
                            && messageCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
                        String[] thisDate = timeStamp[0].split(" ");
                        holder.TV_timeStamp.setText(thisDate[0] + "-" + thisDate[1] + " At " + timeStamp[1]);
                    } else if (messageCalendar.get(Calendar.DATE) != currentCalendar.get(Calendar.DATE)
                            && messageCalendar.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)
                            && messageCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)) {
                        holder.TV_timeStamp.setText(timeStamp[0] + " At " + timeStamp[1]);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
        private TextView TV_timeStamp;
        private ImageView IV_evidencePicture;
        private ImageView btn_callComplaint;
        private ImageView btn_deleteComplaint;
        private ImageView btn_processComplaint;
        private AlertDialog alertDialog;
        private SharedPreferences sharedPreferences;
        private AlertDialog alertDialogProcess;

        ViewHolder(final View itemView) {
            super(itemView);
            //this.setIsRecyclable(false);
            sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            TV_complaintName = (TextView) itemView.findViewById(R.id.TV_ComplaintName);
            TV_complaintRegistrtion = (TextView) itemView.findViewById(R.id.TV_ComplaintRegistration);
            TV_complaintContact = (TextView) itemView.findViewById(R.id.TV_ComplaintContact);
            TV_complaintDescription = (TextView) itemView.findViewById(R.id.TV_ComplaintDescription);
            TV_imageUri = (TextView) itemView.findViewById(R.id.TV_imageUri);
            TV_timeStamp = (TextView) itemView.findViewById(R.id.TV_timeStamp);
            btn_callComplaint = (ImageView) itemView.findViewById(R.id.btn_callComplaint);
            IV_evidencePicture = (ImageView) itemView.findViewById(R.id.IV_evidencePicture);
            IV_evidencePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationsUtills.setGroupName(TV_complaintName.getText().toString());
                    NotificationsUtills.setImageUri("http://hostellocator.com/images/" + TV_imageUri.getText().toString() + ".JPG");
                    context.startActivity(new Intent(view.getContext(), NotificationsGroupImage.class));
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Delete complaint?");
            builder.setCancelable(true);
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    NotificationsUtills.setPosition(getAdapterPosition());
                    new ComplaintPollModal(context).deleteComplaint(TV_imageUri.getText().toString());
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.cancel();
                }
            });
            alertDialog = builder.create();
            btn_deleteComplaint = (ImageView) itemView.findViewById(R.id.btn_deleteComplaint);
            btn_deleteComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.show();
                }
            });

            AlertDialog.Builder builderProcess = new AlertDialog.Builder(context);
            builderProcess.setMessage("This will add complaint to processed complaints list?");
            builderProcess.setCancelable(true);
            builderProcess.setPositiveButton("PROCESS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new ComplaintPollLocalModal(context).processComplaint(TV_imageUri.getText().toString(),
                            String.valueOf(sharedPreferences.getInt("COMPLAINT_TYPE", 0)));
                    remove(getAdapterPosition());
                }
            });
            builderProcess.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialogProcess.cancel();
                }
            });
            alertDialogProcess = builderProcess.create();
            btn_processComplaint = (ImageView) itemView.findViewById(R.id.btn_processComplaint);
            btn_processComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialogProcess.show();
                }
            });
        }
    }
}
