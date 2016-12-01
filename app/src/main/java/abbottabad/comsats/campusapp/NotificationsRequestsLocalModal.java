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
 * This project CampusApp is created by Kamran Ramzan on 27-Nov-16.
 */

class NotificationsRequestsLocalModal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "NOTIFICATIONS_REQUESTS.db";
    private static String TABLE_NAME = "NOTIFICATIONS_REQUESTS";
    private static String COL_NAME = "NAME";
    private static String COL_REG_ID = "REG_ID";
    private static String COL_GROUP_NAME = "GROUP_NAME";
    private static String COL_DATE_TIME = "DATE_NAME";
    private SharedPreferences sharedPreferences;
    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private Context context;


    NotificationsRequestsLocalModal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SERIAL = "SERIAL_NO";
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT,"
                + COL_REG_ID + " TEXT,"
                + COL_GROUP_NAME + " TEXT,"
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

    void addGroupRequest() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, NotificationsRequestController.getName());
        values.put(COL_REG_ID, NotificationsRequestController.getRegId());
        values.put(COL_GROUP_NAME, NotificationsRequestController.getGroupName());
        values.put(COL_DATE_TIME, NotificationsRequestController.getTimeStamp());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void deleteGroupRequest(String regId, String groupName) {
        SQLiteDatabase db = NotificationsRequestsLocalModal.this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_REG_ID + " = ? AND " + COL_GROUP_NAME + " = ?", new String[]{regId, groupName});
        db.close();
    }

    void retrieveGroupRequest(String notification_type) {

        SQLiteDatabase db = NotificationsRequestsLocalModal.this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_GROUP_NAME + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{notification_type});
        cursor.moveToFirst();
        List<PendingGroupRequestsInfo> pendingGroupRequestsInfos = new ArrayList<>();
        PendingGroupRequestsInfo[] pendingGroupRequests = new PendingGroupRequestsInfo[cursor.getCount()];
        while (!cursor.isAfterLast()) {
            pendingGroupRequests[cursor.getPosition()] = new PendingGroupRequestsInfo();
            pendingGroupRequests[cursor.getPosition()].setName(cursor.getString(1));
            pendingGroupRequests[cursor.getPosition()].setRegId(cursor.getString(2));
            pendingGroupRequests[cursor.getPosition()].setTimeStamp(cursor.getString(3));
            pendingGroupRequestsInfos.add(pendingGroupRequests[cursor.getPosition()]);
            cursor.moveToNext();
        }
        if (NotificationsView.pendingGroupRequestsAdapter != null && NotificationsView.recyclerView != null) {
            for (int i = 0; i < pendingGroupRequestsInfos.size(); i++) {
                NotificationsView.pendingGroupRequestsAdapter.addItem(pendingGroupRequestsInfos.get(i), 0);
            }
        }

        cursor.close();
        db.close();
    }
}

