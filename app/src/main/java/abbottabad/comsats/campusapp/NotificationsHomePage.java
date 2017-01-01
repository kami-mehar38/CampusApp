package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

public class NotificationsHomePage extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_notifications_page);
        toolbar = (Toolbar) findViewById(R.id.notifications_home_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerBB);
        EventNotificationsController eventNotificationsController = new EventNotificationsController();
        eventNotificationsController.setupViewPager(viewPager, getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBB);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        }
        String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        actionBar = getSupportActionBar();

        notificationsModal = new NotificationsModal(this);

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
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
        NotificationsGroupFragment.notificationsListAdapter.notifyDataSetChanged();
        Toast.makeText(NotificationsHomePage.this, groupName + " is muted", Toast.LENGTH_SHORT).show();
    }

    void unmuteGroupChat(String groupName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(groupName + "_MUTED", false);
        editor.apply();
        NotificationsGroupFragment.notificationsListAdapter.notifyDataSetChanged();
        Toast.makeText(NotificationsHomePage.this, groupName + " is unmuted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (IS_IN_ACTION_MODE){
            clearActionMode();
        } else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationsGroupFragment.isFirstTime = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NotificationsGroupFragment.notificationsListAdapter != null)
        NotificationsGroupFragment.notificationsListAdapter.notifyDataSetChanged();
    }
}
