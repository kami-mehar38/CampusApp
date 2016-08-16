package abbottabad.comsats.campusapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Kamran Ramzan on 6/3/16.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final int GCM_NOTIFICATION_ID = 13548;
    private static final String BLOOD_REQUEST_NOTIFICATION_MESSAGE = "New Blood request from ";
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";

    @Override
    public void onMessageReceived(String from, Bundle data) {


        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");

        if (APPLICATION_STATUS.equals("BLOOD_BANK")){
            String stdName = data.getString("stdName");
            String stdReg = data.getString("stdReg");
            String stdContact = data.getString("stdContact");
            String bloodType = data.getString("bloodType");
            Log.i("TAG", "From: " + from);

            BloodBankController.setStdName(stdName);
            BloodBankController.setStdReg(stdReg);
            BloodBankController.setStdContact(stdContact);
            BloodBankController.setBloodType(bloodType);
            createNotification(BLOOD_REQUEST_NOTIFICATION_MESSAGE + stdName);

            new BloodRequestsFragment.BloodBankLocalModal(this).addBloodRequest();
            new BloodRequestsFragment.BloodBankLocalModal(this).viewBloodRequests();

        } else {
            // normal downstream message.
           // createNotification(BLOOD_REQUEST_NOTIFICATION_MESSAGE + stdName);
        }
        //createNotification( message);
    }

    private void createNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {1000,1000,1000,1000,1000};

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, BloodBankView.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.blood)
                .setContentTitle("Blood Request").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());

    }
}
