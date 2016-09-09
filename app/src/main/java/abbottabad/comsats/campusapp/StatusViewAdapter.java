package abbottabad.comsats.campusapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/16/16.
 */
public class StatusViewAdapter extends RecyclerView.Adapter<StatusViewAdapter.ViewHolder> {
    private List<StatusInfo> statusInfoList;

    public StatusViewAdapter(List<StatusInfo> statusInfoList) {
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
        switch (statusInfo.getStatus()){
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
    }

    @Override
    public int getItemCount() {
        return statusInfoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TV_teacherName;
        private TextView TV_teacherStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            TV_teacherName = (TextView) itemView.findViewById(R.id.TV_teacherName);
            TV_teacherStatus = (TextView) itemView.findViewById(R.id.TV_teacherStatus);
        }
    }
}
