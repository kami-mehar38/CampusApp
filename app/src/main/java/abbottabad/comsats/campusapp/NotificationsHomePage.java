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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class NotificationsHomePage extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public static NotificationsListAdapter notificationsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.notifications_home_toolbar);
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

    /*public static void setBadgeCount() {
        int softSocietyBadgeCount = sharedPreferences.getInt("Soft Society_COUNT", 0);
        if (softSocietyBadgeCount > 0){
            badgeSoftSociety.setVisibility(View.VISIBLE);
            badgeSoftSociety.setText(String.valueOf(softSocietyBadgeCount));
            setpopupAnimation(badgeSoftSociety);
        } else badgeSoftSociety.setVisibility(View.GONE);

        int funkadaBadgeCount = sharedPreferences.getInt("Funkada_COUNT", 0);
        if (funkadaBadgeCount > 0){
            badgeFunkada.setVisibility(View.VISIBLE);
            badgeFunkada.setText(String.valueOf(funkadaBadgeCount));
            setpopupAnimation(badgeFunkada);
        } else badgeFunkada.setVisibility(View.GONE);

        int hertzSocietyBadgeCount = sharedPreferences.getInt("Hertz Society_COUNT", 0);
        if (hertzSocietyBadgeCount > 0){
            badgeHetzSociety.setVisibility(View.VISIBLE);
            badgeHetzSociety.setText(String.valueOf(hertzSocietyBadgeCount));
            setpopupAnimation(badgeHetzSociety);
        } else badgeHetzSociety.setVisibility(View.GONE);

        int aadrishBadgeCount = sharedPreferences.getInt("Aadrish_COUNT", 0);
        if (aadrishBadgeCount > 0){
            badgeAadrish.setVisibility(View.VISIBLE);
            badgeAadrish.setText(String.valueOf(aadrishBadgeCount));
            setpopupAnimation(badgeAadrish);
        } else badgeAadrish.setVisibility(View.GONE);

        int clsBadgeCount = sharedPreferences.getInt("Comsats Literary Society_COUNT", 0);
        if (clsBadgeCount > 0){
            badgeCLS.setVisibility(View.VISIBLE);
            badgeCLS.setText(String.valueOf(clsBadgeCount));
            setpopupAnimation(badgeCLS);
        } else badgeCLS.setVisibility(View.GONE);

        int ieeeSocietyBadgeCount = sharedPreferences.getInt("IEEE Society_COUNT", 0);
        if (ieeeSocietyBadgeCount > 0){
            badgeIEEESociety.setVisibility(View.VISIBLE);
            badgeIEEESociety.setText(String.valueOf(ieeeSocietyBadgeCount));
            setpopupAnimation(badgeIEEESociety);
        } else badgeIEEESociety.setVisibility(View.GONE);

        int othersBadgeCount = sharedPreferences.getInt("Others_COUNT", 0);
        if (othersBadgeCount > 0){
            badgeOthers.setVisibility(View.VISIBLE);
            badgeOthers.setText(String.valueOf(othersBadgeCount));
            setpopupAnimation(badgeOthers);
        } else badgeOthers.setVisibility(View.GONE);
    }*/




    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.notificationsStatusBar);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

}
