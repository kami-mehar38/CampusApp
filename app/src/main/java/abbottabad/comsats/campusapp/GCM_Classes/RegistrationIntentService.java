package abbottabad.comsats.campusapp.GCM_Classes;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

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

/**
 * Created by Kamran Ramzan on 6/2/16.
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

            saveTokenPreferences();
            sendRegTokenToServer();

        } catch (IOException e) {
            Log.i(TAG, "onHandleIntent: Couldn't get Token");
        }
    }

    private void saveTokenPreferences() {
        if (token != null) {
            SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("TOKEN_GOT", 1);
            editor.apply();
        }
    }

    private void sendRegTokenToServer() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        String stringUrl = "http://10.0.2.2/CampusApp/insertToken.php";
        new SaveTokenInBackground().execute(stringUrl, APPLICATION_STATUS, token);
    }

    private class SaveTokenInBackground extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String stringUrl = params[0];
            String designation = params[1];
            String token = params[2];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("designation", "UTF-8") +"="+ URLEncoder.encode(designation, "UTF-8") +"&"+
                        URLEncoder.encode("token", "UTF-8") +"="+ URLEncoder.encode(token, "UTF-8");
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
    }
}