package abbottabad.comsats.campusapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/16/16.
 */
class TrackFacultyModal {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private Context context;

    TrackFacultyModal(Context context) {
        this.context = context;
    }

    void retrieveStatus(String TEACHER_ID) {
        new RetrieveStatus().execute(TEACHER_ID);
    }

    void updateStatus(String status, String teacher_id) {
        new UpdateStatus().execute(status, teacher_id);
    }

    private class RetrieveStatus extends AsyncTask<String, Void, List<StatusInfo>> {

        private ProgressDialog progressDialog;
        private String APPLICATION_STATUS;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Retrieving faculty information...");
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancel(true);
                            TrackFacultyView.SRL_facultyStatus.setRefreshing(false);
                        }
                    });
            progressDialog.show();
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        }

        @Override
        protected List<StatusInfo> doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/retrieveStatus.php";
            StatusInfo[] statusInfo;
            List<StatusInfo> statusInfoList = new ArrayList<>();
            String TEACHER_ID = params[0];

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("TEACHER_ID", "UTF-8") + "=" + URLEncoder.encode(TEACHER_ID, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                JSONObject parentObject = new JSONObject(stringBuilder.toString());
                JSONArray othersArray = parentObject.getJSONArray("others");

                statusInfo = new StatusInfo[othersArray.length()];
                for (int index = 0; index < othersArray.length(); index++) {
                    JSONObject finalObject = othersArray.getJSONObject(index);
                    statusInfo[index] = new StatusInfo();
                    statusInfo[index].setTeacherName(finalObject.getString("name"));
                    statusInfo[index].setTeacherRegistration(finalObject.getString("reg_id"));
                    statusInfo[index].setStatus(finalObject.getString("status"));
                    statusInfo[index].setMode(finalObject.getString("timetable"));
                    statusInfoList.add(statusInfo[index]);
                }


                if (APPLICATION_STATUS.equals("TEACHER")) {
                    JSONArray myArray = parentObject.getJSONArray("my");
                    JSONObject finalObject = myArray.getJSONObject(0);
                    StatusInfo statusInfo1 = new StatusInfo();
                    statusInfo1.setTeacherName(finalObject.getString("name"));
                    statusInfo1.setStatus(finalObject.getString("status"));
                    statusInfoList.add(0, statusInfo1);
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return statusInfoList;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<StatusInfo> s) {
            progressDialog.cancel();
            TrackFacultyView.SRL_facultyStatus.setRefreshing(false);
            if (s != null) {
                if (APPLICATION_STATUS.equals("TEACHER")) {
                    StatusInfo statusInfo = s.get(0);
                    s.remove(0);
                    TrackFacultyView.TV_myStatus.setText(statusInfo.getStatus());
                }
                TrackFacultyView.statusInfoList = s;
                StatusViewAdapter statusViewAdapter = new StatusViewAdapter(s);
                ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(statusViewAdapter);
                scaleInAnimationAdapter.setFirstOnly(false);
                TrackFacultyView.recyclerView.setAdapter(scaleInAnimationAdapter);

            } else {
                Toast.makeText(context, "Couldn't retrieve information.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateStatus extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;
        private AlertDialog alertDialog;
        private String status;
        private String teacher_id;
        private SharedPreferences sharedPreferences;

        @Override
        protected void onPreExecute() {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("SHOW_PROGRESS", true)) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Updating your status...");
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
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/updateStatus.php";
            status = params[0];
            teacher_id = params[1];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&" +
                        URLEncoder.encode("reg_id", "UTF-8") + "=" + URLEncoder.encode(teacher_id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                JSONArray parentJSON = new JSONArray(stringBuilder.toString());
                JSONObject finalObject = parentJSON.getJSONObject(0);
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
        protected void onPostExecute(String result) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (sharedPreferences.getBoolean("SHOW_PROGRESS", false)) {
                progressDialog.cancel();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
            switch (result) {
                case "UPDATED": {
                    Toast.makeText(context, "Status is successfully updated!", Toast.LENGTH_LONG).show();
                    switch (status) {
                        case "Available":
                            editor.putInt("SELECTED_STATUS", 0);
                            break;
                        case "Busy":
                            editor.putInt("SELECTED_STATUS", 1);
                            break;
                        case "On Leave":
                            editor.putInt("SELECTED_STATUS", 2);
                            break;
                    }
                    editor.putString("CURRENT_STATUS", status);
                    editor.putBoolean("SHOW_PROGRESS", false);
                    editor.apply();
                    if (TrackFacultyView.TV_myStatus != null)
                        TrackFacultyView.TV_myStatus.setText(status);
                    new sendStatusNotification().execute(teacher_id, status);
                    break;
                }
                case "ERROR": {
                    builder.setMessage("Some error occurred! Please try again.");
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                }
                default: {
                    builder.setMessage("Some error occurred! Please try again.");
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                }
            }
        }
    }

    private class sendStatusNotification extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/sendStatusNotification.php";
            String teacher_id = params[0];
            String status = params[1];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("reg_id", "UTF-8") + "=" + URLEncoder.encode(teacher_id, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
