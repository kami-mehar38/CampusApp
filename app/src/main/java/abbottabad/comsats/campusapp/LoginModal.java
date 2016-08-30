package abbottabad.comsats.campusapp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

/**
 * This project CampusApp is created by Kamran Ramzan on 5/29/16.
 */
public class LoginModal {
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private Context context;
    public LoginModal(Context context) {
        this.context = context;
    }

    public void login(String username, String password){
        new Login().execute(username, password);
    }

    public void resetPassword(String designation, String email){
        new ResetPassword().execute(designation, email);
    }

    private class Login extends AsyncTask<String, Void, String>{
    private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Logging you in... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancel(true);
                progressDialog.cancel();
            }
        });
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = "http://hostellocator.com/getData.php";
        String username, password;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            username = params[0];
            password = params[1];
            String data = URLEncoder.encode("username", "UTF-8") +"="+ URLEncoder.encode(username, "UTF-8") +"&"+
                    URLEncoder.encode("password", "UTF-8") +"="+ URLEncoder.encode(password, "UTF-8");
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
            String STATUS_OK = finalObject.getString("STATUS_OK");

            inputStream.close();
            httpURLConnection.disconnect();
            return STATUS_OK.trim();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String status) {
        progressDialog.cancel();
        startHomePageActivity(status);
    }

    private void startHomePageActivity(String status) {
        if (status != null && status.equals("OK")) {

            SharedPreferences applicationStatus = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = applicationStatus.edit();
            editor.putString("APPLICATION_STATUS", "BLOOD_BANK");
            editor.putBoolean("LOGGED_IN", true);
            editor.putString("REG_ID", "12345");
            editor.apply();

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            final int TOKEN_GOT = sharedPreferences.getInt("TOKEN_GOT", 0);

            if (TOKEN_GOT != 1){
                context.startService(new Intent(context, RegistrationIntentService.class));
            }

            context.startActivity(new Intent(context, HomePageView.class));
            ((Activity) context).finish();
        }
        else if (status != null && status.equals("NOT_OK")){
            Toast.makeText(context, "Invalid login credentials.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Some problem occurred, please try again.", Toast.LENGTH_LONG).show();
        }
    }
    }

    private class ResetPassword extends AsyncTask<String, Void, String>{

        private AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            LoginView.btnSendMail.setVisibility(View.GONE);
            LoginView.isSending.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/resetPassword.php";

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String designation = params[0];
                String email = params[1];
                String data = URLEncoder.encode("designation", "UTF-8") +"="+ URLEncoder.encode(designation, "UTF-8") +"&"+
                        URLEncoder.encode("email", "UTF-8") +"="+ URLEncoder.encode(email, "UTF-8");
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

                inputStream.close();
                httpURLConnection.disconnect();
                return RESPONSE;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String status) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
            if (status != null) {
                LoginView.btnSendMail.setVisibility(View.VISIBLE);
                LoginView.isSending.setVisibility(View.GONE);
                switch (status) {
                    case "CHANGED": {
                        builder.setTitle("Success");
                        builder.setMessage("Password reset successfully. You will receive email of new password within 10 minutes, if don't try resetting password again!");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                    case "NO_MATCH": {
                        builder.setMessage("Email address mismatch. Please enter correct email address!");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                    case "NOT_RESPONDING": {
                        Toast.makeText(context, "Server is not responding yet. Try again later.", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            } else {
                LoginView.btnSendMail.setVisibility(View.VISIBLE);
                LoginView.isSending.setVisibility(View.GONE);
                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
            }
        }

    }
}
