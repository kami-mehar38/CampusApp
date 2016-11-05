package abbottabad.comsats.campusapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
    private static String COL_NOTIFICATION_SENDER = "NOTIFICATIONS_SENDER";
    private static String COL_DATE_TIME = "DATE_NAME";
    private static String COL_IS_MINE = "IS_MINE";
    private static String COL_NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    private SharedPreferences sharedPreferences;
    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private Context context;


    NotificationsLocalModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SERIAL = "SERIAL_NO";
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " +TABLE_NAME+ " ( "
                + SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_NOTIFICATION+ " TEXT,"
                +COL_NOTIFICATION_SENDER+ " TEXT,"
                +COL_DATE_TIME+ " TEXT,"
                +COL_IS_MINE+ " INTEGER,"
                +COL_NOTIFICATION_TYPE+ " TEXT"
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
        values.put(COL_NOTIFICATION_SENDER, NotificationsController.getNotificationSender());
        values.put(COL_DATE_TIME, NotificationsController.getDateTime());
        values.put(COL_IS_MINE, NotificationsController.getMine());
        values.put(COL_NOTIFICATION_TYPE, NotificationsController.getNotificationType());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void deleteNotifications(String[] notifications, String notification_type){
        SQLiteDatabase db = NotificationsLocalModal.this.getWritableDatabase();
        for (String notification : notifications) {
            db.delete(TABLE_NAME, COL_NOTIFICATION + " = ? AND " + COL_NOTIFICATION_TYPE + " = ?", new String[]{notification, notification_type});
        }

        String selectQuery= "SELECT * FROM " +TABLE_NAME+ " WHERE " +COL_NOTIFICATION_TYPE+ " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{notification_type});
        if (cursor != null) {
            cursor.moveToLast();
            String message = cursor.getString(1);
            String timeStamp = cursor.getString(3);
            int mine = cursor.getInt(4);
            sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (mine == 1){
                editor.putString(notification_type + "_RECENT_MESSAGE", "You: " + message);
            } else editor.putString(notification_type + "_RECENT_MESSAGE", message);
            editor.putString(notification_type + "_RECENT_MESSAGE_TIME", timeStamp);
            editor.apply();
            cursor.close();
        }
        db.close();
    }

    void deleteAll(String notification_type){
        sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(notification_type + "_RECENT_MESSAGE", "No recent message");
        editor.putString(notification_type + "_RECENT_MESSAGE_TIME", null);
        editor.apply();
        SQLiteDatabase db = NotificationsLocalModal.this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_NOTIFICATION_TYPE + " = ? ", new String[]{notification_type});
        db.close();
    }

    void retrieveNotifications(String notification_type){

        SQLiteDatabase db = NotificationsLocalModal.this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME+ " WHERE " +COL_NOTIFICATION_TYPE+ " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{notification_type});
        cursor.moveToFirst();
        List<NotificationInfo> notificationInfoList = new ArrayList<>();
        NotificationInfo[] notificationInfos = new NotificationInfo[cursor.getCount()];
        while (!cursor.isAfterLast()){
            notificationInfos[cursor.getPosition()] = new NotificationInfo();
            notificationInfos[cursor.getPosition()].setNotification(cursor.getString(1));
            notificationInfos[cursor.getPosition()].setNotificationSender(cursor.getString(2));
            notificationInfos[cursor.getPosition()].setDateTime(cursor.getString(3));
            notificationInfos[cursor.getPosition()].setMine(cursor.getInt(4));
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
