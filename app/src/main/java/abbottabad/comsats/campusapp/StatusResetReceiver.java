package abbottabad.comsats.campusapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * This project CampusApp is created by Kamran Ramzan on 12-Dec-16.
 */

public class StatusResetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String status = intent.getStringExtra("STATUS");
        editor.putBoolean("SHOW_PROGRESS", false);
        editor.putString("RESET_TIME", "Set time");
        editor.putBoolean("IS_AUTO_RESET", false);
        editor.apply();

        String teacherId = intent.getStringExtra("TEACHER_ID");
        TrackFacultyModal trackFacultyModal = new TrackFacultyModal(context);
        trackFacultyModal.updateStatus(status, teacherId);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000};

        Intent resultIntent = new Intent(context, TrackFacultyView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(TrackFacultyView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_notification_tracking)
                .setContentTitle("Faculty availability").setVibrate(pattern)
                .setContentText("Status auto reset to " + status)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(0, mBuilder.build());
    }
}
