package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
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

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private Context context;

    public HomePageModal(Context context) {
        this.context = context;
    }

    public void changeUname(String newUname){
        SharedPreferences applicationStatus = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String account = applicationStatus.getString("APPLICATION_STATUS", null);
        new ChangeUname().execute(newUname, account);
    }

    public void changePassword(String newPassword){
        SharedPreferences applicationStatus = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String account = applicationStatus.getString("APPLICATION_STATUS", null);
        new ChangePassword().execute(newPassword, account);
    }

    public void changeEmail(String newEmail){
        SharedPreferences applicationStatus = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String account = applicationStatus.getString("APPLICATION_STATUS", null);
        new ChangeEmail().execute(newEmail, account);
    }

    private class ChangeUname extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://10.0.2.2/CampusApp/changeUname.php";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String newUname = params[0];
                String account = params[1];
                String data = URLEncoder.encode("newUname", "UTF-8") +"="+ URLEncoder.encode(newUname, "UTF-8") +"&"+
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
            HomePageView.isChangingUnam.setVisibility(View.GONE);
            HomePageView.ET_changeUname.setVisibility(View.VISIBLE);
            Animation expand = AnimationUtils.loadAnimation(context, R.anim.expand);
            HomePageView.ET_changeUname.startAnimation(expand);
            if (response != null){
                switch (response){
                    case "CHANGED":{
                        Toast.makeText(context, "Username successfully changed!", Toast.LENGTH_LONG).show();
                        break;
                    }

                    case "NOT_CHANGED":{
                        Toast.makeText(context, "Username not changed, try again!", Toast.LENGTH_LONG).show();
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

    private class ChangePassword extends AsyncTask<String, Void, String>{

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

    private class ChangeEmail extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://10.0.2.2/CampusApp/changeEmail.php";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String newEmail = params[0];
                String account = params[1];
                String data = URLEncoder.encode("newEmail", "UTF-8") +"="+ URLEncoder.encode(newEmail, "UTF-8") +"&"+
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
            HomePageView.isChangingEmail.setVisibility(View.GONE);
            HomePageView.ET_changeEmail.setVisibility(View.VISIBLE);
            Animation expand = AnimationUtils.loadAnimation(context, R.anim.expand);
            HomePageView.ET_changeEmail.startAnimation(expand);
            if (response != null){
                switch (response){
                    case "CHANGED":{
                        Toast.makeText(context, "Email successfully changed!", Toast.LENGTH_LONG).show();
                        break;
                    }

                    case "NOT_CHANGED":{
                        Toast.makeText(context, "Email not changed, try again!", Toast.LENGTH_LONG).show();
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
