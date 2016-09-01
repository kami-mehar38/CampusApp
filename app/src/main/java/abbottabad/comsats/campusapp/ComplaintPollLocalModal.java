package abbottabad.comsats.campusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
public class ComplaintPollLocalModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "COMPLAINTPOLL.db";
    private static String TABLE_NAME = "COMPLAINTS";
    private static String SERIAL = "SERIAL_NO";
    private static String COL_NAME = "NAME";
    private static String COL_REG = "REG_NO";
    private static String COL_CONTACT = "CONTACT";
    private static String COL_DESCRIPTION = "DESCRIPTION";

    public ComplaintPollLocalModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " +TABLE_NAME+ " ( "
                +SERIAL+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_NAME+ " TEXT,"
                +COL_REG+ " TEXT,"
                +COL_CONTACT+ " TEXT,"
                +COL_DESCRIPTION+ " TEXT"
                + " )";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public void addComplaint(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, ComplaintPollController.getName());
        contentValues.put(COL_REG, ComplaintPollController.getRegistration());
        contentValues.put(COL_CONTACT, ComplaintPollController.getContact());
        contentValues.put(COL_DESCRIPTION, ComplaintPollController.getDescription());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public void retrieveComplaints(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME+ " ORDER BY " +SERIAL+ " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        ComplaintsInfo[] complaintsInfos = new ComplaintsInfo[cursor.getCount()];
        while (!cursor.isAfterLast()){
            complaintsInfos[cursor.getPosition()] = new ComplaintsInfo();
            complaintsInfos[cursor.getPosition()].setName(cursor.getString(1));
            complaintsInfos[cursor.getPosition()].setRegistration(cursor.getString(2));
            complaintsInfos[cursor.getPosition()].setContact(cursor.getString(3));
            complaintsInfos[cursor.getPosition()].setDescription(cursor.getString(4));
            ComplaintPollView.complaintPollVIewAdapter.add(complaintsInfos[cursor.getPosition()], cursor.getPosition());
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    public void deleteComplaint(String reg){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_REG + " = ?", new String[]{reg});
        db.close();
        retrieveComplaints();
    }
}
