package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/2/16.
 */
public class HomePageView extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private AlertDialog AD_logout;
    private AlertDialog AD_passwordDialog;
    private AlertDialog AD_emailDialog;
    private AlertDialog AD_unameDialig;
    public static EditText ET_changePassword;
    public static EditText ET_changeEmail;
    public static EditText ET_changeUname;
    public static ProgressBar isChangingPassword;
    public static ProgressBar isChangingEmail;
    public static ProgressBar isChangingUnam;
    private Validation validation;

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
                    startActivity(new Intent(HomePageView.this, ComplaintPollView.class));
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

        validation = new Validation();
        newUnameAlertDialog();
        newPasswordAlertDiaog();
        newEmailAlertDialog();

    }

    public void newUnameAlertDialog(){
        View view = LayoutInflater.from(HomePageView.this).inflate(R.layout.change_uname, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageView.this);
        builder.setView(view);
        builder.setCancelable(false);
        AD_unameDialig = builder.create();
        ET_changeUname = (EditText) view.findViewById(R.id.ET_changeUname);
        isChangingUnam = (ProgressBar) view.findViewById(R.id.isChangingUname);
        isChangingUnam.setVisibility(View.GONE);

        Button btn_cancelUDialog = (Button) view.findViewById(R.id.btn_cancelUDialog);
        if (btn_cancelUDialog != null) {
            btn_cancelUDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AD_unameDialig.cancel();
                    isChangingUnam.setVisibility(View.GONE);
                    ET_changeUname.setVisibility(View.VISIBLE);
                }
            });
        }


        Button btn_changeUname = (Button) view.findViewById(R.id.btn_changeUname);
        btn_changeUname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUname = ET_changeUname.getText().toString().trim();
                if (validation.validateUsername(newUname)) {
                    new HomePageModal(HomePageView.this).changeUname(newUname);
                    Animation compress = AnimationUtils.loadAnimation(HomePageView.this, R.anim.compress);
                    ET_changeUname.startAnimation(compress);
                    compress.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isChangingUnam.setVisibility(View.VISIBLE);
                            ET_changeUname.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }else {
                    Toast.makeText(HomePageView.this, "Invalid username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void newPasswordAlertDiaog() {
        View view = LayoutInflater.from(HomePageView.this).inflate(R.layout.change_password, null);
        AlertDialog.Builder pBuilder = new AlertDialog.Builder(HomePageView.this);
        pBuilder.setView(view);
        pBuilder.setCancelable(false);
        AD_passwordDialog = pBuilder.create();
        ET_changePassword = (EditText) view.findViewById(R.id.ET_changePassword);
        isChangingPassword = (ProgressBar) view.findViewById(R.id.isChangingPassword);
        isChangingPassword.setVisibility(View.GONE);

        Button btn_cancelPDialog = (Button) view.findViewById(R.id.btn_cancelPDialog);
        if (btn_cancelPDialog != null) {
            btn_cancelPDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AD_passwordDialog.cancel();
                    isChangingPassword.setVisibility(View.GONE);
                    ET_changePassword.setVisibility(View.VISIBLE);
                }
            });
        }


        Button btn_changePassword = (Button) view.findViewById(R.id.btn_changePassword);
        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = ET_changePassword.getText().toString().trim();
                if (validation.validatePassword(newPassword)) {
                    new HomePageModal(HomePageView.this).changePassword(newPassword);
                    Animation compress = AnimationUtils.loadAnimation(HomePageView.this, R.anim.compress);
                    ET_changePassword.startAnimation(compress);
                    compress.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isChangingPassword.setVisibility(View.VISIBLE);
                            ET_changePassword.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }else {
                    Toast.makeText(HomePageView.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void newEmailAlertDialog(){
        View view = LayoutInflater.from(HomePageView.this).inflate(R.layout.change_email, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageView.this);
        builder.setView(view);
        builder.setCancelable(false);
        AD_emailDialog = builder.create();
        ET_changeEmail = (EditText) view.findViewById(R.id.ET_changeEmail);
        isChangingEmail = (ProgressBar) view.findViewById(R.id.isChangingEmail);
        isChangingEmail.setVisibility(View.GONE);

        Button btn_cancelEDialog = (Button) view.findViewById(R.id.btn_cancelEDialog);
        if (btn_cancelEDialog != null) {
            btn_cancelEDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AD_emailDialog.cancel();
                    isChangingEmail.setVisibility(View.GONE);
                    ET_changeEmail.setVisibility(View.VISIBLE);
                }
            });
        }


        Button btn_changeEmail = (Button) view.findViewById(R.id.btn_changeEmail);
        btn_changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = ET_changeEmail.getText().toString().trim();
                if (validation.validateEmail(newEmail)) {
                    new HomePageModal(HomePageView.this).changeEmail(newEmail);
                    Animation compress = AnimationUtils.loadAnimation(HomePageView.this, R.anim.compress);
                    ET_changeEmail.startAnimation(compress);
                    compress.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isChangingEmail.setVisibility(View.VISIBLE);
                            ET_changeEmail.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }else {
                    Toast.makeText(HomePageView.this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        if (APPLICATION_STATUS.equals("BLOOD_BANK")) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

        }
        return true;
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
                        AD_logout.cancel();
                    }
                });
                AD_logout = builder.create();
                AD_logout.show();
                break;
            }
            case R.id.action_change_uname: {
                ET_changeUname.setText("");
                HomePageView.isChangingUnam.setVisibility(View.GONE);
                HomePageView.ET_changeUname.setVisibility(View.VISIBLE);
                AD_unameDialig.show();
                break;
            }
            case R.id.action_change_password: {
                ET_changePassword.setText("");
                HomePageView.isChangingPassword.setVisibility(View.GONE);
                HomePageView.ET_changePassword.setVisibility(View.VISIBLE);
                AD_passwordDialog.show();
                break;
            }

            case R.id.action_change_email: {
                ET_changeEmail.setText("");
                isChangingEmail.setVisibility(View.GONE);
                ET_changeEmail.setVisibility(View.VISIBLE);
                AD_emailDialog.show();
                break;
            }
        }
        return true;
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
