package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
 * Created by Kamran Ramzan on 6/26/16.
 */
public class SignUpModal extends AsyncTask<String, Void, String> {
    private Context context;
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public SignUpModal(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {


        String stringUrl = "http://10.0.2.2/CampusApp/addStudent.php";
        String std_name, std_id, std_section, gcm_token;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            std_name = params[0];
            std_id = params[1];
            std_section = params[2];
            gcm_token = RegistrationIntentService.token;
            String data = URLEncoder.encode("std_name", "UTF-8") + "=" + URLEncoder.encode(std_name, "UTF-8") +"&"+
                    URLEncoder.encode("std_id", "UTF-8") + "=" + URLEncoder.encode(std_id, "UTF-8") +"&"+
                    URLEncoder.encode("std_section", "UTF-8") + "=" + URLEncoder.encode(std_section, "UTF-8") +"&"+
                    URLEncoder.encode("gcm_token", "UTF-8") + "=" + URLEncoder.encode(gcm_token, "UTF-8");
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SharedPreferences applicationStatus = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = applicationStatus.edit();
        editor.putString("APPLICATION_STATUS", "STUDENT");
        editor.apply();
    }
}
