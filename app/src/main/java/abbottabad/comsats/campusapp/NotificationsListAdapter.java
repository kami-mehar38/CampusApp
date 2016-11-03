package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 25-Oct-16.
 */

class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationsListViewHolder> {

    private List<NotificationsListInfo> notificationsListInfoList = new ArrayList<>();
    private Context context;
    private SharedPreferences sharedPreferences;

    NotificationsListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public NotificationsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_notifications_chat, null);
        return new NotificationsListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return notificationsListInfoList.size();
    }

    @Override
    public void onBindViewHolder(NotificationsListViewHolder holder, int position) {
        NotificationsListInfo notificationsListInfo = notificationsListInfoList.get(position);
        // Then later, when you want to display image
        ImageLoader.getInstance().displayImage(notificationsListInfo.getGroupImageUri(), holder.IV_groupPicture); // Default options will be used

        holder.TV_groupName.setText(notificationsListInfo.getGroupName());
        String recentMessage = sharedPreferences.getString(notificationsListInfo.getGroupName() + "_RECENT_MESSAGE", "No recent message");
        holder.TV_recentMessage.setText(recentMessage);
        int notificationCount = sharedPreferences.getInt(notificationsListInfo.getGroupName() + "_COUNT", 0);
        Log.i("TAG", "onBindViewHolder: " + notificationCount);
        if (notificationCount > 0) {
            holder.TV_counterBadge.setVisibility(View.VISIBLE);
            holder.TV_counterBadge.setText(String.valueOf(notificationCount));

        } else holder.TV_counterBadge.setVisibility(View.INVISIBLE);

        String recentMessageTime = sharedPreferences.getString(notificationsListInfo.getGroupName() + "_RECENT_MESSAGE_TIME", "");
        holder.TV_timeStamp.setText(recentMessageTime);
    }

    void addItem(NotificationsListInfo notificationsListInfo, int position) {
        notificationsListInfoList.add(position, notificationsListInfo);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        notificationsListInfoList.remove(position);
        notifyItemRemoved(position);
    }

    class NotificationsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView IV_groupPicture;
        private TextView TV_groupName;
        private TextView TV_recentMessage;
        private TextView TV_counterBadge;
        private TextView TV_timeStamp;
        private CardView CV_group;
        private SharedPreferences.Editor editor;

        NotificationsListViewHolder(View itemView) {
            super(itemView);
            IV_groupPicture = (ImageView) itemView.findViewById(R.id.IV_profilePicture);
            TV_groupName = (TextView) itemView.findViewById(R.id.TV_groupName);
            TV_recentMessage = (TextView) itemView.findViewById(R.id.TV_recentMessage);
            TV_counterBadge = (TextView) itemView.findViewById(R.id.TV_counterBadge);
            TV_timeStamp = (TextView) itemView.findViewById(R.id.TV_timeStamp);
            CV_group = (CardView) itemView.findViewById(R.id.CV_group);
            CV_group.setOnClickListener(this);
            String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
            sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.CV_group: {
                    editor.putString("NOTIFICATION_TYPE", TV_groupName.getText().toString());
                    editor.apply();
                    context.startActivity(new Intent(context, NotificationsView.class));
                    break;
                }
            }
        }
    }
}
