package abbottabad.comsats.campusapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This project CampusApp is created by Kamran Ramzan on 5/25/16.
 */
public class SignUpView extends AppCompatActivity implements View.OnClickListener {

    private static int signupOption;
    private static int bloodOption;
    private AlertDialog alertDialog = null;
    private EditText ETname;
    private EditText ETregistration;
    private EditText ETcontact;
    private Validation validation;
    private Spinner SPBloodOptions;
    private Spinner SPsignupOptions;
    private String bloodGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.signuppage_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        validation = new Validation();
        ETname = (EditText) findViewById(R.id.ETname);
        ETregistration = (EditText) findViewById(R.id.ETregistration);
        ETcontact = (EditText) findViewById(R.id.ETcontact);
        ImageView IV_openSignupSpinner = (ImageView) findViewById(R.id.IV_openSignupSpinner);
        if (IV_openSignupSpinner != null) {
            IV_openSignupSpinner.setOnClickListener(this);
        }

        SPsignupOptions = (Spinner) findViewById(R.id.SPsignupOptions);
        new SignUpController(this).populateSpinner(SPsignupOptions);
        if (SPsignupOptions != null) {
            SPsignupOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    signupOption = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        ImageView IV_openBloodSpinner = (ImageView) findViewById(R.id.IV_openBloodSpinner);
        if (IV_openBloodSpinner != null) {
            IV_openBloodSpinner.setOnClickListener(this);
        }

        SPBloodOptions = (Spinner) findViewById(R.id.SPbloodType);
        new BloodBankController(this).populateSpinner(SPBloodOptions);
        if (SPBloodOptions != null) {
            SPBloodOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    bloodGroup = SPBloodOptions.getSelectedItem().toString().trim();
                    bloodOption = position;
                    Log.i("TAG", "onItemSelected: " + bloodGroup);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        Button btnSignup = (Button) findViewById(R.id.btnSignup);
        if (btnSignup != null) {
            btnSignup.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignup: {
                String name = ETname.getText().toString().trim();
                String reg = ETregistration.getText().toString().trim();
                String contact = ETcontact.getText().toString().trim();
                if (validation.validateName(name)) {
                    if (validation.validateReg(reg)) {
                        if (validation.validatePhoneNumber(contact)) {
                            if (bloodOption != 0) {
                                if (signupOption != 0) {
                                    switch (signupOption) {
                                        case 1: {
                                            new SignUpModal(SignUpView.this).addTeacher(name, reg, contact, bloodGroup);
                                            break;
                                        }
                                        case 2: {
                                            new SignUpModal(SignUpView.this).addStudent(name, reg, contact, bloodGroup);
                                            break;
                                        }
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setTitle("Error");
                                    builder.setMessage("Please select sign up option");
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Error");
                                builder.setMessage("Please select blood group");
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
                            Toast.makeText(SignUpView.this, "Invalid contact #", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(SignUpView.this, "Invalid registration id", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(SignUpView.this, "Invalid name", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.IV_openBloodSpinner: {
                SPBloodOptions.performClick();
                break;
            }
            case R.id.IV_openSignupSpinner: {
                SPsignupOptions.performClick();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpView.this, InitialPage.class));
        finish();
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.signupStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
