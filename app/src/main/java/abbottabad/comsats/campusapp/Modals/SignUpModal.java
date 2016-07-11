package abbottabad.comsats.campusapp.Modals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import abbottabad.comsats.campusapp.Views.HomePageView;
import abbottabad.comsats.campusapp.GCM_Classes.RegistrationIntentService;

/**
 * Created by Kamran Ramzan on 6/26/16.
 */
public class SignUpModal extends AsyncTask<String, Void, String> {
    private Context context;
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private AlertDialog alertDialog;


    public SignUpModal(Context context) {
        this.context = context;
    }

    private ProgressDialog progressDialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Singing you up... Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {


        String stringUrl = "http://amgbuilders.co.nf/addStudent.php";
        String std_name, std_id, std_section;
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

            String data = URLEncoder.encode("std_name", "UTF-8") + "=" + URLEncoder.encode(std_name, "UTF-8") +"&"+
                    URLEncoder.encode("std_id", "UTF-8") + "=" + URLEncoder.encode(std_id, "UTF-8") +"&"+
                    URLEncoder.encode("std_section", "UTF-8") + "=" + URLEncoder.encode(std_section, "UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
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
        return "ERROR";
    }

        @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
            progressDialog.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
            switch (result) {
                case "ADDED":
                    SharedPreferences applicationStatus = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = applicationStatus.edit();
                    editor.putString("APPLICATION_STATUS", "STUDENT");
                    editor.apply();
                    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                    final int TOKEN_GOT = sharedPreferences.getInt("TOKEN_GOT", 0);

                    if (TOKEN_GOT != 1){
                        context.startService(new Intent(context, RegistrationIntentService.class));
                    }
                    context.startActivity(new Intent(context, HomePageView.class));
                    break;
                case "EXISTED": {
                    builder.setMessage("Couldn't sign up, student already exists!");
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                }
                case "ERROR": {
                    builder.setMessage("Some error occurred! Please try again.");
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                }
            }
    }
}
