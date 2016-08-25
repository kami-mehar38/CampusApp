package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/2/16.
 */
public class HomePageView extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        new InitialPageController(this).execute();
        isPlayServicesAvailable();

        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.accelerate_decelerate_aimation);

        RelativeLayout RL_notifications = (RelativeLayout) findViewById(R.id.RL_notifications);

        if (RL_notifications != null) {
            RL_notifications.startAnimation(bounceAnimation);
            RL_notifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomePageView.this, NotificationsView.class));
                }
            });
        }

        RelativeLayout RL_timetable = (RelativeLayout) findViewById(R.id.RL_timetable);

        if (RL_timetable != null) {
            RL_timetable.startAnimation(bounceAnimation);
            RL_timetable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        RelativeLayout RL_complaintPoll = (RelativeLayout) findViewById(R.id.RL_complaintPoll);

        if (RL_complaintPoll != null) {
            RL_complaintPoll.startAnimation(bounceAnimation);
            RL_complaintPoll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        RelativeLayout RL_tracking = (RelativeLayout) findViewById(R.id.RL_trackFaculty);

        if (RL_tracking != null) {
            RL_tracking.startAnimation(bounceAnimation);
            RL_tracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomePageView.this, TrackFacultyView.class));
                }
            });
        }

        RelativeLayout RL_parking = (RelativeLayout) findViewById(R.id.RL_parking);

        if (RL_parking != null) {
            RL_parking.startAnimation(bounceAnimation);
            RL_parking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        RelativeLayout RL_bloodBank = (RelativeLayout) findViewById(R.id.RL_bloodBank);

        if (RL_bloodBank != null) {
            RL_bloodBank.startAnimation(bounceAnimation);
            RL_bloodBank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomePageView.this, BloodBankView.class));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        if (APPLICATION_STATUS.equals("BLOOD_BANK")) {
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:{
                AlertDialog.Builder  builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert?");
                builder.setCancelable(false);
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("yes i'm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences applicationStatus = HomePageView.this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = applicationStatus.edit();
                        editor.putBoolean("LOGGED_IN", false);
                        editor.apply();
                        startActivity(new Intent(HomePageView.this, LoginView.class));
                        finish();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isPlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("This device is not supported");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
            return false;
        }
        return true;
    }
}
