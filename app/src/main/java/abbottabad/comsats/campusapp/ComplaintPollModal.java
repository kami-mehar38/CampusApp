package abbottabad.comsats.campusapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

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
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
class ComplaintPollModal {

    private Context context;

    ComplaintPollModal(Context context) {
        this.context = context;
    }

    void sendComplaint(String name, String regID, String contact, String description, String imageName, String imageString){
        new SendComplaint().execute(name, regID, contact, description, imageName, imageString);
    }

    void deleteComplaint(String imageName) {
        new DeleteComplaint().execute(imageName);
    }

    private class SendComplaint extends AsyncTask<String, Void, String>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Sending Complaint...");
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
            String stringUrl = "http://hostellocator.com/sendComplaint.php";
            String name, registration, contact, description, imageName, imageString;
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                name = params[0];
                registration = params[1];
                contact = params[2];
                description = params[3];
                imageName = params[4];
                imageString = params[5];
                Log.i("TAG", "doInBackground: " + imageName);
                Log.i("TAG", "doInBackground: " + imageString);
                String data = URLEncoder.encode("name", "UTF-8") +"="+ URLEncoder.encode(name, "UTF-8") +"&"+
                        URLEncoder.encode("registration", "UTF-8") +"="+ URLEncoder.encode(registration, "UTF-8") +"&"+
                        URLEncoder.encode("contact", "UTF-8") +"="+ URLEncoder.encode(contact, "UTF-8") +"&"+
                        URLEncoder.encode("description", "UTF-8") +"="+ URLEncoder.encode(description, "UTF-8") +"&"+
                        URLEncoder.encode("imageName", "UTF-8") +"="+ URLEncoder.encode(imageName, "UTF-8") +"&"+
                        URLEncoder.encode("imageString", "UTF-8") +"="+ URLEncoder.encode(imageString, "UTF-8");
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
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            Log.i("TAG", "onPostExecute: " + s);
            if (s != null && s.equals("OK")) {
                Toast.makeText(context, "Complaint sent.", Toast.LENGTH_LONG).show();
            } else if (s != null && s.equals("ERROR")){
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, "Some error occurred, please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class DeleteComplaint extends AsyncTask<String, Void, String> {
        private String imageName;
        private ProgressDialog progressDialog;
        private AlertDialog alertDialog;
        private SharedPreferences.Editor editor;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Deleting complaint");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/deleteComplaint.php";
            imageName = params[0];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("imageName", "UTF-8") + "=" + URLEncoder.encode(imageName, "UTF-8");
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
            progressDialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            if (result != null) {
                switch (result) {
                    case "DELETED": {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        MemoryCacheUtils.removeFromCache("http://hostellocator.com/images/" + imageName + ".JPG",
                                ImageLoader.getInstance().getMemoryCache());
                        DiskCacheUtils.removeFromCache("http://hostellocator.com/images/" + imageName + ".JPG",
                                ImageLoader.getInstance().getDiskCache());
                        Log.i("TAG", "onPostExecute: " + NotificationsUtills.getPosition());
                        new ComplaintPollLocalModal(context).deleteComplaint(imageName);
                        if (ComplaintPollView.complaintPollVIewAdapter != null) {
                            ComplaintPollView.complaintPollVIewAdapter.remove(NotificationsUtills.getPosition());
                        }
                        break;
                    }
                    case "ERROR": {
                        builder.setMessage("Error occurred");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                }
            } else {
                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }
}
