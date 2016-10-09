package abbottabad.comsats.campusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/21/16.
 */
class NotificationsLocalModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "TRACKFACULTY.db";
    private static String TABLE_NAME = "SENT_NOTIFICATIONS";
    private static String COL_NOTIFICATION = "NOTIFICATIONS";
    private static String COL_DATE_TIME = "DATE_NAME";
    private static String COL_IS_MINE = "IS_MINE";


    NotificationsLocalModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SERIAL = "SERIAL_NO";
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " +TABLE_NAME+ " ( "
                + SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_NOTIFICATION+ " TEXT,"
                +COL_DATE_TIME+ " TEXT,"
                +COL_IS_MINE+ " INTEGER"
                + " )";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    void addEventNotification(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTIFICATION, NotificationsController.getNotification());
        values.put(COL_DATE_TIME, NotificationsController.getDateTime());
        values.put(COL_IS_MINE, NotificationsController.getMine());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void deleteNotifications(String[] notifications){
        SQLiteDatabase db = NotificationsLocalModal.this.getWritableDatabase();
        for (String notification : notifications) {
            db.delete(TABLE_NAME, COL_NOTIFICATION + " = ?", new String[]{notification});
        }
    }

    void deleteAll(){
        SQLiteDatabase db = NotificationsLocalModal.this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    void retrieveNotifications(){

        SQLiteDatabase db = NotificationsLocalModal.this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        List<NotificationInfo> notificationInfoList = new ArrayList<>();
        NotificationInfo[] notificationInfos = new NotificationInfo[cursor.getCount()];
        while (!cursor.isAfterLast()){
            notificationInfos[cursor.getPosition()] = new NotificationInfo();
            notificationInfos[cursor.getPosition()].setNotification(cursor.getString(1));
            notificationInfos[cursor.getPosition()].setDateTime(cursor.getString(2));
            notificationInfos[cursor.getPosition()].setMine(cursor.getInt(3));
            notificationInfoList.add(notificationInfos[cursor.getPosition()]);
            cursor.moveToNext();
        }
        if (NotificationsView.notificationsAdapter != null && NotificationsView.listView != null){
            for (int i = 0; i < notificationInfoList.size(); i++) {
                NotificationsView.notificationsAdapter.add(notificationInfoList.get(i));
            }
            NotificationsView.listView.setSelection(NotificationsView.notificationsAdapter.getCount());
        }

        cursor.close();
        db.close();
    }

}
