package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private Context context;

    @Override
    public void add(EventNotificationInfo object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public EventNotificationsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public EventNotificationInfo getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        EventNotificationInfo eventNotificationInfo = getItem(position);
        View row;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (eventNotificationInfo.getMine() == 1) {
            row = inflater.inflate(R.layout.chat_right, parent, false);
        }else{
            row = inflater.inflate(R.layout.chat_left, parent, false);
        }

        TextView TV_notification = (TextView) row.findViewById(R.id.txt_msg);
        TextView TV_notificationDate = (TextView) row.findViewById(R.id.TV_notificationDate);
        LinearLayout deleteLayout = (LinearLayout) row.findViewById(R.id.deleteLayout);
        if (!NotificationsView.IS_IN_ACTION_MODE){
            deleteLayout.setVisibility(View.GONE);
        } else {
            deleteLayout.setVisibility(View.VISIBLE);
        }
        TV_notification.setText(eventNotificationInfo.getNotification());
        TV_notification.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NotificationsView.IS_IN_ACTION_MODE = true;
                NotificationsView.eventNotificationsAdapter.notifyDataSetChanged();
                return true;
            }
        });
        TV_notificationDate.setText(eventNotificationInfo.getDateTime());
        return row;
    }

}
