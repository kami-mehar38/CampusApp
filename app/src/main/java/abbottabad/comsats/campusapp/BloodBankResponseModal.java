package abbottabad.comsats.campusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This project CampusApp is created by Kamran Ramzan on 03-Oct-16.
 */

class BloodBankResponseModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "BLOODBANK_RESPONSE.db";
    private static String SERIAL = "SERIAL_NO";
    private static String TABLE_NAME = "BLOOD_REQUESTS_RESPONSE";
    private static String COL_NAME = "NAME";
    private static String COL_REG = "REG_NO";
    private static String COL_CONTACT = "CONTACT";
    private static String COL_BLOOD_TYPE = "BLLOD_TYPE";
    private static String COL_DISTANCE = "DISTANCE";
    private static String COL_ACCEPTED = "ACCEPTED";
    private static String COL_REJECTED = "REJECTED";

    BloodBankResponseModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " +TABLE_NAME+ " ( "
                +SERIAL+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_NAME+ " TEXT,"
                +COL_REG+ " TEXT,"
                +COL_CONTACT+ " TEXT,"
                +COL_BLOOD_TYPE+ " TEXT,"
                +COL_DISTANCE+ " INTEGER,"
                +COL_ACCEPTED+ " INTEGER,"
                +COL_REJECTED+ " INTEGER"
                + " )";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
    void addBloodRequestResponse(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, BloodBankResponseController.getName());
        contentValues.put(COL_REG, BloodBankResponseController.getRegistration());
        contentValues.put(COL_CONTACT, BloodBankResponseController.getContact());
        contentValues.put(COL_BLOOD_TYPE, BloodBankResponseController.getBloodGroup());
        contentValues.put(COL_DISTANCE, BloodBankResponseController.getDistance());
        contentValues.put(COL_ACCEPTED, BloodBankResponseController.getIsAccepted());
        contentValues.put(COL_REJECTED, BloodBankResponseController.getIsRejected());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
    void viewBloodRequestResponses(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME+ " ORDER BY " +COL_DISTANCE+ " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        ResponseInfo[] responseInfos = new ResponseInfo[cursor.getCount()];
        while (!cursor.isAfterLast()){
            responseInfos[cursor.getPosition()] = new ResponseInfo();
            responseInfos[cursor.getPosition()].setName(cursor.getString(1));
            responseInfos[cursor.getPosition()].setRegistration(cursor.getString(2));
            responseInfos[cursor.getPosition()].setBloodType(cursor.getString(4));
            responseInfos[cursor.getPosition()].setContact(cursor.getString(3));
            responseInfos[cursor.getPosition()].setDistance(cursor.getInt(5));
            responseInfos[cursor.getPosition()].setIsAccepted(cursor.getInt(6));
            responseInfos[cursor.getPosition()].setIsRejected(cursor.getInt(7));
            BloodRequestResponseFragment.responseViewAdapter.add(responseInfos[cursor.getPosition()], cursor.getPosition());
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    void deleteResponse(String reg){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_REG + " = ?", new String[]{reg});
        db.close();
    }

    void setIsAccepted(String reg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ACCEPTED, 1);
        db.update(TABLE_NAME, values, COL_REG + " = ?", new String[]{reg});
        db.close();
    }

    int getIsAccepted(String reg_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery= "SELECT " +COL_ACCEPTED+ " FROM " +TABLE_NAME+ " WHERE " +COL_REG+ "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{reg_id.trim()});
        cursor.moveToFirst();
        int isDonated = 0;
        while (!cursor.isAfterLast()){
            isDonated = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return isDonated;
    }

    void setIsRejected(String reg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_REJECTED, 1);
        db.update(TABLE_NAME, values, COL_REG + " = ?", new String[]{reg});
        db.close();
    }

    int getIsRejected(String reg_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery= "SELECT " +COL_REJECTED+ " FROM " +TABLE_NAME+ " WHERE " +COL_REG+ "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{reg_id.trim()});
        cursor.moveToFirst();
        int isDonated = 0;
        while (!cursor.isAfterLast()){
            isDonated = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return isDonated;
    }
}
