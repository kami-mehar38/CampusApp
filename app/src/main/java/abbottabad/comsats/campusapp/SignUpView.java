package abbottabad.comsats.campusapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        validation = new Validation();
        TILname = (TextInputLayout) findViewById(R.id.TILname);
        if (TILname != null) {
            TILname.setHint("Name");
        }
        TILreg = (TextInputLayout) findViewById(R.id.TILreg);
        if (TILreg != null) {
            TILreg.setHint("Registration ID");
        }

        ETname = (EditText) findViewById(R.id.ETname);
        ETregistration = (EditText) findViewById(R.id.ETregistration);

        setActionBarLogo();
        Spinner SPsignupOptions = (Spinner) findViewById(R.id.SPsignupOptions);
        new SignUpController(this).populateSpinner(SPsignupOptions);
        if (SPsignupOptions != null) {
            SPsignupOptions.setOnItemSelectedListener(this);
        }
        Button btnSignup = (Button) findViewById(R.id.btnSignup);
        if (btnSignup != null) {
            btnSignup.setOnClickListener(this);
        }
    }

    /**
     * This method sets the logo in action bar at the left corner of action bar
     */
    private void setActionBarLogo() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.comsats_logo);
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
                if (spinnerOption != 0){
                    switch (spinnerOption){
                        case 1:{
                            String name = ETname.getText().toString().trim();
                            String reg = ETregistration.getText().toString().trim();
                            if (validation.validateName(name)) {
                                if (validation.validateReg(reg)) {
                                    new SignUpModal(SignUpView.this).addTeacher(name, reg);
                                }else TILreg.setError("Invalid registration");
                            }else TILname.setError("Invalid name");
                            break;
                        }
                        case 2:{
                            String name = ETname.getText().toString().trim();
                            String reg = ETregistration.getText().toString().trim();
                            if (validation.validateName(name)) {
                                if (validation.validateReg(reg)) {
                                    new SignUpModal(SignUpView.this).addStudent(name, reg);
                                }else TILreg.setError("Invalid registration");
                            }else TILname.setError("Invalid name");
                            break;
                        }
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("Please select sign up option");
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
        }
    }

}
