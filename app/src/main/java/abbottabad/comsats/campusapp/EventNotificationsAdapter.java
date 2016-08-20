package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
public class EventNotificationsAdapter extends ArrayAdapter<EventNotificationInfo> {

    private Activity activity;
    private List<EventNotificationInfo> messages;

    public EventNotificationsAdapter(Activity context, int resource, List<EventNotificationInfo> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        EventNotificationInfo eventNotificationInfo = getItem(position);
        int viewType = getItemViewType(position);

        if (eventNotificationInfo.isMine()) {
            layoutResource = R.layout.chat_left;
        } else {
            layoutResource = R.layout.chat_right;
        }

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        //set message content

        viewHolder.TV_notification.setText(eventNotificationInfo.getNotification());
        viewHolder.TV_notificationDate.setText("17/08/2016-05:45pm");

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    private class ViewHolder{
        private TextView TV_notification;
        private TextView TV_notificationDate;
        public ViewHolder(View view){
            TV_notification = (TextView) view.findViewById(R.id.txt_msg);
            TV_notificationDate = (TextView) view.findViewById(R.id.TV_notificationDate);
        }
    }
}
