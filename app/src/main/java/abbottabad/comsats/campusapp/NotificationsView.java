package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
public class NotificationsView extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public static ListView listView;
    public static ImageButton btnSend;
    public static EditText ET_message;
    public static ProgressBar progressBar;
    public static EventNotificationsAdapter eventNotificationsAdapter;
    public static boolean IS_IN_ACTION_MODE = false;
    private String REG_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.notifications_toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        REG_ID = sharedPreferences.getString("REG_ID", null);
        EventNotificationsLocalModal eventNotificationsLocalModal = new EventNotificationsLocalModal(this);

        listView = (ListView) findViewById(R.id.LV_notifications);
        btnSend = (ImageButton) findViewById(R.id.sendNotification);
        ET_message = (EditText) findViewById(R.id.ET_notification);
        progressBar = (ProgressBar) findViewById(R.id.waiting);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        eventNotificationsAdapter = new EventNotificationsAdapter(NotificationsView.this, R.layout.chat_right);
        listView.setAdapter(eventNotificationsAdapter);
        listView.setClickable(false);
        eventNotificationsLocalModal.retrieveNotifications();

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ET_message.getText().toString().trim().equals("")) {
                    Toast.makeText(NotificationsView.this, "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {

                    new EventNotificationModal(NotificationsView.this).
                            sendEventNotification(REG_ID, ET_message.getText().toString().trim());

                }
            }
        });
    }

}
