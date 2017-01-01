package abbottabad.comsats.campusapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/3/16.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final int GCM_NOTIFICATION_ID = 13548;
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private Boolean IS_LOGGED_IN;
    private SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i("TAG", "From: " + from);
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
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
                if (sharedPreferences.getBoolean("RECEIVE_COMPLAINTS", false)) {
                    receiveFoodComplaint(data);
                }
            }
        }

        if (PURPOSE != null && PURPOSE.equals("STATUS_NOTIFICATION")) {
            if (sharedPreferences.getBoolean("RECEIVE_FACULTY_NOTIFICATIONS", false)) {
                receiveStatusNotification(data);
            }
        }

        if (PURPOSE != null && PURPOSE.equals("EVENT_NOTIFICATION")) {
            if (sharedPreferences.getBoolean("RECEIVE_EVENT_NOTIFICATIONS", false)) {
                receiveEventNotification(data);
            }
        }

        if (PURPOSE != null && PURPOSE.equals("GROUP_REQUEST")) {
            receiveGroupRequest(data);

        }

        if (PURPOSE != null && PURPOSE.equals("GROUP_REQUEST_RESPONSE")) {
            receiveGroupRequestResponse(data);
        }
    }

    private void receiveBloodRequestResponseStatus(Bundle data) {
        final String message = data.getString("message");
        createBloodResponseAcceptNotification(message);
    }

    private void createBloodResponseAcceptNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000};

        Intent resultIntent = new Intent(this, BloodBankView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(BloodBankView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_blood)
                .setContentTitle("Blood Bank").setVibrate(pattern)
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
        long[] pattern = {1000, 1000};

        Intent resultIntent = new Intent(this, BloodBankView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(BloodBankView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_blood)
                .setContentTitle("Blood Bank").setVibrate(pattern)
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
        final String imageUrl = data.getString("imagePath");

        ComplaintPollController.setName(name);
        ComplaintPollController.setRegistration(reg);
        ComplaintPollController.setContact(contact);
        ComplaintPollController.setDescription(description);
        ComplaintPollController.setImageUrl(imageUrl);

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
                    complaintsInfo.setImageUrl(imageUrl);
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
        long[] pattern = {1000, 1000};

        Intent resultIntent = new Intent(this, ComplaintPollView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(ComplaintPollView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_complaint)
                .setContentTitle("Complaint Poll").setVibrate(pattern)
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
        DateFormat df = new SimpleDateFormat("d MMM yyyy/h:mm a", Locale.getDefault());
        String currentDateTimeString = df.format(Calendar.getInstance().getTime());
        String message = data.getString("message");
        String notificationSender = data.getString("notificationSender");
        final String notificationType = data.getString("notificationType");
        NotificationsController.setNotification(message);
        NotificationsController.setNotificationSender(notificationSender);
        NotificationsController.setDateTime(currentDateTimeString);
        NotificationsController.setMine(0);
        NotificationsController.setNotificationType(notificationType);

        if (sharedPreferences.getBoolean(notificationType + "_EXISTS", false) &&
                sharedPreferences.getBoolean(notificationType + "_IS_JOINED", false) ||
                sharedPreferences.getBoolean(notificationType + "_CREATED_BY_ME", false)) {
            new NotificationsLocalModal(this).addEventNotification();

            /**
             * Below is the code to set the message counter of a specific group
             * It shows the number of unread messages
             */

            int badgeCount = sharedPreferences.getInt(notificationType + "_COUNT", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            badgeCount++;
            editor.putInt(notificationType + "_COUNT", badgeCount);
            editor.putString(notificationType + "_RECENT_MESSAGE", message);
            editor.putString(notificationType + "_RECENT_MESSAGE_TIME", currentDateTimeString);
            editor.apply();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (NotificationsGroupFragment.notificationsListAdapter != null) {
                        NotificationsGroupFragment.notificationsListAdapter.notifyDataSetChanged();
                    }
                }
            });

            final NotificationInfo notificationInfo = new NotificationInfo();
            notificationInfo.setNotification(message);
            notificationInfo.setNotificationSender(notificationSender);
            notificationInfo.setDateTime(currentDateTimeString);
            notificationInfo.setMine(0);
            notificationInfo.setNotificationType(notificationType);
            final String NOTIFICATION_TYPE = sharedPreferences.getString("NOTIFICATION_TYPE", null);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (NotificationsView.notificationsAdapter != null && NotificationsView.listView != null) {
                        if (notificationType != null && NOTIFICATION_TYPE != null && notificationType.equals(NOTIFICATION_TYPE)) {
                            NotificationsView.notificationsAdapter.add(notificationInfo);
                            NotificationsView.notificationsAdapter.notifyDataSetChanged();
                            NotificationsView.listView.setSelection(NotificationsView.notificationsAdapter.getCount());
                        }
                    }
                }
            });

            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            boolean isMuted = sharedPreferences.getBoolean(notificationType + "_MUTED", false);
            boolean isChatOpen = sharedPreferences.getBoolean("IS_CHAT_OPEN", false);
            if (!isMuted && audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL && isChatOpen) {
                final int initVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initVolume, 0);
                MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.message_received);
                if (mPlayer != null)
                    mPlayer.start();
            } else if (!isMuted && !isChatOpen) {
                createEventNotification(message);
            }
        }
    }

    private void createEventNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000};

        Intent resultIntent = new Intent(this, NotificationsHomePage.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(NotificationsView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_notifications)
                .setContentTitle("Event Notifications").setVibrate(pattern)
                .setContentText(message)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());
    }

    private void createStatusNotification(String status) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        long[] pattern = {1000, 1000};

        Intent resultIntent = new Intent(this, TrackFacultyView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(TrackFacultyView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_tracking)
                .setContentTitle("Track Faculty").setVibrate(pattern)
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
        long[] pattern = {1000, 1000};

        Intent resultIntent = new Intent(this, BloodBankView.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(BloodBankView.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_notification_blood)
                .setContentTitle("Blood Bank").setVibrate(pattern)
                .setContentText(message)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(GCM_NOTIFICATION_ID, mBuilder.build());

    }

    private void receiveGroupRequest(Bundle data) {
        DateFormat df = new SimpleDateFormat("d MMM yyyy/h:mm a", Locale.getDefault());
        final String currentDateTimeString = df.format(Calendar.getInstance().getTime());
        final String name = data.getString("name");
        final String notificationType = data.getString("groupName");
        final String regId = data.getString("regId");


        if (sharedPreferences.getBoolean(notificationType + "_EXISTS", false)) {

            NotificationsRequestController.setName(name);
            NotificationsRequestController.setRegId(regId);
            NotificationsRequestController.setGroupName(notificationType);
            NotificationsRequestController.setTimeStamp(currentDateTimeString);
            new NotificationsRequestsLocalModal(this).addGroupRequest();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (NotificationsView.pendingGroupRequestsAdapter != null) {
                        PendingGroupRequestsInfo pendingGroupRequestsInfo = new PendingGroupRequestsInfo();
                        pendingGroupRequestsInfo.setName(name);
                        pendingGroupRequestsInfo.setRegId(regId);
                        pendingGroupRequestsInfo.setGroupName(notificationType);
                        pendingGroupRequestsInfo.setTimeStamp(currentDateTimeString);
                        NotificationsView.pendingGroupRequestsAdapter.addItem(pendingGroupRequestsInfo, 0);
                        NotificationsView.recyclerView.smoothScrollToPosition(0);
                    }
                }
            });

            SharedPreferences.Editor editor = sharedPreferences.edit();
            int badgeCount = sharedPreferences.getInt(notificationType + "_REQUESTS_COUNT", 0);
            badgeCount++;
            editor.putInt(notificationType + "_REQUESTS_COUNT", badgeCount);
            editor.apply();

            final int finalBadgeCount = badgeCount;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (NotificationsView.TV_requestsCount != null) {
                        NotificationsView.TV_requestsCount.setText(String.valueOf(finalBadgeCount));
                    }
                }
            });

            createEventNotification(name + " wants to join " + notificationType);
        }
    }

    private void receiveGroupRequestResponse(Bundle data) {
        String response = data.getString("response");
        String groupName = data.getString("groupName");
        Log.i("TAG", "receiveGroupRequestResponse: " + groupName);
        String status = data.getString("status");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (status != null && status.equals("Accepted")) {
            editor.putBoolean(groupName + "_IS_JOINED", true);
        } else if (status != null && status.equals("Rejected"))
            editor.putBoolean(groupName + "_IS_JOINED", false);
        editor.apply();
        createEventNotification(response);
    }

}
