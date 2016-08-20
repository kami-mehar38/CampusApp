package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
public class NotificationsView extends AppCompatActivity {
    private ListView listView;
    private ImageButton btnSend;
    private EditText editText;
    boolean isMine = true;
    private List<EventNotificationInfo> eventNotificationInfos;
    private ArrayAdapter<EventNotificationInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);
        setActionBarLogo();
        eventNotificationInfos = new ArrayList<>();

        listView = (ListView) findViewById(R.id.LV_notifications);
        btnSend = (ImageButton) findViewById(R.id.sendNotification);
        editText = (EditText) findViewById(R.id.ET_notification);

        //set ListView adapter first
        adapter = new EventNotificationsAdapter(this, R.layout.chat_left, eventNotificationInfos);
        listView.setAdapter(adapter);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(NotificationsView.this, "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    EventNotificationInfo eventNotificationInfo = new EventNotificationInfo(editText.getText().toString(), isMine);
                    eventNotificationInfos.add(eventNotificationInfo);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                    isMine = !isMine;
                }
            }
        });
    }
    public void setActionBarLogo() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notifications");
    }
}
