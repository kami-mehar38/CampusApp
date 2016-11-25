package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().displayer(new CircleBitmapDisplayer())
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        ImageLoader.getInstance().displayImage(notificationsListInfo.getGroupImageUri(), holder.IV_groupPicture);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(notificationsListInfo.getGroupName() + "_EXISTS", true);
        editor.apply();

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
                } else holder.TV_timeStamp.setText(timeStamp[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.TV_userName.setText(notificationsListInfo.getUserName());
        holder.TV_userName.setVisibility(View.GONE);
        holder.TV_regId.setText(notificationsListInfo.getRegId());
        holder.TV_regId.setVisibility(View.GONE);
        holder.TV_groupTimeStamp.setText(notificationsListInfo.getTimeStamp());
        holder.TV_groupTimeStamp.setVisibility(View.GONE);
    }

    void addItem(NotificationsListInfo notificationsListInfo, int position) {
        notificationsListInfoList.add(position, notificationsListInfo);
        notifyItemInserted(position);
    }

    void removeItem(int position) {
        notificationsListInfoList.remove(position);
        notifyDataSetChanged();
    }

    class NotificationsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView IV_groupPicture;
        private TextView TV_groupName;
        private TextView TV_recentMessage;
        private TextView TV_counterBadge;
        private TextView TV_timeStamp;
        private TextView TV_userName;
        private TextView TV_regId;
        private TextView TV_groupTimeStamp;
        private RippleView CV_group;
        private SharedPreferences.Editor editor;
        private AlertDialog alertDialog;


        NotificationsListViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            IV_groupPicture = (ImageView) itemView.findViewById(R.id.IV_profilePicture);
            IV_groupPicture.setOnClickListener(this);
            TV_groupName = (TextView) itemView.findViewById(R.id.TV_groupName);
            TV_recentMessage = (TextView) itemView.findViewById(R.id.TV_recentMessage);
            TV_counterBadge = (TextView) itemView.findViewById(R.id.TV_counterBadge);
            TV_timeStamp = (TextView) itemView.findViewById(R.id.TV_timeStamp);
            TV_userName = (TextView) itemView.findViewById(R.id.TV_userName);
            TV_regId = (TextView) itemView.findViewById(R.id.TV_regId);
            TV_groupTimeStamp = (TextView) itemView.findViewById(R.id.TV_groupTimeStamp);
            CV_group = (RippleView) itemView.findViewById(R.id.CV_group);
            CV_group.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    if (!NotificationsHomePage.isLongClick) {
                        editor.putString("NOTIFICATION_TYPE", TV_groupName.getText().toString());
                        editor.apply();
                        context.startActivity(new Intent(context, NotificationsView.class));
                    }

                }
            });
            //CV_group.setOnClickListener(this);
            CV_group.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NotificationsHomePage.isLongClick = true;
                    String userName = TV_userName.getText().toString().trim();
                    String regId = TV_regId.getText().toString().trim();
                    String timeStamp = TV_groupTimeStamp.getText().toString().trim();
                    notificationsHomePage.setActionMode(TV_groupName.getText().toString(), v, userName, regId, timeStamp);
                    NotificationsUtills.setPosition(getAdapterPosition());
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
                case R.id.IV_profilePicture: {
                    View view = LayoutInflater.from(context).inflate(R.layout.notifications_group_picture_view, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.IV_groupPicture);
                    imageView.setOnClickListener(this);
                    TextView TV_sendMessage = (TextView) view.findViewById(R.id.TV_sendMessage);
                    TV_sendMessage.append(" " + TV_groupName.getText().toString());
                    TV_sendMessage.setOnClickListener(this);
                    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                            .cacheInMemory(true).cacheOnDisk(true).build();

                    ImageLoader.getInstance().displayImage("http://hostellocator.com/images/" + TV_groupName.getText().toString() + ".JPG", imageView, defaultOptions);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(view);
                    builder.setCancelable(true);
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                }
                case R.id.IV_groupPicture: {
                    alertDialog.cancel();
                    NotificationsUtills.setGroupName(TV_groupName.getText().toString());
                    NotificationsUtills.setImageUri("http://hostellocator.com/images/" + TV_groupName.getText().toString() + ".JPG");
                    context.startActivity(new Intent(context, NotificationsGroupImage.class));
                    break;
                }
                case R.id.TV_sendMessage: {
                    alertDialog.cancel();
                    editor.putString("NOTIFICATION_TYPE", TV_groupName.getText().toString());
                    editor.apply();
                    context.startActivity(new Intent(context, NotificationsView.class));
                    break;
                }
            }
        }
    }
}
