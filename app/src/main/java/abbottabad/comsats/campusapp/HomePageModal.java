package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

/**
 * This project CampusApp is created by Kamran Ramzan on 8/29/16.
 */
public class HomePageModal {

    private Context context;

    public HomePageModal(Context context) {
        this.context = context;
    }

    public void changePassword(String newPassword, String account){
        new ChangePassword().execute(newPassword, account);
    }

    private class ChangePassword extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://10.0.2.2/CampusApp/changePassword.php";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String newPassworde = params[0];
                String account = params[1];

                String data = URLEncoder.encode("newPassword", "UTF-8") +"="+ URLEncoder.encode(newPassworde, "UTF-8") +"&"+
                        URLEncoder.encode("account", "UTF-8") +"="+ URLEncoder.encode(account, "UTF-8");
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

                String finalJson = stringBuilder.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("STATUS");
                JSONObject finalObject = parentArray.getJSONObject(0);
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
        protected void onPostExecute(String response) {
            HomePageView.isChangingPassword.setVisibility(View.GONE);
            HomePageView.ET_changePassword.setVisibility(View.VISIBLE);
            Animation expand = AnimationUtils.loadAnimation(context, R.anim.expand);
            HomePageView.ET_changePassword.startAnimation(expand);
            if (response != null){
                switch (response){
                    case "CHANGED":{
                        Toast.makeText(context, "Password successfully changed!", Toast.LENGTH_LONG).show();
                        break;
                    }

                    case "NOT_CHANGED":{
                        Toast.makeText(context, "Password not changed, try again!", Toast.LENGTH_LONG).show();
                        break;
                    }

                    case "NOT_RESPONDING":{
                        Toast.makeText(context, "Server not responding yet, try again later!", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            } else {
                Toast.makeText(context, "Some problem occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
