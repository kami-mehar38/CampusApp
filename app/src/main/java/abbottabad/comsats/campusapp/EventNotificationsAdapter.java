package abbottabad.comsats.campusapp;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This project CampusApp is created by Kamran Ramzan on 02-Jan-17.
 */

class EventNotificationsAdapter extends RecyclerView.Adapter<EventNotificationsAdapter.ViewHolder> {

    private List<EventNotificationsInfo> eventNotificationsInfos = new ArrayList<>();
    private Context context;

    EventNotificationsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_event_notifications, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventNotificationsInfo eventNotificationsInfo = eventNotificationsInfos.get(position);

        holder.TV_notificationSender.setText(eventNotificationsInfo.getName());
        holder.TV_notification.setText(eventNotificationsInfo.getNotification());
        holder.TV_fullTimeStamp.setText(eventNotificationsInfo.getTimeStamp());

        Character firstLetter = eventNotificationsInfo.getName() != null ? eventNotificationsInfo.getName().charAt(0) : 0;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(firstLetter), Color.parseColor("#94b8c4"));
        holder.IV_name.setImageDrawable(drawable);

        String timeDateStamp = eventNotificationsInfo.getTimeStamp();
        if (timeDateStamp != null) {
            String[] timeStamp = timeDateStamp.split("/");
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
    }

    @Override
    public int getItemCount() {
        return eventNotificationsInfos.size();
    }

    void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final EventNotificationsInfo eventNotificationsInfo = eventNotificationsInfos.get(adapterPosition);
        Snackbar snackbar = Snackbar
                .make(recyclerView, "Notification deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventsInfoController.setName(eventNotificationsInfo.getName());
                        EventsInfoController.setNotification(eventNotificationsInfo.getNotification());
                        EventsInfoController.setTimeDate(eventNotificationsInfo.getTimeStamp());
                        new EventsLocalModal(context).addEventNotification();
                        eventNotificationsInfos.add(adapterPosition, eventNotificationsInfo);
                        notifyItemInserted(adapterPosition);
                        recyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
        eventNotificationsInfos.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        new EventsLocalModal(context).deleteNotifications(eventNotificationsInfo.getTimeStamp());
    }

    void addItem(EventNotificationsInfo eventNotificationsInfo, int position) {
        eventNotificationsInfos.add(position, eventNotificationsInfo);
        notifyItemInserted(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView IV_name;
        private TextView TV_notificationSender;
        private TextView TV_notification;
        private TextView TV_timeStamp;
        private TextView TV_fullTimeStamp;
        ViewHolder(View itemView) {
            super(itemView);
            IV_name = (ImageView) itemView.findViewById(R.id.IV_name);
            TV_notificationSender = (TextView) itemView.findViewById(R.id.TV_notificationSender);
            TV_notification = (TextView) itemView.findViewById(R.id.TV_notification);
            TV_timeStamp = (TextView) itemView.findViewById(R.id.TV_timeStamp);
            TV_fullTimeStamp = (TextView) itemView.findViewById(R.id.TV_fullTimeStamp);
        }
    }
}
