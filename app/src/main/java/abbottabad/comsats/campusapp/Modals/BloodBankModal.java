package abbottabad.comsats.campusapp.Modals;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

import abbottabad.comsats.campusapp.Controllers.BloodBankController;
import abbottabad.comsats.campusapp.Helper_Classes.DonorsInfo;
import abbottabad.comsats.campusapp.Helper_Classes.RequestsInfo;
import abbottabad.comsats.campusapp.Controllers.RequestsViewAdapter;
import abbottabad.comsats.campusapp.Views.BloodDonorsFragment;

import static abbottabad.comsats.campusapp.Views.BloodDonorsFragment.*;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodBankModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "BLOODBANK.db";
    private static String SERIAL = "SERIAL_NO";
    private static String TABLE_NAME = "BLOOD_REQUESTS";
    private static String COL_NAME = "NAME";
    private static String COL_REG = "REG_NO";
    private static String COL_CONTACT = "CONTACT";
    private static String COL_BLOOD_TYPE = "BLLOD_TYPE";
    private Context context;
    private AlertDialog alertDialog;

    public BloodBankModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " +TABLE_NAME+ " ( "
                +SERIAL+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_NAME+ " TEXT,"
                +COL_REG+ " TEXT,"
                +COL_CONTACT+ " TEXT,"
                +COL_BLOOD_TYPE+ " TEXT"
                + " )";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
    public void addBloodRequest(BloodBankController bloodBankController){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, bloodBankController.getStdName());
        contentValues.put(COL_REG, bloodBankController.getStdReg());
        contentValues.put(COL_CONTACT, bloodBankController.getStdContact());
        contentValues.put(COL_BLOOD_TYPE, bloodBankController.getBloodType());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
    public void viewBloodRequests(RecyclerView RV_bloodRequests){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME+ " ORDER BY " +SERIAL+ " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        List<RequestsInfo> requestsInfoList = new ArrayList<>();
        RequestsInfo[] requestsInfos = new RequestsInfo[cursor.getCount()];
        while (!cursor.isAfterLast()){
            requestsInfos[cursor.getPosition()] = new RequestsInfo();
            requestsInfos[cursor.getPosition()].setName(cursor.getString(1));
            requestsInfos[cursor.getPosition()].setRegistration(cursor.getString(2));
            requestsInfos[cursor.getPosition()].setBloodType(cursor.getString(4));
            requestsInfos[cursor.getPosition()].setContact(cursor.getString(3));
            requestsInfoList.add(requestsInfos[cursor.getPosition()]);
            cursor.moveToNext();
        }
        cursor.close();
        RV_bloodRequests.setAdapter(new RequestsViewAdapter(requestsInfoList));
    }

    public void sendRequest(String name, String registration, String contact, String bloodType){
        new sendRequestInBackground().execute(name, registration, contact, bloodType);
    }

    public void retrieveDonors(RecyclerView recyclerView, String bloodType) {
        new RetrieveDonors(recyclerView).execute(bloodType);
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
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://10.0.2.2/CampusApp/sendPushNotification.php";
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
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }

                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            progressDialog.cancel();
        }
    }
    public class RetrieveDonors extends AsyncTask<String, Void, List<DonorsInfo>> {

        private ProgressDialog progressDialog;
        private RecyclerView recyclerView;
        public RetrieveDonors(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Retrieving donors information...");
            progressDialog.show();
        }

        @Override
        protected List<DonorsInfo> doInBackground(String... params) {
            String stringUrl = "http://10.0.2.2/CampusApp/retrieveDonors.php";
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

                String data = URLEncoder.encode("bloodType", "UTF-8") +"="+ URLEncoder.encode(bloodType, "UTF-8");
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
            recyclerView.setAdapter(new DonorsViewAdapter(s));
        }
    }
    public class AddDonor extends AsyncTask<String, Void, String>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Adding donor... Please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://10.0.2.2/CampusApp/insertDonor.php";
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
                    builder.setMessage("Some error occurred! Please try again.");
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                }
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
            String stringUrl = "http://10.0.2.2/CampusApp/updateBleedingDate.php";
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
            }
        }
    }
}
