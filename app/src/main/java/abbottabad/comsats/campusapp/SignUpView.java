package abbottabad.comsats.campusapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
public class SignUpView extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static TextInputLayout TILname, TILreg;
    private static int spinnerOption;
    private AlertDialog alertDialog = null;
    private EditText ETname;
    private EditText ETregistration;
    private Validation validation;
    private Spinner SPsignupOptions;


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
        ImageView IV_openBloodSpinner = (ImageView) findViewById(R.id.IV_openBloodSpinner);
        if (IV_openBloodSpinner != null) {
            IV_openBloodSpinner.setOnClickListener(this);
        }

        SPsignupOptions = (Spinner) findViewById(R.id.SPsignupOptions);
        new SignUpController(this).populateSpinner(SPsignupOptions);
        if (SPsignupOptions != null) {
            SPsignupOptions.setOnItemSelectedListener(this);
        }
        Button btnSignup = (Button) findViewById(R.id.btnSignup);
        if (btnSignup != null) {
            btnSignup.setOnClickListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerOption = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "Nothing is selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignup:{
                String name = ETname.getText().toString().trim();
                String reg = ETregistration.getText().toString().trim();
                if (validation.validateName(name)) {
                    if (validation.validateReg(reg)) {
                        if (spinnerOption != 0) {
                            switch (spinnerOption) {
                                case 1: {
                                    new SignUpModal(SignUpView.this).addTeacher(name, reg);
                                    break;
                                }
                                case 2: {
                                    new SignUpModal(SignUpView.this).addStudent(name, reg);
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
                            break;
                        }
                    } else {
                        Toast.makeText(SignUpView.this, "Invalid registration id", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    Toast.makeText(SignUpView.this, "Invalid name", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            case R.id.IV_openBloodSpinner: {
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
