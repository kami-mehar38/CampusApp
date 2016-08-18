package abbottabad.comsats.campusapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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
        holder.TV_teacherStatus.setText(statusInfo.getStatus());
        setFadeAnimation(holder.CV_facultyStatus);
        //setScaleAnimation(holder.CV_facultyStatus);
    }

    @Override
    public int getItemCount() {
        return statusInfoList.size();
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TV_teacherName;
        private TextView TV_teacherStatus;
        private CardView CV_facultyStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            TV_teacherName = (TextView) itemView.findViewById(R.id.TV_teacherName);
            TV_teacherStatus = (TextView) itemView.findViewById(R.id.TV_teacherStatus);
            CV_facultyStatus = (CardView) itemView.findViewById(R.id.CV_facultyStatus);
        }
    }
}
