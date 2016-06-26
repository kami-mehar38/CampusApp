package abbottabad.comsats.campusapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Kamran Ramzan on 6/2/16.
 */
public class RegistrationIntentService extends IntentService {
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";

    private static final String TAG = "RegServicePush";
    private static final String GCM_SENDER_ID = "398454349636";
    public static String token = null;


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
            SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
            if (APPLICATION_STATUS.equals("BLOOD_BANK")){
                String stringUrl = "http://10.0.2.2/CampusApp/insertTokenOfBloodBank.php";
                sendRegTokenToServer(stringUrl, LoginView.designation , token, LoginView.username, LoginView.password);
            }

        } catch (IOException e) {
            Log.i(TAG, "onHandleIntent: Couldn't get Token");
        }

    }

    private void saveTokenPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("TOKEN_GOT", 1);
        editor.apply();
    }

    private void sendRegTokenToServer(String stringUrl, String designation, String token, String username, String password) {
        new SendTokenInBackground().execute(stringUrl, designation, token, username, password);
    }

    private void sendRegTokenToServer(){

    }
    public class SendTokenInBackground extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String designation, token, username, password;
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                designation = params[1];
                token = params[2];
                username = params[3];
                password = params[4];
                String data = URLEncoder.encode("designation", "UTF-8") + "=" + URLEncoder.encode(designation, "UTF-8") +"&"+
                              URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8") +"&"+
                              URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") +"&"+
                              URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return token;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "onPostExecute: " + s);
        }
    }
}
