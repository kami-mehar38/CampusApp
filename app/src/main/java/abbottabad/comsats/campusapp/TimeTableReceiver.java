package abbottabad.comsats.campusapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * This project CampusApp is created by Kamran Ramzan on 9/4/16.
 */
public class TimeTableReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {1000,1000,1000,1000,1000};

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, TimeTableView.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.complaint)
                .setContentTitle("Complaint Poll").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentText("Coming")
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(1687, mBuilder.build());
    }
}
