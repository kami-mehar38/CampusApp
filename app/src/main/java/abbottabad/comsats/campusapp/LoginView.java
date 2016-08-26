package abbottabad.comsats.campusapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This project CampusApp is created by Kamran Ramzan on 5/25/16.
 */
public class LoginView extends AppCompatActivity {

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
    private TextView TV_enterAccountMail;
    private EditText ET_accountMail;
    private Spinner SP_forgotPassword;
    private ScrollView SV_forgotPassword;
    public static ProgressBar isSending;
    public static String FP_designation;
    private int FP_spinnerOption;
    private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.loginpage_toolbar);
        setSupportActionBar(toolbar);
        TextInputLayout TIname = (TextInputLayout) findViewById(R.id.TILname);
        if (TIname != null) {
            TIname.setHint("Username");
        }
        TextInputLayout TIpass = (TextInputLayout) findViewById(R.id.TILpass);
        if (TIpass != null) {
            TIpass.setHint("Password");
        }
        ETuname = (EditText) findViewById(R.id.ETname);
        ETpassword = (EditText) findViewById(R.id.ETpassword);

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
                    if (spinnerOption != 0){
                        switch (spinnerOption){
                            case 1:{
                                String username = ETuname.getText().toString().trim();
                                String password = ETpassword.getText().toString().trim();
                                new LoginModal(LoginView.this).login(username, password);

                                break;
                            }
                        }
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginView.this);
                        builder.setTitle("Error");
                        builder.setMessage("Please select designation");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if (alertDialog != null){
                                    alertDialog.cancel();
                                }
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });
        }

        validation = new Validation();
        SV_forgotPassword = (ScrollView) findViewById(R.id.SV_forgotPassword);
        SV_forgotPassword.setVisibility(View.GONE);
        TV_enterAccountMail = (TextView) findViewById(R.id.TV_enterAccountMail);
        TV_enterAccountMail.setVisibility(View.GONE);
        ET_accountMail = (EditText) findViewById(R.id.ET_accountMail);
        ET_accountMail.setVisibility(View.GONE);
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
        isSending.setVisibility(View.GONE);
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
                        builder.setMessage("Please select disignation");
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
                }else {

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

        SV_loginPage = (ScrollView) findViewById(R.id.SV_loginPage);
        TV_forgotPassword = (TextView) findViewById(R.id.TV_forgotPassword);
        TV_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.forgot_password);
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
                        SP_forgotPassword.setVisibility(View.VISIBLE);
                        btnSendMail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                SV_loginPage.startAnimation(scaleOut);
            }
        });
    }

}
