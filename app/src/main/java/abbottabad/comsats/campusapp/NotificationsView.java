package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
public class NotificationsView extends AppCompatActivity implements View.OnLongClickListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public static ListView listView;
    public static ImageButton btnSend;
    public static EditText ET_message;
    public static ProgressBar progressBar;
    public static EventNotificationsAdapter eventNotificationsAdapter;
    public static boolean IS_IN_ACTION_MODE = false;
    private String REG_ID;
    private Toolbar toolbar;
    private int counter = 0;
    private ArrayList<Integer> selectedItems;
    private ArrayList<EventNotificationInfo> eventNotificationInfos;
    private TextView notificationTitle;
    private EventNotificationsLocalModal eventNotificationsLocalModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);
        toolbar = (Toolbar) findViewById(R.id.notifications_toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        REG_ID = sharedPreferences.getString("REG_ID", null);
        eventNotificationsLocalModal = new EventNotificationsLocalModal(this);
        IS_IN_ACTION_MODE = false;

        listView = (ListView) findViewById(R.id.LV_notifications);
        btnSend = (ImageButton) findViewById(R.id.sendNotification);
        ET_message = (EditText) findViewById(R.id.ET_notification);
        notificationTitle = (TextView) findViewById(R.id.notificationTitle);

        progressBar = (ProgressBar) findViewById(R.id.waiting);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        eventNotificationsAdapter = new EventNotificationsAdapter(NotificationsView.this, R.layout.chat_right);
        listView.setAdapter(eventNotificationsAdapter);
        listView.setClickable(false);
        eventNotificationsLocalModal.retrieveNotifications();
        selectedItems = new ArrayList<>();
        eventNotificationInfos = new ArrayList<>();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete: {
                if (selectedItems.size() > 0) {
                    for (int i = 0; i < selectedItems.size(); i++) {
                        eventNotificationInfos.add(eventNotificationsAdapter.getItem(selectedItems.get(i)));
                    }

                    for (int i = 0; i < selectedItems.size(); i++) {
                        eventNotificationsAdapter.remove(eventNotificationInfos.get(i));
                    }

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    notificationTitle.setText(R.string.notifications);
                    IS_IN_ACTION_MODE = false;
                    eventNotificationsAdapter.notifyDataSetChanged();
                    notificationTitle.setText("0 item selected");
                    selectedItems.clear();
                    counter = 0;

                    String[] notifications = new String[eventNotificationInfos.size()];
                    for (int i = 0; i < eventNotificationInfos.size(); i++){
                        notifications[i] = eventNotificationInfos.get(i).getNotification();
                    }
                    eventNotificationsLocalModal.deleteNotifications(notifications);
                    eventNotificationInfos.clear();
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "onOptionsItemSelected: "+ Arrays.toString(notifications));
                } else {
                    Toast.makeText(this, "No item is selected", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        notificationTitle.setText("0 item selected");
        toolbar.inflateMenu(R.menu.notifications_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IS_IN_ACTION_MODE = true;
        eventNotificationsAdapter.notifyDataSetChanged();
        return true;
    }

    public void updateSelection(View v, int position) {
        if (((CheckBox)v).isChecked()){
            Log.i("TAG", "updateSelection: "+ position);
            counter += 1;
            updateCounter(counter);
            selectedItems.add(position);
        } else {
            counter -= 1;
            updateCounter(counter);
            if (selectedItems.size() > 0) {
                for (int i = 0; i < selectedItems.size(); i++){
                    if (selectedItems.get(i) == position) {
                        selectedItems.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void updateCounter(int counter) {
        if (counter == 0){
            notificationTitle.setText("0 item selected");
        } else {
            notificationTitle.setText(counter + " items selected");
        }
    }
}
