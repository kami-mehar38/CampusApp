package abbottabad.comsats.campusapp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
public class LoginModal extends AsyncTask<String, Void, String>{

    private ProgressDialog progressDialog;
    private Context context;

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";

    public LoginModal(Context context) {
        this.context = context;
    }

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
