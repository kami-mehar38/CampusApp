package abbottabad.comsats.campusapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
class NotificationsAdapter extends ArrayAdapter<NotificationInfo> {

    private List<NotificationInfo> chatMessageList = new ArrayList<>();
    private NotificationsView notificationsView;

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void remove(NotificationInfo object) {
        chatMessageList.remove(object);
        super.remove(object);
    }

    @Override
    public void add(NotificationInfo object) {
        chatMessageList.add(object);
        super.add(object);
    }

    NotificationsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.notificationsView = (NotificationsView) context;
    }

    @Override
    public void clear() {
        chatMessageList.clear();
        super.clear();
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public NotificationInfo getItem(int index) {
        return this.chatMessageList.get(index);
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final NotificationInfo notificationInfo = getItem(position);
        View row;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if ((notificationInfo != null ? notificationInfo.getMine() : 0) == 1) {
            row = inflater.inflate(R.layout.chat_right, parent, false);
        }else{
            row = inflater.inflate(R.layout.chat_left, parent, false);
            TextView TV_notificationsSender = (TextView) row.findViewById(R.id.TV_notificationSender);
            TV_notificationsSender.setText(notificationInfo != null ? notificationInfo.getNotificationSender() : null);
        }

        TextView TV_notification = (TextView) row.findViewById(R.id.txt_msg);
        TextView TV_notificationDate = (TextView) row.findViewById(R.id.TV_notificationDate);
        LinearLayout LL_notification = (LinearLayout) row.findViewById(R.id.LL_notification);
        CheckBox CB_delete = (CheckBox) row.findViewById(R.id.CB_delete);
        if (!NotificationsView.IS_IN_ACTION_MODE){
            CB_delete.setVisibility(View.GONE);
        } else {
            CB_delete.setVisibility(View.VISIBLE);
            CB_delete.setChecked(false);
        }
        if (NotificationsView.IS_IN_SELECT_ALL_MODE){
            CB_delete.setChecked(true);
        }
        TV_notification.setText(notificationInfo != null ? notificationInfo.getNotification() : null);
        LL_notification.setOnLongClickListener(notificationsView);

        if (notificationInfo != null){
            String[] timeStamp = notificationInfo.getDateTime().split("/");
            try {
                DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                String currentDateString = df.format(Calendar.getInstance().getTime());
                Date currentDate = df.parse(currentDateString);
                Date messageDate = df.parse(timeStamp[0]);
                if (messageDate.equals(currentDate)) {
                    TV_notificationDate.setText(timeStamp[1]);
                } else TV_notificationDate.setText(timeStamp[0] +" At "+ timeStamp[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        CB_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationsView.updateSelection(v, position);
            }
        });
        return row;
    }

}
