package abbottabad.comsats.campusapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
class ComplaintPollModal {

    private Context context;

    ComplaintPollModal(Context context) {
        this.context = context;
    }

    void sendComplaint(String name, String regID, String contact, String description){
        new SendComplaint().execute(name, regID, contact, description);
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
            String name, registration, contact, description;
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
                String data = URLEncoder.encode("name", "UTF-8") +"="+ URLEncoder.encode(name, "UTF-8") +"&"+
                        URLEncoder.encode("registration", "UTF-8") +"="+ URLEncoder.encode(registration, "UTF-8") +"&"+
                        URLEncoder.encode("contact", "UTF-8") +"="+ URLEncoder.encode(contact, "UTF-8") +"&"+
                        URLEncoder.encode("description", "UTF-8") +"="+ URLEncoder.encode(description, "UTF-8");
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
}
