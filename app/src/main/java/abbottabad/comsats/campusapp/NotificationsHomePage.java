package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

public class NotificationsHomePage extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public static NotificationsListAdapter notificationsListAdapter;
    private Toolbar toolbar;
    private boolean IS_IN_ACTION_MODE = false;
    private String groupName;
    private RippleView rippleView;
    private NotificationsModal notificationsModal;
    private ActionBar actionBar;
    private String userName;
    private String regId;
    private String groupTimeStamp;
    private AlertDialog alertDialog;
    private TextView TV_userName;
    private TextView TV_regId;
    private TextView TV_timeStamp;
    private TextView TV_groupName;
    public static boolean isLongClick;
    public static boolean isImageClick;
    private Spinner SP_groupPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_home_page);
        toolbar = (Toolbar) findViewById(R.id.notifications_home_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        actionBar = getSupportActionBar();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            notificationsListAdapter = new NotificationsListAdapter(this);
            recyclerView.setAdapter(notificationsListAdapter);
        }

        FloatingActionButton FAB_createGroup = (FloatingActionButton) findViewById(R.id.FAB_createGroup);
        if (FAB_createGroup != null) {
            FAB_createGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(NotificationsHomePage.this, CreateNotificationsGroup.class));
                }
            });
        }

        notificationsModal = new NotificationsModal(this);
        notificationsModal.getNotificationGroups();

        View view = LayoutInflater.from(NotificationsHomePage.this).inflate(R.layout.notification_group_info_view, null);
        TV_groupName = (TextView) view.findViewById(R.id.TV_groupName);
        TV_userName = (TextView) view.findViewById(R.id.TV_userName);
        TV_regId = (TextView) view.findViewById(R.id.TV_regId);
        TV_timeStamp = (TextView) view.findViewById(R.id.TV_timeStamp);
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationsHomePage.this);
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog = builder.create();

    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationsListAdapter.notifyDataSetChanged();
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.notificationsStatusBar);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    void setActionMode(String groupName, View v, String userName, String regId, String timeStamp){
        rippleView = (RippleView) v;
        v.setBackground(ContextCompat
                .getDrawable(NotificationsHomePage.this,
                        R.drawable.cardview_status_background_checked));
        this.groupName = groupName;
        toolbar.getMenu().clear();
        boolean isMuted = sharedPreferences.getBoolean(groupName + "_MUTED" , false);
        if (isMuted){
            toolbar.inflateMenu(R.menu.notifications_group_menu_unmute);
        } else toolbar.inflateMenu(R.menu.notifications_group_menu_mute);
        actionBar.setDisplayHomeAsUpEnabled(true);
        IS_IN_ACTION_MODE = true;
        this.userName = userName;
        this.regId = regId;
        this.groupTimeStamp = timeStamp;
        boolean isCreatedByMe = sharedPreferences.getBoolean(groupName + "_CREATED_BY_ME" , false);
        if (!isCreatedByMe)
        toolbar.getMenu().getItem(1).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                clearActionMode();
                break;
            }
            case R.id.action_mute_chat: {
                muteGroupChat(groupName);
                clearActionMode();
                break;
            }
            case R.id.action_unmute_chat: {
                unmuteGroupChat(groupName);
                clearActionMode();
                break;
            }
            case R.id.action_info_chat: {
                TV_groupName.setText(groupName);
                TV_userName.setText("Created by: " + userName);
                TV_regId.setText("Registration id: " + regId);
                TV_timeStamp.setText("Created on: " + groupTimeStamp);
                alertDialog.show();
                clearActionMode();
                break;
            }
            case R.id.action_delete_chat: {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationsHomePage.this);
                builder.setCancelable(false);
                builder.setMessage("Delete " + groupName + " ?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNotificationGroup();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearActionMode();
                    }
                });
                break;
            }
        }
        return true;
    }

    void deleteNotificationGroup(){
        notificationsModal.deleteGroup(groupName);
        clearActionMode();
    }

    void clearActionMode(){
        IS_IN_ACTION_MODE = false;
        isLongClick = false;
        actionBar.setDisplayHomeAsUpEnabled(false);
        toolbar.getMenu().clear();
        rippleView.setBackground(ContextCompat
                .getDrawable(NotificationsHomePage.this,
                        R.drawable.cardview_status_background));
    }

    void muteGroupChat(String groupName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(groupName + "_MUTED", true);
        editor.apply();
        notificationsListAdapter.notifyDataSetChanged();
        Toast.makeText(NotificationsHomePage.this, groupName + " is muted", Toast.LENGTH_SHORT).show();
    }

    void unmuteGroupChat(String groupName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(groupName + "_MUTED", false);
        editor.apply();
        notificationsListAdapter.notifyDataSetChanged();
        Toast.makeText(NotificationsHomePage.this, groupName + " is unmuted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (IS_IN_ACTION_MODE){
            clearActionMode();
        } else super.onBackPressed();
    }
}
