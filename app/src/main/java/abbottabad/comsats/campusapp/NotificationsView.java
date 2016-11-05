package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    public static NotificationsAdapter notificationsAdapter;
    public static boolean IS_IN_ACTION_MODE = false;
    private String REG_ID;
    private Toolbar toolbar;
    private int counter = 0;
    private ArrayList<Integer> selectedItems;
    private ArrayList<NotificationInfo> notificationInfos;
    private TextView notificationTitle;
    private NotificationsLocalModal notificationsLocalModal;
    private CheckBox CB_selectAll;
    public static boolean IS_IN_SELECT_ALL_MODE = false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);
        toolbar = (Toolbar) findViewById(R.id.notifications_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        REG_ID = sharedPreferences.getString("REG_ID", null);
        notificationsLocalModal = new NotificationsLocalModal(this);
        IS_IN_ACTION_MODE = false;
        IS_IN_SELECT_ALL_MODE = false;

        String notificationType = sharedPreferences.getString("NOTIFICATION_TYPE", null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(notificationType + "_COUNT", 0);
        editor.apply();

        listView = (ListView) findViewById(R.id.LV_notifications);
        btnSend = (ImageButton) findViewById(R.id.sendNotification);
        ET_message = (EditText) findViewById(R.id.ET_notification);
        notificationTitle = (TextView) findViewById(R.id.notificationTitle);
        notificationTitle.setText(sharedPreferences.getString("NOTIFICATION_TYPE", ""));
        CB_selectAll = (CheckBox) findViewById(R.id.CB_selectAll);
        if (CB_selectAll != null) {
            CB_selectAll.setVisibility(View.GONE);
        }

        progressBar = (ProgressBar) findViewById(R.id.waiting);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        notificationsAdapter = new NotificationsAdapter(NotificationsView.this, R.layout.chat_right);
        listView.setAdapter(notificationsAdapter);
        notificationsLocalModal.retrieveNotifications(sharedPreferences.getString("NOTIFICATION_TYPE", null));
        selectedItems = new ArrayList<>();
        notificationInfos = new ArrayList<>();
        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ET_message.getText().toString().trim().equals("")) {
                    Toast.makeText(NotificationsView.this, "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    new NotificationsModal(NotificationsView.this).
                            sendEventNotification(REG_ID, ET_message.getText().toString().trim(),
                                    sharedPreferences.getString("NAME", null),
                                    sharedPreferences.getString("NOTIFICATION_TYPE", null));
                }
            }
        });

        CB_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    IS_IN_SELECT_ALL_MODE = true;
                    notificationsAdapter.notifyDataSetChanged();
                    notificationTitle.setText(notificationsAdapter.getCount() + " items selected");
                } else {
                    IS_IN_SELECT_ALL_MODE = false;
                    notificationsAdapter.notifyDataSetChanged();
                    notificationTitle.setText("0 item selected");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                if (!CB_selectAll.isChecked()) {
                    if (selectedItems.size() > 0) {
                        for (int i = 0; i < selectedItems.size(); i++) {
                            notificationInfos.add(notificationsAdapter.getItem(selectedItems.get(i)));
                        }

                        for (int i = 0; i < selectedItems.size(); i++) {
                            notificationsAdapter.remove(notificationInfos.get(i));
                        }

                        clearActionMode();

                        String[] notifications = new String[notificationInfos.size()];
                        for (int i = 0; i < notificationInfos.size(); i++) {
                            notifications[i] = notificationInfos.get(i).getNotification();
                        }
                        notificationsLocalModal.deleteNotifications(notifications, sharedPreferences.getString("NOTIFICATION_TYPE", null));
                        notificationInfos.clear();
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "onOptionsItemSelected: " + Arrays.toString(notifications));
                    } else {
                        Toast.makeText(this, "No item is selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    notificationsLocalModal.deleteAll(sharedPreferences.getString("NOTIFICATION_TYPE", null));
                    notificationsAdapter.clear();
                    notificationInfos.clear();
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    clearActionMode();
                }
                break;
            }

            case android.R.id.home: {
                clearActionMode();
                break;
            }
        }
        return true;
    }

    private void clearActionMode() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        notificationTitle.setText(sharedPreferences.getString("NOTIFICATION_TYPE", ""));
        toolbar.getMenu().clear();
        CB_selectAll.setChecked(false);
        CB_selectAll.setVisibility(View.GONE);
        IS_IN_ACTION_MODE = false;
        IS_IN_SELECT_ALL_MODE = false;
        notificationsAdapter.notifyDataSetChanged();
        selectedItems.clear();
        counter = 0;
    }

    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        notificationTitle.setText("0 item selected");
        toolbar.inflateMenu(R.menu.notifications_menu);
        CB_selectAll.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IS_IN_ACTION_MODE = true;
        IS_IN_SELECT_ALL_MODE = false;
        notificationsAdapter.notifyDataSetChanged();
        return true;
    }

    public void updateSelection(View v, int position) {
        if (((CheckBox) v).isChecked()) {
            Log.i("TAG", "updateSelection: " + position);
            counter += 1;
            updateCounter(counter);
            selectedItems.add(position);
        } else {
            counter -= 1;
            updateCounter(counter);
            if (selectedItems.size() > 0) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i) == position) {
                        selectedItems.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void updateCounter(int counter) {
        if (counter == 0) {
            notificationTitle.setText("0 item selected");
        } else {
            notificationTitle.setText(counter + " items selected");
        }
    }

    @Override
    public void onBackPressed() {
        if (IS_IN_ACTION_MODE) {
            clearActionMode();
        } else {
            super.onBackPressed();
        }
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.notificationsStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String notificationType = sharedPreferences.getString("NOTIFICATION_TYPE", null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(notificationType + "_COUNT", 0);
        editor.apply();
    }
}
