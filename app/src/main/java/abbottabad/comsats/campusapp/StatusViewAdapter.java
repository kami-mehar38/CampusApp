package abbottabad.comsats.campusapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/16/16.
 */
class StatusViewAdapter extends RecyclerView.Adapter<StatusViewAdapter.ViewHolder> {
    private List<StatusInfo> statusInfoList;

    StatusViewAdapter(List<StatusInfo> statusInfoList) {
        this.statusInfoList = statusInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_status, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatusInfo statusInfo = statusInfoList.get(position);
        holder.TV_teacherName.setText(statusInfo.getTeacherName());
        switch (statusInfo.getStatus()) {
            case "Available": {
                holder.TV_teacherStatus.setTextColor(Color.parseColor("#4d9c2d"));
                break;
            }
            case "Busy": {
                holder.TV_teacherStatus.setTextColor(Color.DKGRAY);
                break;
            }
            case "Absent": {
                holder.TV_teacherStatus.setTextColor(Color.parseColor("#FFA500"));
                break;
            }
            case "On Leave": {
                holder.TV_teacherStatus.setTextColor(Color.RED);
                break;
            }
        }
        holder.TV_teacherStatus.setText(statusInfo.getStatus());
        holder.TV_teacherRegistration.setText(statusInfo.getTeacherRegistration());
        holder.TV_teacherRegistration.setVisibility(View.GONE);
        if (statusInfo.getMode().equals("Private")) {
            holder.btnTimetable.setVisibility(View.GONE);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Timetable is not available", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return statusInfoList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TV_teacherName;
        private TextView TV_teacherRegistration;
        private TextView TV_teacherStatus;
        private RippleView btnTimetable;
        private RelativeLayout cardView;

        ViewHolder(final View itemView) {
            super(itemView);
            TV_teacherName = (TextView) itemView.findViewById(R.id.TV_teacherName);
            TV_teacherRegistration = (TextView) itemView.findViewById(R.id.TV_teacherRegistration);
            TV_teacherStatus = (TextView) itemView.findViewById(R.id.TV_teacherStatus);
            btnTimetable = (RippleView) itemView.findViewById(R.id.btn_timetable);
            cardView = (RelativeLayout) itemView.findViewById(R.id.cardView);

            btnTimetable.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    TrackFacultyUtills.setRegistration(TV_teacherRegistration.getText().toString());
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), TimetableImage.class));
                }
            });
        }
    }
}
