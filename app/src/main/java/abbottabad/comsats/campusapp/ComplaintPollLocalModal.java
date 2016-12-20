package abbottabad.comsats.campusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
class ComplaintPollLocalModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "COMPLAINTPOLL.db";
    private static String TABLE_NAME = "COMPLAINTS";
    private static String SERIAL = "SERIAL_NO";
    private static String COL_NAME = "NAME";
    private static String COL_REG = "REG_NO";
    private static String COL_CONTACT = "CONTACT";
    private static String COL_DESCRIPTION = "DESCRIPTION";
    private static String COL_IMAGE_URL = "IMAGE_URL";

    ComplaintPollLocalModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " +TABLE_NAME+ " ( "
                +SERIAL+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_NAME+ " TEXT,"
                +COL_REG+ " TEXT,"
                +COL_CONTACT+ " TEXT,"
                +COL_DESCRIPTION+ " TEXT,"
                +COL_IMAGE_URL+ " TEXT"
                + " )";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    void addComplaint(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, ComplaintPollController.getName());
        contentValues.put(COL_REG, ComplaintPollController.getRegistration());
        contentValues.put(COL_CONTACT, ComplaintPollController.getContact());
        contentValues.put(COL_DESCRIPTION, ComplaintPollController.getDescription());
        contentValues.put(COL_IMAGE_URL, ComplaintPollController.getImageUrl());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    void retrieveComplaints(){
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
            complaintsInfos[cursor.getPosition()].setImageUrl(cursor.getString(5));
            ComplaintPollView.complaintPollVIewAdapter.add(complaintsInfos[cursor.getPosition()], cursor.getPosition());
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    void deleteComplaint(String imageName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_IMAGE_URL + " = ?", new String[]{imageName});
        db.close();
        retrieveComplaints();
    }
}
