package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
public class EventNotificationsAdapter extends ArrayAdapter<EventNotificationInfo> {

    private List<EventNotificationInfo> chatMessageList = new ArrayList<>();
    private NotificationsView notificationsView;

    @Override
    public void remove(EventNotificationInfo object) {
        chatMessageList.remove(object);
        super.remove(object);
    }

    @Override
    public void add(EventNotificationInfo object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public EventNotificationsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.notificationsView = (NotificationsView) context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public EventNotificationInfo getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final EventNotificationInfo eventNotificationInfo = getItem(position);
        View row;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (eventNotificationInfo.getMine() == 1) {
            row = inflater.inflate(R.layout.chat_right, parent, false);
        }else{
            row = inflater.inflate(R.layout.chat_left, parent, false);
        }

        TextView TV_notification = (TextView) row.findViewById(R.id.txt_msg);
        TextView TV_notificationDate = (TextView) row.findViewById(R.id.TV_notificationDate);
        CheckBox CB_delete = (CheckBox) row.findViewById(R.id.CB_delete);
        if (!NotificationsView.IS_IN_ACTION_MODE){
            CB_delete.setVisibility(View.GONE);
        } else {
            CB_delete.setVisibility(View.VISIBLE);
            CB_delete.setChecked(false);
        }
        TV_notification.setText(eventNotificationInfo.getNotification());
        TV_notification.setOnLongClickListener(notificationsView);
        TV_notificationDate.setText(eventNotificationInfo.getDateTime());
        CB_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationsView.updateSelection(v, position);
            }
        });
        return row;
    }

}
