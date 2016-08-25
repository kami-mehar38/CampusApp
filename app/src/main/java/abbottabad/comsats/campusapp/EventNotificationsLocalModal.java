package abbottabad.comsats.campusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/21/16.
 */
public class EventNotificationsLocalModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "TRACKFACULTY.db";
    private static String TABLE_NAME = "SENT_NOTIFICATIONS";
    private static String COL_NOTIFICATION = "NOTIFICATIONS";
    private static String COL_DATE_TIME = "DATE_NAME";
    private static String COL_IS_MINE = "IS_MINE";


    public EventNotificationsLocalModal(Context context) {
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

    public void addEventNotification(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTIFICATION, NotificationsController.getNotification());
        values.put(COL_DATE_TIME, NotificationsController.getDateTime());
        values.put(COL_IS_MINE, NotificationsController.getMine());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteNotifications(String[] notifications){
        SQLiteDatabase db = EventNotificationsLocalModal.this.getWritableDatabase();
        for (String notification : notifications) {
            db.delete(TABLE_NAME, COL_NOTIFICATION + " = ?", new String[]{notification});
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = EventNotificationsLocalModal.this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public void retrieveNotifications(){

        SQLiteDatabase db = EventNotificationsLocalModal.this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        List<EventNotificationInfo> eventNotificationInfoList = new ArrayList<>();
        EventNotificationInfo[] eventNotificationInfos = new EventNotificationInfo[cursor.getCount()];
        while (!cursor.isAfterLast()){
            eventNotificationInfos[cursor.getPosition()] = new EventNotificationInfo();
            eventNotificationInfos[cursor.getPosition()].setNotification(cursor.getString(1));
            eventNotificationInfos[cursor.getPosition()].setDateTime(cursor.getString(2));
            eventNotificationInfos[cursor.getPosition()].setMine(cursor.getInt(3));
            eventNotificationInfoList.add(eventNotificationInfos[cursor.getPosition()]);
            cursor.moveToNext();
        }
        if (NotificationsView.eventNotificationsAdapter != null && NotificationsView.listView != null){
            for (int i = 0; i < eventNotificationInfoList.size(); i++) {
                NotificationsView.eventNotificationsAdapter.add(eventNotificationInfoList.get(i));
            }
            NotificationsView.listView.setSelection(NotificationsView.eventNotificationsAdapter.getCount());
        }

        cursor.close();
        db.close();
    }

}
