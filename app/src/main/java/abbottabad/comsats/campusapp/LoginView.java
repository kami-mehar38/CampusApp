package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This project CampusApp is created by Kamran Ramzan on 5/25/16.
 */

public class LoginView extends AppCompatActivity implements View.OnClickListener{

    private static EditText ETuname;
    private EditText ETpassword;
    private int spinnerOption;
    private AlertDialog alertDialog = null;
    private Spinner SPloginOptions;
    public static String designation;
    private TextView TV_forgotPassword;
    private ScrollView SV_loginPage;
    private Button btnLogin;
    public static Button btnSendMail;
    private Button btnGoBack;
    private TextView TV_enterAccountMail;
    private EditText ET_accountMail;
    private Spinner SP_forgotPassword;
    private ScrollView SV_forgotPassword;
    public static ProgressBar isSending;
    public static String FP_designation;
    private int FP_spinnerOption;
    private Validation validation;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.loginpage_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        ETuname = (EditText) findViewById(R.id.ETname);
        ETpassword = (EditText) findViewById(R.id.ETpassword);
        ImageView IV_openAccountSpinner = (ImageView) findViewById(R.id.IV_openAccountSpinner);
        if (IV_openAccountSpinner != null) {
            IV_openAccountSpinner.setOnClickListener(this);
        }
        ImageView IV_openForgotAccountSpinner = (ImageView) findViewById(R.id.IV_openForgotAccountSpinner);
        if (IV_openForgotAccountSpinner != null) {
            IV_openForgotAccountSpinner.setOnClickListener(this);
        }

        SPloginOptions = (Spinner) findViewById(R.id.SPloginOptions);
        new LoginController(this).populateSpinner(SPloginOptions);
        if (SPloginOptions != null) {
            SPloginOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerOption = position;
                    designation = SPloginOptions.getSelectedItem().toString().trim();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        btnLogin = (Button) findViewById(R.id.btnLogin);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = ETuname.getText().toString().trim();
                    String password = ETpassword.getText().toString().trim();
                    if (validation.validateUsername(username)) {
                        if (validation.validatePassword(password)) {
                            if (spinnerOption != 0) {
                                switch (spinnerOption) {
                                    case 1: {
                                        new LoginModal(LoginView.this).login(username, password, "BLOOD_BANK");
                                        break;
                                    }
                                    case 2: {
                                        new LoginModal(LoginView.this).login(username, password, "FOOD");
                                        break;
                                    }
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginView.this);
                                builder.setTitle("Error");
                                builder.setMessage("Please select account");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        if (alertDialog != null) {
                                            alertDialog.cancel();
                                        }
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();
                            }
                        } else
                            Toast.makeText(LoginView.this, "Invalid password format", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(LoginView.this, "Invalid username format", Toast.LENGTH_SHORT).show();
                }
            });
        }

        validation = new Validation();
        SV_forgotPassword = (ScrollView) findViewById(R.id.SV_forgotPassword);
        if (SV_forgotPassword != null) {
            SV_forgotPassword.setVisibility(View.GONE);
        }
        TV_enterAccountMail = (TextView) findViewById(R.id.TV_enterAccountMail);
        if (TV_enterAccountMail != null) {
            TV_enterAccountMail.setVisibility(View.GONE);
        }
        ET_accountMail = (EditText) findViewById(R.id.ET_accountMail);
        if (ET_accountMail != null) {
            ET_accountMail.setVisibility(View.GONE);
        }
        SP_forgotPassword = (Spinner) findViewById(R.id.SP_forgotPassword);
        SP_forgotPassword.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FP_spinnerOption = position;
                FP_designation = SP_forgotPassword.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SP_forgotPassword.setVisibility(View.GONE);
        new LoginController(this).populateSpinner(SP_forgotPassword);
        isSending = (ProgressBar) findViewById(R.id.isSending);
        if (isSending != null) {
            isSending.setVisibility(View.GONE);
        }
        btnSendMail = (Button) findViewById(R.id.btnSendMail);
        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation.validateEmail(ET_accountMail.getText().toString())) {
                    if (FP_spinnerOption != 0) {
                        switch (FP_spinnerOption) {
                            case 1: {

                                String email = ET_accountMail.getText().toString().trim();
                                new LoginModal(LoginView.this).resetPassword(FP_designation, email);
                                break;
                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginView.this);
                        builder.setTitle("Error");
                        builder.setMessage("Please select account");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if (alertDialog != null) {
                                    alertDialog.cancel();
                                }
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                    }
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginView.this);
                    builder.setTitle("Error");
                    builder.setMessage("Invalid email address.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            if (alertDialog != null) {
                                alertDialog.cancel();
                            }
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });
        btnSendMail.setVisibility(View.GONE);
        btnGoBack = (Button) findViewById(R.id.goBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.forgot_password_in);
                SV_loginPage.startAnimation(scaleIn);
                scaleIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        ETuname.setVisibility(View.VISIBLE);
                        ETpassword.setVisibility(View.VISIBLE);
                        SPloginOptions.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.VISIBLE);
                        TV_forgotPassword.setVisibility(View.VISIBLE);

                        SV_forgotPassword.setVisibility(View.GONE);
                        TV_enterAccountMail.setVisibility(View.GONE);
                        ET_accountMail.setVisibility(View.GONE);
                        SP_forgotPassword.setVisibility(View.GONE);
                        btnSendMail.setVisibility(View.GONE);
                        btnGoBack.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        btnGoBack.setVisibility(View.GONE);


        SV_loginPage = (ScrollView) findViewById(R.id.SV_loginPage);
        TV_forgotPassword = (TextView) findViewById(R.id.TV_forgotPassword);
        TV_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.forgot_password_out);
                scaleOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ETuname.setVisibility(View.GONE);
                        ETpassword.setVisibility(View.GONE);
                        SPloginOptions.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.GONE);
                        TV_forgotPassword.setVisibility(View.GONE);

                        SV_forgotPassword.setVisibility(View.VISIBLE);
                        TV_enterAccountMail.setVisibility(View.VISIBLE);
                        ET_accountMail.setVisibility(View.VISIBLE);
                        ET_accountMail.setText("");
                        SP_forgotPassword.setVisibility(View.VISIBLE);
                        SP_forgotPassword.setSelection(0);
                        btnSendMail.setVisibility(View.VISIBLE);
                        btnGoBack.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                SV_loginPage.startAnimation(scaleOut);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IV_openAccountSpinner: {
                SPloginOptions.performClick();
                break;
            }
            case R.id.IV_openForgotAccountSpinner: {
                SP_forgotPassword.performClick();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (sharedPreferences.getBoolean("LOGGED_IN", false)) {
            super.onBackPressed();
        } else {
            startActivity(new Intent(LoginView.this, InitialPage.class));
            finish();
        }
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.loginStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
