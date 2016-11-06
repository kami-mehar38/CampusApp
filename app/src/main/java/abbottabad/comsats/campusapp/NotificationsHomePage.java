package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class NotificationsHomePage extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public static NotificationsListAdapter notificationsListAdapter;
    private Toolbar toolbar;
    private boolean IS_IN_ACTION_MODE = false;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_home_page);
        toolbar = (Toolbar) findViewById(R.id.notifications_home_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start

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
        NotificationsModal notificationsModal = new NotificationsModal(this);
        notificationsModal.getNotificationGroups();

        NotificationsLocalModal notificationsLocalModal = new NotificationsLocalModal(this);

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

    void setActionMode(String groupName){
        this.groupName = groupName;
        toolbar.getMenu().clear();
        boolean isMuted = sharedPreferences.getBoolean(groupName + "_MUTED" , false);
        if (isMuted){
            toolbar.inflateMenu(R.menu.notifications_group_menu_unmute);
        } else toolbar.inflateMenu(R.menu.notifications_group_menu_mute);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IS_IN_ACTION_MODE = true;
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
        }
        return true;
    }

    void clearActionMode(){
        IS_IN_ACTION_MODE = false;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.getMenu().clear();
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
