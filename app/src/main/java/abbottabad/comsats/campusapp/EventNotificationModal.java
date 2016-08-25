package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.util.Date;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/22/16.
 */
public class EventNotificationModal {

    private Context context;

    public EventNotificationModal(Context context) {
        this.context = context;
    }

    public void sendEventNotification(String reg_id, String message){
        new SendEventNotification().execute(reg_id, message);
    }

    public class SendEventNotification extends AsyncTask<String, Void, String> {

        private AlertDialog alertDialog;
        private String message;
        private String currentDateTimeString;

        @Override
        protected void onPreExecute() {
            NotificationsView.btnSend.setVisibility(View.GONE);
            NotificationsView.progressBar.setVisibility(View.VISIBLE);
            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            NotificationsView.ET_message.setText("");
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/sendEventNotification.php";
            String reg_id = params[0];
            message = params[1];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("reg_id", "UTF-8") +"="+ URLEncoder.encode(reg_id, "UTF-8") +"&"+
                        URLEncoder.encode("message", "UTF-8") +"="+ URLEncoder.encode(message, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
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
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
            if (result != null) {
                switch (result) {
                    case "OK": {
                        NotificationsView.btnSend.setVisibility(View.VISIBLE);
                        NotificationsView.progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show();
                        NotificationsController.setNotification(message);
                        NotificationsController.setDateTime(currentDateTimeString);
                        NotificationsController.setMine(1);
                        new EventNotificationsLocalModal(context).addEventNotification();

                        EventNotificationInfo eventNotificationInfo = new EventNotificationInfo();
                        eventNotificationInfo.setNotification(message);
                        eventNotificationInfo.setDateTime(currentDateTimeString);
                        eventNotificationInfo.setMine(1);

                        NotificationsView.eventNotificationsAdapter.add(eventNotificationInfo);
                        NotificationsView.eventNotificationsAdapter.notifyDataSetChanged();
                        NotificationsView.listView.setSelection(NotificationsView.eventNotificationsAdapter.getCount()-1);
                        break;
                    }
                    case "ERROR": {
                        NotificationsView.btnSend.setVisibility(View.VISIBLE);
                        NotificationsView.progressBar.setVisibility(View.GONE);

                        builder.setMessage("Message not sent! Please try again.");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                }
            } else {
                NotificationsView.btnSend.setVisibility(View.VISIBLE);
                NotificationsView.progressBar.setVisibility(View.GONE);

                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }
}
