package abbottabad.comsats.campusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 03-Jan-17.
 */

class EventsLocalModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "EVENTS.db";
    private static String TABLE_NAME = "EVENTS_NOTIFICATIONS";
    private static String COL_NOTIFICATION = "NOTIFICATIONS";
    private static String COL_NAME = "NOTIFICATIONS_SENDER";
    private static String COL_DATE_TIME = "DATE_NAME";
    private Context context;

    EventsLocalModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SERIAL = "SERIAL_NO";
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NOTIFICATION + " TEXT,"
                + COL_NAME + " TEXT,"
                + COL_DATE_TIME + " TEXT"
                + " )";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    void addEventNotification() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTIFICATION, EventsInfoController.getNotification());
        values.put(COL_NAME, EventsInfoController.getName());
        values.put(COL_DATE_TIME, EventsInfoController.getTimeDate());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void deleteNotifications(String timeDate) {
        SQLiteDatabase db = EventsLocalModal.this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_DATE_TIME + " = ?", new String[]{timeDate});
        db.close();
    }

    void retrieveNotifications() {

        SQLiteDatabase db = EventsLocalModal.this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        List<EventNotificationsInfo> eventNotificationsInfos = new ArrayList<>();
        EventNotificationsInfo[] notificationInfos = new EventNotificationsInfo[cursor.getCount()];
        while (!cursor.isAfterLast()) {
            notificationInfos[cursor.getPosition()] = new EventNotificationsInfo();
            notificationInfos[cursor.getPosition()].setNotification(cursor.getString(1));
            notificationInfos[cursor.getPosition()].setName(cursor.getString(2));
            notificationInfos[cursor.getPosition()].setTimeStamp(cursor.getString(3));
            eventNotificationsInfos.add(notificationInfos[cursor.getPosition()]);
            cursor.moveToNext();
        }
        if (NotificationsListFragment.eventNotificationsAdapter != null) {
            for (int i = 0; i < eventNotificationsInfos.size(); i++) {
                NotificationsListFragment.eventNotificationsAdapter.addItem(eventNotificationsInfos.get(i), 0);
            }
            NotificationsListFragment.RV_notifications.smoothScrollToPosition(0);
        }
        cursor.close();
        db.close();
    }
}

