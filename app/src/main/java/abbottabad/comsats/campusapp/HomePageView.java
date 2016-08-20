package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Kamran Ramzan on 6/2/16.
 */
public class HomePageView extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        new InitialPageController(this).execute();
        isPlayServicesAvailable();

        CardView cv_bloodBank = (CardView) findViewById(R.id.CV_bloodBank);

        if (cv_bloodBank != null) {
            cv_bloodBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomePageView.this, BloodBankView.class));
                    }
                });
            }

        CardView cv_tracking = (CardView) findViewById(R.id.CV_tracking);

        if (cv_tracking != null) {
            cv_tracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomePageView.this, TrackFacultyView.class));
                }
            });
        }

        CardView cv_notifications = (CardView) findViewById(R.id.CV_notifications);

        if (cv_notifications != null) {
            cv_notifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomePageView.this, NotificationsView.class));
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
