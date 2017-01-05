package abbottabad.comsats.campusapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/2/16.
 */
public class HomePageView extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 149;
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
    private DrawerLayout mDrawerLayout;

    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = HomePageView.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    public static CheckBox CB_shareTimetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final int TOKEN_GOT = sharedPreferences.getInt("TOKEN_GOT", 0);

        if (TOKEN_GOT != 1){
            startService(new Intent(this, RegistrationIntentService.class));
        }

        new InitialPageController(this).execute();
        isPlayServicesAvailable();
        checkForPermissions();

        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.accelerate_decelerate_aimation);

        RippleView RL_notifications = (RippleView) findViewById(R.id.RL_notifications);

        if (RL_notifications != null) {
            RL_notifications.startAnimation(bounceAnimation);
            RL_notifications.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    startActivity(new Intent(HomePageView.this, NotificationsHomePage.class));
                }
            });
        }

        RippleView RL_timetable = (RippleView) findViewById(R.id.RL_timetable);

        if (RL_timetable != null) {
            RL_timetable.startAnimation(bounceAnimation);
            RL_timetable.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    startActivity(new Intent(HomePageView.this, TimeTableView.class));
                }
            });
        }

        RippleView RL_complaintPoll = (RippleView) findViewById(R.id.RL_complaintPoll);

        if (RL_complaintPoll != null) {
            RL_complaintPoll.startAnimation(bounceAnimation);
            RL_complaintPoll.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    startActivity(new Intent(HomePageView.this, ComplaintPollHomePage.class));
                }
            });
        }

        RippleView RL_tracking = (RippleView) findViewById(R.id.RL_trackFaculty);

        if (RL_tracking != null) {
            RL_tracking.startAnimation(bounceAnimation);
            RL_tracking.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    startActivity(new Intent(HomePageView.this, TrackFacultyView.class));
                }
            });
        }

        RippleView RL_parking = (RippleView) findViewById(R.id.RL_parking);

        if (RL_parking != null) {
            RL_parking.startAnimation(bounceAnimation);
            RL_parking.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    startActivity(new Intent(HomePageView.this, ParkingHomePage.class));
                }
            });
        }

        RippleView RL_bloodBank = (RippleView) findViewById(R.id.RL_bloodBank);

        if (RL_bloodBank != null) {
            RL_bloodBank.startAnimation(bounceAnimation);
            RL_bloodBank.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    startActivity(new Intent(HomePageView.this, BloodBankView.class));
                }
            });
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        validation = new Validation();
        newUnameAlertDialog();
        newPasswordAlertDiaog();
        newEmailAlertDialog();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000);

        CheckBox CB_enableNoifications = (CheckBox) findViewById(R.id.CB_enableNotifications);
        if (CB_enableNoifications != null) {
            CB_enableNoifications.setOnClickListener(this);
            CB_enableNoifications.setChecked(sharedPreferences.getBoolean("RECEIVE_EVENT_NOTIFICATIONS", false));
        }

        CheckBox CB_receiveBloodRequest = (CheckBox) findViewById(R.id.CB_receiveBloodRequest);
        if (CB_receiveBloodRequest != null) {
            CB_receiveBloodRequest.setOnClickListener(this);
            CB_receiveBloodRequest.setChecked(sharedPreferences.getBoolean("RECEIVE_BLOOD_REQUEST", false));
        }

        CheckBox CB_enableFacultyNotifications = (CheckBox) findViewById(R.id.CB_enableFacultyNotifications);
        if (CB_enableFacultyNotifications != null) {
            CB_enableFacultyNotifications.setOnClickListener(this);
            CB_enableFacultyNotifications.setChecked(sharedPreferences.getBoolean("RECEIVE_FACULTY_NOTIFICATIONS", false));
        }

        CB_shareTimetable = (CheckBox) findViewById(R.id.CB_shareTimetable);
        if (CB_shareTimetable != null) {
            CB_shareTimetable.setOnClickListener(this);
            CB_shareTimetable.setChecked(sharedPreferences.getBoolean("SHARE_TIMETABLE", false));
        }

        CheckBox CB_classNotifications = (CheckBox) findViewById(R.id.CB_classNotifications);
        if (CB_classNotifications != null) {
            CB_classNotifications.setOnClickListener(this);
            CB_classNotifications.setChecked(sharedPreferences.getBoolean("RECEIVE_CLASS_NOTIFICATIONS", false));
        }

        CheckBox CB_silentDuringClass = (CheckBox) findViewById(R.id.CB_silentDuringClass);
        if (CB_silentDuringClass != null) {
            CB_silentDuringClass.setOnClickListener(this);
            CB_silentDuringClass.setChecked(sharedPreferences.getBoolean("SILENT_DURING_CLASS", false));
        }

        CheckBox CB_receiveComplaints = (CheckBox) findViewById(R.id.CB_receiveComplaints);
        if (CB_receiveComplaints != null) {
            CB_receiveComplaints.setOnClickListener(this);
            CB_receiveComplaints.setChecked(sharedPreferences.getBoolean("RECEIVE_COMPLAINTS", false));
        }

        String applicationStatus = sharedPreferences.getString("APPLICATION_STATUS", "");
        if (!applicationStatus.equals("TEACHER")) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.viewShareTimetable);
            if (linearLayout != null) {
                linearLayout.setVisibility(View.GONE);
            }
        }
        if (!applicationStatus.equals("FOOD")){
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.complaintView);
            if (linearLayout != null) {
                linearLayout.setVisibility(View.GONE);
            }
        }

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().displayer(new CircleBitmapDisplayer())
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start
    }

    private void checkIfGpsIsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Enable it to make this application better for you?")
                    .setCancelable(true)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                    dialog.cancel();
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void newUnameAlertDialog() {
        View view = LayoutInflater.from(HomePageView.this).inflate(R.layout.change_uname, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageView.this);
        builder.setView(view);
        builder.setCancelable(false);
        AD_unameDialig = builder.create();
        ET_changeUname = (EditText) view.findViewById(R.id.ET_changeUname);
        isChangingUnam = (ProgressBar) view.findViewById(R.id.isChangingUname);
        isChangingUnam.setVisibility(View.INVISIBLE);

        Button btn_cancelUDialog = (Button) view.findViewById(R.id.btn_cancelUDialog);
        if (btn_cancelUDialog != null) {
            btn_cancelUDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AD_unameDialig.cancel();
                    isChangingUnam.setVisibility(View.INVISIBLE);
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
                            ET_changeUname.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                } else {
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
        isChangingPassword.setVisibility(View.INVISIBLE);

        Button btn_cancelPDialog = (Button) view.findViewById(R.id.btn_cancelPDialog);
        if (btn_cancelPDialog != null) {
            btn_cancelPDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AD_passwordDialog.cancel();
                    isChangingPassword.setVisibility(View.INVISIBLE);
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
                            ET_changePassword.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                } else {
                    Toast.makeText(HomePageView.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void newEmailAlertDialog() {
        View view = LayoutInflater.from(HomePageView.this).inflate(R.layout.change_email, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageView.this);
        builder.setView(view);
        builder.setCancelable(false);
        AD_emailDialog = builder.create();
        ET_changeEmail = (EditText) view.findViewById(R.id.ET_changeEmail);
        isChangingEmail = (ProgressBar) view.findViewById(R.id.isChangingEmail);
        isChangingEmail.setVisibility(View.INVISIBLE);

        Button btn_cancelEDialog = (Button) view.findViewById(R.id.btn_cancelEDialog);
        if (btn_cancelEDialog != null) {
            btn_cancelEDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AD_emailDialog.cancel();
                    isChangingEmail.setVisibility(View.INVISIBLE);
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
                            ET_changeEmail.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                } else {
                    Toast.makeText(HomePageView.this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        if (APPLICATION_STATUS.equals("BLOOD_BANK") || APPLICATION_STATUS.equals("FOOD")) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(HomePageView.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            handleNewLocation(location);
            LocationController.setLatitide(location.getLatitude());
            LocationController.setLongitude(location.getLongitude());
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 10);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        checkIfGpsIsEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
        LocationController.setLatitide(location.getLatitude());
        LocationController.setLongitude(location.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(HomePageView.this, "Permission granted", Toast.LENGTH_SHORT).show();

                }
                break;
            }
            case 52: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(HomePageView.this, "Permission granted", Toast.LENGTH_SHORT).show();

                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling
            ActivityCompat.requestPermissions(HomePageView.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    52);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CB_enableNotifications:{
                if (((CheckBox) v).isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_EVENT_NOTIFICATIONS", true);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_EVENT_NOTIFICATIONS", false);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Disabled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.CB_receiveBloodRequest:{
                if (((CheckBox) v).isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_BLOOD_REQUEST", true);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_BLOOD_REQUEST", false);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Disabled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.CB_enableFacultyNotifications:{
                if (((CheckBox) v).isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_FACULTY_NOTIFICATIONS", true);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_FACULTY_NOTIFICATIONS", false);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Disabled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.CB_shareTimetable:{
                if (((CheckBox) v).isChecked()){
                    String reg_id =  sharedPreferences.getString("REG_ID", "");
                    new TimetableModal(HomePageView.this).shareTimetable("Public", reg_id);
                } else {
                    String reg_id =  sharedPreferences.getString("REG_ID", "");
                    new TimetableModal(HomePageView.this).shareTimetable("Private", reg_id);
                }
                break;
            }
            case R.id.CB_classNotifications:{
                if (((CheckBox) v).isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_CLASS_NOTIFICATIONS", true);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_CLASS_NOTIFICATIONS", false);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Disabled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.CB_silentDuringClass:{
                if (((CheckBox) v).isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("SILENT_DURING_CLASS", true);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("SILENT_DURING_CLASS", false);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Disabled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.CB_receiveComplaints:{
                if (((CheckBox) v).isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_COMPLAINTS", true);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("RECEIVE_COMPLAINTS", false);
                    editor.apply();
                    Toast.makeText(HomePageView.this, "Disabled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
