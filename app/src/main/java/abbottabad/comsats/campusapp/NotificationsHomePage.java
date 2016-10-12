package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotificationsHomePage extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    public static TextView badgeSoftSociety;
    public static TextView badgeFunkada;
    public static TextView badgeHetzSociety;
    public static TextView badgeAadrish;
    public static TextView badgeCLS;
    public static TextView badgeIEEESociety;
    public static TextView badgeOthers;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_home_page);
        toolbar = (Toolbar) findViewById(R.id.notifications_home_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        initializeBadges();
        setBadgeCount();
        LinearLayout listSoftSociet = (LinearLayout) findViewById(R.id.listSoftSociety);
        if (listSoftSociet != null) {
            listSoftSociet.setOnClickListener(this);
        }

        LinearLayout listFunkada = (LinearLayout) findViewById(R.id.listFunkada);
        if (listFunkada != null) {
            listFunkada.setOnClickListener(this);
        }

        LinearLayout listHertzSociety = (LinearLayout) findViewById(R.id.listHertzSociety);
        if (listHertzSociety != null) {
            listHertzSociety.setOnClickListener(this);
        }

        LinearLayout listAadrish = (LinearLayout) findViewById(R.id.listAadrish);
        if (listAadrish != null) {
            listAadrish.setOnClickListener(this);
        }

        LinearLayout listCLS = (LinearLayout) findViewById(R.id.listCLS);
        if (listCLS != null) {
            listCLS.setOnClickListener(this);
        }

        LinearLayout listIEEESociety = (LinearLayout) findViewById(R.id.listIEEESociety);
        if (listIEEESociety != null) {
            listIEEESociety.setOnClickListener(this);
        }

        LinearLayout listOthers = (LinearLayout) findViewById(R.id.listOthers);
        if (listOthers != null) {
            listOthers.setOnClickListener(this);
        }

    }

    public static void setBadgeCount() {
        int softSocietyBadgeCount = sharedPreferences.getInt("Soft Society_COUNT", 0);
        if (softSocietyBadgeCount > 0){
            badgeSoftSociety.setVisibility(View.VISIBLE);
            badgeSoftSociety.setText(String.valueOf(softSocietyBadgeCount));
        } else badgeSoftSociety.setVisibility(View.GONE);

        int funkadaBadgeCount = sharedPreferences.getInt("Funkada_COUNT", 0);
        if (funkadaBadgeCount > 0){
            badgeFunkada.setVisibility(View.VISIBLE);
            badgeFunkada.setText(String.valueOf(funkadaBadgeCount));
        } else badgeFunkada.setVisibility(View.GONE);

        int hertzSocietyBadgeCount = sharedPreferences.getInt("Hertz Society_COUNT", 0);
        if (hertzSocietyBadgeCount > 0){
            badgeHetzSociety.setVisibility(View.VISIBLE);
            badgeHetzSociety.setText(String.valueOf(hertzSocietyBadgeCount));
        } else badgeHetzSociety.setVisibility(View.GONE);

        int aadrishBadgeCount = sharedPreferences.getInt("Aadrish_COUNT", 0);
        if (aadrishBadgeCount > 0){
            badgeAadrish.setVisibility(View.VISIBLE);
            badgeAadrish.setText(String.valueOf(aadrishBadgeCount));
        } else badgeAadrish.setVisibility(View.GONE);

        int clsBadgeCount = sharedPreferences.getInt("Comsats Literary Society_COUNT", 0);
        if (clsBadgeCount > 0){
            badgeCLS.setVisibility(View.VISIBLE);
            badgeCLS.setText(String.valueOf(clsBadgeCount));
        } else badgeCLS.setVisibility(View.GONE);

        int ieeeSocietyBadgeCount = sharedPreferences.getInt("IEEE Society_COUNT", 0);
        if (ieeeSocietyBadgeCount > 0){
            badgeIEEESociety.setVisibility(View.VISIBLE);
            badgeIEEESociety.setText(String.valueOf(ieeeSocietyBadgeCount));
        } else badgeIEEESociety.setVisibility(View.GONE);

        int othersBadgeCount = sharedPreferences.getInt("Others_COUNT", 0);
        if (othersBadgeCount > 0){
            badgeOthers.setVisibility(View.VISIBLE);
            badgeOthers.setText(String.valueOf(othersBadgeCount));
        } else badgeOthers.setVisibility(View.GONE);
    }

    private void initializeBadges() {
        badgeSoftSociety = (TextView) findViewById(R.id.badgeSoftSociety);
        if (badgeSoftSociety != null) {
            badgeSoftSociety.setVisibility(View.GONE);
        }
        badgeFunkada = (TextView) findViewById(R.id.badgeFunkada);
        if (badgeFunkada != null) {
            badgeFunkada.setVisibility(View.GONE);
        }
        badgeHetzSociety = (TextView) findViewById(R.id.badgeHetzSociety);
        if (badgeHetzSociety != null) {
            badgeHetzSociety.setVisibility(View.GONE);
        }
        badgeAadrish = (TextView) findViewById(R.id.badgeAadrish);
        if (badgeAadrish != null) {
            badgeAadrish.setVisibility(View.GONE);
        }
        badgeCLS = (TextView) findViewById(R.id.badgeCLS);
        if (badgeCLS != null) {
            badgeCLS.setVisibility(View.GONE);
        }
        badgeIEEESociety = (TextView) findViewById(R.id.badgeIEEESociety);
        if (badgeIEEESociety != null) {
            badgeIEEESociety.setVisibility(View.GONE);
        }
        badgeOthers = (TextView) findViewById(R.id.badgeOthers);
        if (badgeOthers != null) {
            badgeOthers.setVisibility(View.GONE);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listSoftSociety: {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NOTIFICATION_TYPE", "Soft Society");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
            case R.id.listFunkada: {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NOTIFICATION_TYPE", "Funkada");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
            case R.id.listHertzSociety: {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NOTIFICATION_TYPE", "Hertz Society");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
            case R.id.listAadrish: {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NOTIFICATION_TYPE", "Aadrish");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
            case R.id.listCLS: {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NOTIFICATION_TYPE", "Comsats Literary Society");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
            case R.id.listIEEESociety: {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NOTIFICATION_TYPE", "IEEE Society");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
            case R.id.listOthers: {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NOTIFICATION_TYPE", "Others");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        setBadgeCount();
        super.onResume();
    }
}
