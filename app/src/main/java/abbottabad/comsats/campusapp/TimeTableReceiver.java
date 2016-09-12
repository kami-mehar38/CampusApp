package abbottabad.comsats.campusapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * This project CampusApp is created by Kamran Ramzan on 9/4/16.
 */
public class TimeTableReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        String subject = intent.getStringExtra("SUBJECT");
        String teacher = intent.getStringExtra("TEACHER");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000,1000,1000,1000,1000};

        Intent resultIntent = new Intent(context, TimeTableView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(TimeTableView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_notification_timetable)
                .setContentTitle("Timetable").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentText("Class on " + subject + " of " + teacher + " is ahead, get ready")
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(1687, mBuilder.build());
    }
}
