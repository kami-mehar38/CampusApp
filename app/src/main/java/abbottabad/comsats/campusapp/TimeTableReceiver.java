package abbottabad.comsats.campusapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


/**
 * This project CampusApp is created by Kamran Ramzan on 9/4/16.
 */
public class TimeTableReceiver extends BroadcastReceiver{
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("RECEIVE_CLASS_NOTIFICATIONS", false)) {
            String subject = intent.getStringExtra("SUBJECT");
            String room = intent.getStringExtra("ROOM");
            int id = intent.getIntExtra("ID", 0);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound);
            long[] pattern = {1000, 1000, 1000, 1000, 1000};

            Intent resultIntent = new Intent(context, TimeTableView.class);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(TimeTableView.class);
            taskStackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(id, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.ic_notification_timetable)
                    .setContentTitle("Timetable").setVibrate(pattern)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setContentText("Next class of " + subject + " is in room# " + room)
                    .setAutoCancel(true).setSound(sound);
            mBuilder.setContentIntent(resultPendingIntent);
            notificationManager.notify(1687, mBuilder.build());
        }

        if (sharedPreferences.getBoolean("SILENT_DURING_CLASS", false)){
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
        }
    }
}
