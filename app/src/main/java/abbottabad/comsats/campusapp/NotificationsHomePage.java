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
    private TextView badgeSoftSociety;
    private TextView badgeFunkada;
    private TextView badgeHetzSociety;
    private TextView badgeAadrish;
    private TextView badgeCLS;
    private TextView badgeIEEESociety;
    private TextView badgeOthers;
    private SharedPreferences sharedPreferences;

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
        LinearLayout listSoftSociet = (LinearLayout) findViewById(R.id.listSoftSociety);
        if (listSoftSociet != null) {
            listSoftSociet.setOnClickListener(this);
        }

        LinearLayout listFunkada = (LinearLayout) findViewById(R.id.listFunkada);
        if (listFunkada != null) {
            listFunkada.setOnClickListener(this);
        }

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
                editor.putString("NOTIFICATION_TYPE", "SOFT_SOCIETY");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
            case R.id.listFunkada: {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NOTIFICATION_TYPE", "FUNKADA");
                editor.apply();
                startActivity(new Intent(NotificationsHomePage.this, NotificationsView.class));
                break;
            }
        }
    }
}
