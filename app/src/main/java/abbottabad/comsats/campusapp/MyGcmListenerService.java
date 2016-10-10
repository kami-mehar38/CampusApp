package abbottabad.comsats.campusapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.text.DateFormat;
import java.util.Date;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/3/16.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final int GCM_NOTIFICATION_ID = 13548;
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private Boolean IS_LOGGED_IN;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i("TAG", "From: " + from);
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        IS_LOGGED_IN = sharedPreferences.getBoolean("LOGGED_IN", false);
        String PURPOSE = data.getString("PURPOSE");

        if (PURPOSE != null && PURPOSE.equals("BLOOD_REQUEST")) {
            if (sharedPreferences.getBoolean("RECEIVE_BLOOD_REQUEST", false)) {
                receiveBloodRequest(data);
            }
        }

        if (PURPOSE != null && PURPOSE.equals("BLOOD_REQUEST_RESPONSE")) {
            receiveBloodRequestResponse(data);
        }

        if (PURPOSE != null && PURPOSE.equals("BLOOD_REQUEST_RESPONSE_ACCEPT")
                || PURPOSE != null && PURPOSE.equals("BLOOD_REQUEST_RESPONSE_REJECT")) {
            receiveBloodRequestResponseStatus(data);
        }


        if (APPLICATION_STATUS.equals("FOOD")) {
            if (PURPOSE != null && PURPOSE.equals("FOOD_COMPLAINT")) {
                receiveFoodComplaint(data);
            }
        }

        if (PURPOSE != null && PURPOSE.equals("STATUS_NOTIFICATION")) {
            receiveStatusNotification(data);
        }

        if (PURPOSE != null && PURPOSE.equals("EVENT_NOTIFICATION")) {
            receiveEventNotification(data);
        }
    }

    private void receiveBloodRequestResponseStatus(Bundle data) {
        final String message = data.getString("message");
        createBloodResponseAcceptNotification(message);
    }

    private void createBloodResponseAcceptNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000, 1000, 1000, 1000};

        Intent resultIntent = new Intent(this, BloodBankView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(BloodBankView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_blood)
                .setContentTitle("Blood Bank").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());
    }

    private void receiveBloodRequestResponse(Bundle data) {
        final String stdName = data.getString("stdName");
        final String stdReg = data.getString("stdReg");
        final String stdContact = data.getString("stdContact");
        final String bloodType = data.getString("bloodType");
        final String latitude = data.getString("latitude");
        final String longitude = data.getString("longitude");

        double lat = Double.valueOf(latitude);
        double lon = Double.valueOf(longitude);

        Location locationA = new Location("locationA");
        locationA.setLatitude(lat);
        locationA.setLongitude(lon);

        Location locationB = new Location("locationB");
        locationB.setLatitude(LocationController.getLatitide());
        locationB.setLongitude(LocationController.getLongitude());

        final int distance = (int) locationA.distanceTo(locationB);

        BloodBankResponseController.setName(stdName);
        BloodBankResponseController.setRegistration(stdReg);
        BloodBankResponseController.setBloodGroup(bloodType);
        BloodBankResponseController.setContact(stdContact);
        BloodBankResponseController.setDistance(distance);
        BloodBankResponseController.setIsAccepted(0);
        BloodBankResponseController.setIsRejected(0);

        new BloodBankResponseModal(this).addBloodRequestResponse();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (BloodRequestResponseFragment.responseViewAdapter != null) {
                    ResponseInfo responseInfo = new ResponseInfo();
                    responseInfo.setName(stdName);
                    responseInfo.setRegistration(stdReg);
                    responseInfo.setContact(stdContact);
                    responseInfo.setBloodType(bloodType);
                    responseInfo.setDistance(distance);
                    responseInfo.setIsAccepted(0);
                    responseInfo.setIsRejected(0);
                    BloodRequestResponseFragment.responseViewAdapter.add(responseInfo, 0);
                    BloodRequestResponseFragment.recyclerView.smoothScrollToPosition(0);
                }
            }
        });
        createBloodResponseNotification(stdName + " is willing to donate blood.");
    }

    private void createBloodResponseNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000, 1000, 1000, 1000};

        Intent resultIntent = new Intent(this, BloodBankView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(BloodBankView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_blood)
                .setContentTitle("Blood Bank").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());

    }

    private void receiveFoodComplaint(Bundle data) {
        final String name = data.getString("name");
        final String reg = data.getString("reg");
        final String contact = data.getString("contact");
        final String description = data.getString("description");

        ComplaintPollController.setName(name);
        ComplaintPollController.setRegistration(reg);
        ComplaintPollController.setContact(contact);
        ComplaintPollController.setDescription(description);

        new ComplaintPollLocalModal(this).addComplaint();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ComplaintPollView.complaintPollVIewAdapter != null) {
                    ComplaintsInfo complaintsInfo = new ComplaintsInfo();
                    complaintsInfo.setName(name);
                    complaintsInfo.setRegistration(reg);
                    complaintsInfo.setContact(contact);
                    complaintsInfo.setDescription(description);
                    ComplaintPollView.complaintPollVIewAdapter.add(complaintsInfo, 0);
                    ComplaintPollView.RV_complaints.smoothScrollToPosition(0);
                }

                if (IS_LOGGED_IN) {
                    createComplaintNotification("New Food complaint from " + name);
                }
            }
        });
    }

    private void createComplaintNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000, 1000, 1000, 1000};

        Intent resultIntent = new Intent(this, ComplaintPollView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(ComplaintPollView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_complaint)
                .setContentTitle("Complaint Poll").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());
    }

    private void receiveStatusNotification(Bundle data) {
        String status = data.getString("status");
        String name = data.getString("name");
        createStatusNotification(name + " is " + status + " now");
    }

    private void receiveEventNotification(Bundle data) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String message = data.getString("message");
        NotificationsController.setNotification(message);
        NotificationsController.setDateTime(currentDateTimeString);
        NotificationsController.setMine(0);
        new NotificationsLocalModal(this).addEventNotification();

        final NotificationInfo notificationInfo = new NotificationInfo();
        notificationInfo.setNotification(message);
        notificationInfo.setDateTime(currentDateTimeString);
        notificationInfo.setMine(0);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (NotificationsView.notificationsAdapter != null && NotificationsView.listView != null) {
                    NotificationsView.notificationsAdapter.add(notificationInfo);
                    NotificationsView.notificationsAdapter.notifyDataSetChanged();
                    NotificationsView.listView.setSelection(NotificationsView.notificationsAdapter.getCount());
                }
            }
        });

        createEventNotification(message);
    }

    private void createEventNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000, 1000, 1000, 1000};

        Intent resultIntent = new Intent(this, NotificationsView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(NotificationsView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_notifications)
                .setContentTitle("Event Notifications").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());
    }

    private void createStatusNotification(String status) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000, 1000, 1000, 1000};

        Intent resultIntent = new Intent(this, TrackFacultyView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(TrackFacultyView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_tracking)
                .setContentTitle("Track Faculty").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(status))
                .setContentText(status)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());
    }

    private void receiveBloodRequest(Bundle data) {
        final String stdName = data.getString("stdName");
        final String stdReg = data.getString("stdReg");
        final String stdContact = data.getString("stdContact");
        final String bloodType = data.getString("bloodType");

        BloodBankController.setStdName(stdName);
        BloodBankController.setStdReg(stdReg);
        BloodBankController.setStdContact(stdContact);
        BloodBankController.setBloodType(bloodType);
        BloodBankController.setIsDonated(0);

        new BloodBankLocalModal(this).addBloodRequest();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (BloodRequestsFragment.requestsViewAdapter != null) {
                    RequestsInfo requestsInfo = new RequestsInfo();
                    requestsInfo.setName(stdName);
                    requestsInfo.setRegistration(stdReg);
                    requestsInfo.setContact(stdContact);
                    requestsInfo.setBloodType(bloodType);
                    BloodRequestsFragment.requestsViewAdapter.add(requestsInfo, 0);
                    BloodRequestsFragment.RV_bloodRequests.smoothScrollToPosition(0);
                }
            }
        });
        createBloodNotification("New Blood request from " + stdName);
    }

    private void createBloodNotification(String message) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000, 1000, 1000, 1000};

        Intent resultIntent = new Intent(this, BloodBankView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(BloodBankView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_blood)
                .setContentTitle("Blood Bank").setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());

    }
}
