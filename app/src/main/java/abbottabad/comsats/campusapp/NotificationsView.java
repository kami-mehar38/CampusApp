package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    public static boolean IS_WAITING = true;
    private String REG_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);
        setActionBarLogo();
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
        eventNotificationsAdapter = new EventNotificationsAdapter(this, R.layout.chat_right);
        listView.setAdapter(eventNotificationsAdapter);
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

        eventNotificationsAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

            }
        });
    }
    public void setActionBarLogo() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notifications");
    }
}
