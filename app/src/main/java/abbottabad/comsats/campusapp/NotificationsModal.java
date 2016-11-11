package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/22/16.
 */
class NotificationsModal {

    private Context context;
    private String notificationType;
    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";

    NotificationsModal(Context context) {
        this.context = context;
    }

    void sendEventNotification(String reg_id, String message, String notificationSender, String notification_type) {
        new SendEventNotification().execute(reg_id, message, notificationSender, notification_type);
    }

    void createGroup(String imageString, String groupName, String userName, String reg_id, String currentDateTimeString){
        new CreateGroup().execute(imageString, groupName, reg_id, currentDateTimeString, userName);
    }

    void getNotificationGroups(){
        new GetNotificationGroups().execute();
    }

    void deleteGroup(String groupName){
        new DeleteGroup().execute(groupName);
    }

    private class SendEventNotification extends AsyncTask<String, Void, String> {

        private String message;
        private String currentDateTimeString;
        private SharedPreferences.Editor editor;

        @Override
        protected void onPreExecute() {
            Animation scaleOut = AnimationUtils.loadAnimation(context, R.anim.send_out);
            NotificationsView.btnSend.startAnimation(scaleOut);
            scaleOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    NotificationsView.btnSend.setVisibility(View.GONE);
                    NotificationsView.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            DateFormat df = new SimpleDateFormat("d MMM yyyy/h:mm a", Locale.getDefault());
            currentDateTimeString = df.format(Calendar.getInstance().getTime());
            NotificationsView.ET_message.setText("");

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/sendEventNotification.php";
            String reg_id = params[0];
            message = params[1];
            String notificationSender = params[2];
            notificationType = params[3];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("reg_id", "UTF-8") + "=" + URLEncoder.encode(reg_id, "UTF-8") + "&" +
                        URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8") + "&" +
                        URLEncoder.encode("notificationSender", "UTF-8") + "=" + URLEncoder.encode(notificationSender, "UTF-8") + "&" +
                        URLEncoder.encode("notificationType", "UTF-8") + "=" + URLEncoder.encode(notificationType, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONArray parentJSON = new JSONArray(stringBuilder.toString());
                JSONObject finalObject = parentJSON.getJSONObject(0);
                String RESPONSE = finalObject.getString("RESPONSE");
                Log.i("TAG", "doInBackground: " + RESPONSE);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return RESPONSE;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Animation scaleIn = AnimationUtils.loadAnimation(context, R.anim.send_in);
            NotificationsView.btnSend.startAnimation(scaleIn);
            scaleIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    NotificationsView.btnSend.setVisibility(View.VISIBLE);
                    NotificationsView.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            if (result != null) {
                switch (result) {
                    case "OK": {
                        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.message_sent);
                        if (mPlayer != null && audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                            final int initVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initVolume, 0);
                            mPlayer.start();
                        }

                        NotificationsController.setNotification(message);
                        NotificationsController.setDateTime(currentDateTimeString);
                        NotificationsController.setMine(1);
                        NotificationsController.setNotificationType(notificationType);
                        new NotificationsLocalModal(context).addEventNotification();

                        NotificationInfo notificationInfo = new NotificationInfo();
                        notificationInfo.setNotification(message);
                        notificationInfo.setDateTime(currentDateTimeString);
                        notificationInfo.setMine(1);

                        NotificationsView.notificationsAdapter.add(notificationInfo);
                        NotificationsView.notificationsAdapter.notifyDataSetChanged();
                        NotificationsView.listView.setSelection(NotificationsView.notificationsAdapter.getCount() - 1);

                        editor.putString(notificationType + "_RECENT_MESSAGE", "Me: " + message);
                        editor.putString(notificationType + "_RECENT_MESSAGE_TIME", currentDateTimeString);
                        editor.apply();
                        break;

                    }
                    case "ERROR": {
                        NotificationsView.ET_message.setText(message);
                        Toast.makeText(context, "Message sending failed", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            } else {
                NotificationsView.ET_message.setText(message);
                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class CreateGroup extends AsyncTask<String, Void, String> {
        private String imageString;
        private String groupName;
        private String reg_id;
        private String timeStamp;
        private String userName;
        private ProgressDialog progressDialog;
        private AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Creating group... This might take some time");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/createNotificationsGroup.php";
            imageString = params[0];
            groupName = params[1];
            reg_id = params[2];
            timeStamp = params[3];
            userName = params[4];

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("imageString", "UTF-8") + "=" + URLEncoder.encode(imageString, "UTF-8") + "&" +
                        URLEncoder.encode("groupName", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8") + "&" +
                        URLEncoder.encode("reg_id", "UTF-8") + "=" + URLEncoder.encode(reg_id, "UTF-8") + "&" +
                        URLEncoder.encode("timeStamp", "UTF-8") + "=" + URLEncoder.encode(timeStamp, "UTF-8") + "&" +
                        URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONArray parentJSON = new JSONArray(stringBuilder.toString());
                JSONObject finalObject = parentJSON.getJSONObject(0);
                String RESPONSE = finalObject.getString("RESPONSE");
                Log.i("TAG", "doInBackground: " + RESPONSE);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return RESPONSE;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });

            if (result != null) {
                switch (result) {
                    case "CREATED": {
                        Toast.makeText(context, "Succesfully created", Toast.LENGTH_LONG).show();
                        ((Activity) context).finish();
                        context.startActivity(new Intent(context, NotificationsHomePage.class));
                        break;
                    }
                    case "EXISTED": {
                        builder.setMessage("Couldn't create group. Group with the same name already exists.");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                    case "ERROR": {
                        builder.setMessage("Error occurred");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                }
            } else {
                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class GetNotificationGroups extends AsyncTask<Void, Void, List<NotificationsListInfo>> {

        private ProgressDialog progressDialog;
        private NotificationsListInfo[] notificationsListInfo;
        private List<NotificationsListInfo> notificationsListInfoList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected List<NotificationsListInfo> doInBackground(Void... params) {
            String stringUrl = "http://hostellocator.com/getNotificationsGroups.php";

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONArray parentJson = new JSONArray(stringBuilder.toString());
                notificationsListInfo = new NotificationsListInfo[parentJson.length()];
                for (int index = 0; index < parentJson.length(); index++) {
                    JSONObject finalObject = parentJson.getJSONObject(index);
                    notificationsListInfo[index] = new NotificationsListInfo();
                    notificationsListInfo[index].setGroupImageUri(finalObject.getString("group_image"));
                    notificationsListInfo[index].setGroupName(finalObject.getString("group_name"));
                    notificationsListInfoList.add(notificationsListInfo[index]);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return notificationsListInfoList;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NotificationsListInfo> notificationsListInfoList) {
            progressDialog.cancel();
            if (notificationsListInfoList != null) {
                if (notificationsListInfoList.size() > 0) {
                    for (int i = 0; i < notificationsListInfoList.size(); i++){
                        NotificationsHomePage.notificationsListAdapter.addItem(notificationsListInfoList.get(i), 0);
                    }
                } else Toast.makeText(context, "No group is created yet", Toast.LENGTH_LONG).show();
            } else Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    private class DeleteGroup extends AsyncTask<String, Void, String> {
        private String groupName;
        private String selectedItem;
        private ProgressDialog progressDialog;
        private AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Deleting group... This might take some time");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/deleteNotificationGroup.php";
            groupName = params[0];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("groupName", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONArray parentJSON = new JSONArray(stringBuilder.toString());
                JSONObject finalObject = parentJSON.getJSONObject(0);
                String RESPONSE = finalObject.getString("RESPONSE");
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return RESPONSE;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            if (result != null) {
                switch (result) {
                    case "DELETED": {
                        Toast.makeText(context, "Succesfully deleted", Toast.LENGTH_LONG).show();
                        MemoryCacheUtils.removeFromCache("http://hostellocator.com/images/" + groupName + ".JPG",
                                ImageLoader.getInstance().getMemoryCache());
                        DiskCacheUtils.removeFromCache("http://hostellocator.com/images/" + groupName + ".JPG",
                                ImageLoader.getInstance().getDiskCache());
                        new NotificationsLocalModal(context).deleteAll(groupName);
                        if (NotificationsHomePage.notificationsListAdapter != null) {
                            NotificationsHomePage.notificationsListAdapter.removeItem(NotificationsUtills.getPosition());
                            Log.i("TAG", "onPostExecute: " + NotificationsUtills.getPosition());
                        }
                        break;
                    }
                    case "ERROR": {
                        builder.setMessage("Error occurred");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                }
            } else {
                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }
}
