package abbottabad.comsats.campusapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/2/16.
 */
public class RegistrationIntentService extends IntentService {
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";

    private static final String TAG = "RegServicePush";
    private static final String GCM_SENDER_ID = "398454349636";
    private static String token = null;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            token = instanceID.getToken(GCM_SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegTokenToServer();
        } catch (IOException e) {
            Log.i(TAG, "onHandleIntent: Couldn't get Token");
        }
    }

    private void sendRegTokenToServer() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        final String REG_ID = sharedPreferences.getString("REG_ID", "NULL");
        String stringUrl = "http://hostellocator.com/insertToken.php";
        new SaveTokenInBackground().execute(stringUrl, APPLICATION_STATUS, token, REG_ID);
    }

    private class SaveTokenInBackground extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String designation = params[1];
            String token = params[2];
            String reg_id = params[3];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("designation", "UTF-8") +"="+ URLEncoder.encode(designation, "UTF-8") +"&"+
                        URLEncoder.encode("token", "UTF-8") +"="+ URLEncoder.encode(token, "UTF-8") +"&"+
                        URLEncoder.encode("reg_id", "UTF-8") +"="+ URLEncoder.encode(reg_id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && token != null){
                SharedPreferences sharedPreferences = RegistrationIntentService.
                        this.getSharedPreferences(
                        PREFERENCE_FILE_KEY,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("TOKEN_GOT", 1);
                editor.apply();
            }
        }
    }
}