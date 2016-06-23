package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * Created by Kamran Ramzan on 5/25/16.
 */
public class SignUpView extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static TextInputLayout TILname, TILreg, TILsection;
    private static int spinnerOption;
    private AlertDialog alertDialog = null;
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        TILname = (TextInputLayout) findViewById(R.id.TILname);
        if (TILname != null) {
            TILname.setHint("Name");
        }
        TILreg = (TextInputLayout) findViewById(R.id.TILreg);
        if (TILreg != null) {
            TILreg.setHint("Registration ID");
        }
        TILsection = (TextInputLayout) findViewById(R.id.TILsection);
        if (TILsection != null) {
            TILsection.setHint("Section");
        }

        EditText ETname = (EditText) findViewById(R.id.ETname);
        EditText ETregistration = (EditText) findViewById(R.id.ETregistration);
        EditText ETsection = (EditText) findViewById(R.id.ETsection);

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
                            SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("SIGNUP_OPTION", 1);
                            editor.apply();

                            startActivity(new Intent(SignUpView.this, HomePageView.class));
                            break;
                        }
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("Please select login option");
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

    public void setLoginError() {
        TILname.setError("Invalid username");
        TILreg.setError("Invalid password");
    }
}
