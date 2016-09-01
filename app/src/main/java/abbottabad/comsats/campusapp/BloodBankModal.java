package abbottabad.comsats.campusapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static abbottabad.comsats.campusapp.BloodDonorsFragment.*;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/4/16.
 */
public class BloodBankModal {


    private Context context;
    private AlertDialog alertDialog;

    public BloodBankModal(Context context) {
        this.context = context;
    }

    /**
     *
     * @param name name of the student, teacher who sends request
     * @param registration registration id of the student, teacher who sends request
     * @param contact contact# of the student, teacher who sends request
     * @param bloodType blood group required by the student or teacher
     *
     *                  This method call to the sendRequestInBackground class extended by AsynchTask,
     *                  to send the Blood Request using HTTP Protocol
     */

    public void sendRequest(String name, String registration, String contact, String bloodType){
        new sendRequestInBackground().execute(name, registration, contact, bloodType);
    }

    public void retrieveDonors(String bloodType) {
        new RetrieveDonors().execute(bloodType);
    }

    public void addDonor(String name, String regID, String bloodType, String contact, String bleededDate){
        new AddDonor().execute(name, regID, bloodType, contact, bleededDate);
    }

    public void updateBleedingDate(String registration, String bleededDate){
        new UpdateBleedingDate().execute(registration, bleededDate);
    }

    private class sendRequestInBackground extends AsyncTask<String, Void, String>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Sending Request...");
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
            String stringUrl = "http://hostellocator.com/sendPushNotification.php";
            String name, registration, contact, bloodType;
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
                bloodType = params[3];
                String data = URLEncoder.encode("name", "UTF-8") +"="+ URLEncoder.encode(name, "UTF-8") +"&"+
                              URLEncoder.encode("registration", "UTF-8") +"="+ URLEncoder.encode(registration, "UTF-8") +"&"+
                              URLEncoder.encode("contact", "UTF-8") +"="+ URLEncoder.encode(contact, "UTF-8") +"&"+
                              URLEncoder.encode("bloodType", "UTF-8") +"="+ URLEncoder.encode(bloodType, "UTF-8") +"&"+
                              URLEncoder.encode("designation", "UTF-8") +"="+ URLEncoder.encode("BLOOD_BANK", "UTF-8");
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
            if (s != null && s.equals("OK")) {
                Toast.makeText(context, "Request sent.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Some error occurred, please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class RetrieveDonors extends AsyncTask<String, Void, List<DonorsInfo>> {

        private ProgressDialog progressDialog;
        public RetrieveDonors( ) {

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Retrieving donors information...");
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
        protected List<DonorsInfo> doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/retrieveDonors.php";
            DonorsInfo[] donorsInfo;
            List<DonorsInfo> donorsInfoList = new ArrayList<>();
            String bloodType = params[0];
                try {
                    URL url = new URL(stringUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String data = URLEncoder.encode("bloodType", "UTF-8") + "=" + URLEncoder.encode(bloodType, "UTF-8");
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
                    JSONArray parentJson = new JSONArray(stringBuilder.toString());
                    donorsInfo = new DonorsInfo[parentJson.length()];
                    for (int index = 0; index < parentJson.length(); index++) {
                        JSONObject finalObject = parentJson.getJSONObject(index);
                        donorsInfo[index] = new DonorsInfo();
                        donorsInfo[index].setName(finalObject.getString("name"));
                        donorsInfo[index].setRegistration(finalObject.getString("reg_id"));
                        donorsInfo[index].setBloodType(finalObject.getString("blood_type"));
                        donorsInfo[index].setContact(finalObject.getString("contact"));
                        donorsInfoList.add(donorsInfo[index]);
                    }
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return donorsInfoList;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(List<DonorsInfo> s) {
            progressDialog.cancel();
            if (s != null) {
                DonorsViewAdapter donorsViewAdapter = new DonorsViewAdapter(s);
                ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(donorsViewAdapter);
                scaleInAnimationAdapter.setFirstOnly(false);
                recyclerView.setAdapter(scaleInAnimationAdapter);
            } else {
                Toast.makeText(context, "Couldn't retrieve donors.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AddDonor extends AsyncTask<String, Void, String>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Adding donor... Please wait");
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
            String stringUrl = "http://hostellocator.com/insertDonor.php";
            String name, regID, bloodType, contact, bleeded;
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                name = params[0];
                regID = params[1];
                bloodType = params[2];
                contact = params[3];
                bleeded = params[4];
                String data = URLEncoder.encode("name", "UTF-8") +"="+ URLEncoder.encode(name, "UTF-8") +"&"+
                        URLEncoder.encode("regID", "UTF-8") +"="+ URLEncoder.encode(regID, "UTF-8") +"&"+
                        URLEncoder.encode("bloodType", "UTF-8") +"="+ URLEncoder.encode(bloodType, "UTF-8") +"&"+
                        URLEncoder.encode("contact", "UTF-8") +"="+ URLEncoder.encode(contact, "UTF-8") +"&"+
                        URLEncoder.encode("bleeded", "UTF-8") +"="+ URLEncoder.encode(bleeded, "UTF-8");
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
                    case "INSERTED": {
                        Toast.makeText(context, "Donor is successfully added!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case "EXISTED": {
                        builder.setMessage("Couldn't add donor because donor already exists!");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                    case "ERROR": {
                        Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            } else {
                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateBleedingDate extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Updating donor's information... Please wait!");
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://hostellocator.com/updateBleedingDate.php";
            String regID, bleedingDate;
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                regID = params[0];
                bleedingDate = params[1];
                String data = URLEncoder.encode("regID", "UTF-8") +"="+ URLEncoder.encode(regID, "UTF-8") +"&"+
                        URLEncoder.encode("bleedingDate", "UTF-8") +"="+ URLEncoder.encode(bleedingDate, "UTF-8");
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
            if (result != null) {
                switch (result) {
                    case "UPDATED": {
                        Toast.makeText(context, "Bleeding date updated successfully!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case "ERROR": {
                        builder.setMessage("Some error occurred! Please try again.");
                        alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                    default: {
                        Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }else {
                Toast.makeText(context, "Some error occurred! Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
