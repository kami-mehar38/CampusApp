package abbottabad.comsats.campusapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

/**
 * This project CampusApp is created by Kamran Ramzan on 15-Oct-16.
 */

class TimetableModal {

    private Context context;
    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";


    TimetableModal(Context context) {
        this.context = context;
    }

    void shareTimetable(String mode, String reg_id) {
        new ShareTimetable().execute(mode, reg_id);
    }

    void uploadImage(String imageString){
        new UploadImage().execute(imageString);
    }

    private class ShareTimetable extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        private AlertDialog alertDialog;
        private SharedPreferences sharedPreferences;
        private String mode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Sharing timetable... Please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancel(true);
                        }
                    });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            mode = params[0];
            String reg_id = params[1];
            String stringUrl = "http://hostellocator.com/setMode.php ";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode(mode, "UTF-8") + "&" +
                        URLEncoder.encode("reg_id", "UTF-8") + "=" + URLEncoder.encode(reg_id, "UTF-8");

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
            super.onPostExecute(result);
            progressDialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
            if (result != null) {
                switch (result) {
                    case "UPDATED": {
                        Toast.makeText(context, "Ok", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("SHARE_TIMETABLE", true);
                        editor.apply();
                        break;
                    }
                    case "ERROR": {
                        builder.setMessage("Some error occurred! Please try again.");
                        alertDialog = builder.create();
                        alertDialog.show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("SHARE_TIMETABLE", false);
                        editor.apply();
                        if (mode.equals("Public")){
                            HomePageView.CB_shareTimetable.setChecked(false);
                        } else {
                            HomePageView.CB_shareTimetable.setChecked(true);
                        }
                        break;
                    }
                }
            } else {
                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("SHARE_TIMETABLE", false);
                editor.apply();
                if (mode.equals("Public")){
                    HomePageView.CB_shareTimetable.setChecked(false);
                } else {
                    HomePageView.CB_shareTimetable.setChecked(true);
                }
            }
        }
    }

    private class UploadImage extends AsyncTask<String,Void,String> {

        private SharedPreferences sharedPreferences;
        @Override
        protected String doInBackground(String... params) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("REG_ID", "");
            String uploadImage = params[0];
            String stringUrl = "http://hostellocator.com/upload.php";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") +"&"+
                        URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(uploadImage, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "ERROR";
        }
    }
}
