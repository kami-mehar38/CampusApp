package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * This project CampusApp is created by Kamran Ramzan on 25-Oct-16.
 */

class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationsListViewHolder> {

    private List<NotificationsListInfo> notificationsListInfoList = new ArrayList<>();
    private Context context;
    private SharedPreferences sharedPreferences;
    private NotificationsHomePage notificationsHomePage;

    NotificationsListAdapter(Context context) {
        this.context = context;
        this.notificationsHomePage = (NotificationsHomePage) context;
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

        boolean isMuted = sharedPreferences.getBoolean(notificationsListInfo.getGroupName() + "_MUTED", false);
        if (isMuted){
            Drawable drawableLeft = context.getResources().getDrawable(R.drawable.ic_muted_chat);
            holder.TV_groupName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            holder.TV_groupName.setCompoundDrawablePadding(15);
        } else holder.TV_groupName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        holder.TV_groupName.setText(notificationsListInfo.getGroupName());
        String recentMessage = sharedPreferences.getString(notificationsListInfo.getGroupName() + "_RECENT_MESSAGE", "No recent message");
        holder.TV_recentMessage.setText(recentMessage);
        int notificationCount = sharedPreferences.getInt(notificationsListInfo.getGroupName() + "_COUNT", 0);
        Log.i("TAG", "onBindViewHolder: " + notificationCount);
        if (notificationCount > 0) {
            holder.TV_counterBadge.setVisibility(View.VISIBLE);
            holder.TV_counterBadge.setText(String.valueOf(notificationCount));

        } else holder.TV_counterBadge.setVisibility(View.INVISIBLE);

        String recentMessageTime = sharedPreferences.getString(notificationsListInfo.getGroupName() + "_RECENT_MESSAGE_TIME", null);
        if (recentMessageTime != null) {
            String[] timeStamp = recentMessageTime.split("/");
            try {
                DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                String currentDateString = df.format(Calendar.getInstance().getTime());
                Date currentDate = df.parse(currentDateString);
                Date messageDate = df.parse(timeStamp[0]);
                if (messageDate.equals(currentDate)) {
                    holder.TV_timeStamp.setText(timeStamp[1]);
                } else holder.TV_timeStamp.setText(timeStamp[0] +" At "+ timeStamp[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else holder.TV_timeStamp.setVisibility(View.INVISIBLE);
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
            CV_group.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    notificationsHomePage.setActionMode(TV_groupName.getText().toString());
                    return true;
                }
            });
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
