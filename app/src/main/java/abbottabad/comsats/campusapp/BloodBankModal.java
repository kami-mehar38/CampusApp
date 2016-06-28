package abbottabad.comsats.campusapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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
    public ArrayList<String> viewBloodRequests(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME+ " ORDER BY " +SERIAL+ " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String reqList = "";
        ArrayList<String> arrayList = new ArrayList<String>();
        while (!cursor.isAfterLast()){
            reqList = reqList + "   " + cursor.getString(1);
            reqList = reqList + "   " + cursor.getString(2);
            arrayList.add(reqList);
            reqList = "";
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }
    public void viewSelectedRequest(String searchREG){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_NAME, COL_REG, COL_CONTACT, COL_BLOOD_TYPE};
        String where = COL_REG + "=?";
        Cursor cursor = db.query(TABLE_NAME, columns, where, new String[]{searchREG}, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> arrayList = new ArrayList<>();

        while (!cursor.isAfterLast()){
            arrayList.add(cursor.getString(0));
            arrayList.add(cursor.getString(1));
            arrayList.add(cursor.getString(2));
            arrayList.add(cursor.getString(3));
            cursor.moveToNext();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Student details");
        builder.setMessage("Name:   " +arrayList.get(0)+ "\n" +
                           "Reg ID:   " +arrayList.get(1)+ "\n" +
                           "Contact#:   " +arrayList.get(2)+ "\n" +
                           "Blood required:   " +arrayList.get(3)+ "\n");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        cursor.close();
    }
    public void sendRequest(String name, String registration, String contact, String bloodType){
        new sendRequestInBackground().execute(name, registration, contact, bloodType);
    }

    public void retrieveDonors() {
        new RetrieveDonors().execute();
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
    public class RetrieveDonors extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Retrieving donors information...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "http://10.0.2.2/CampusApp/retrieveDonors.php";

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
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
            progressDialog.cancel();
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        }
    }

}
