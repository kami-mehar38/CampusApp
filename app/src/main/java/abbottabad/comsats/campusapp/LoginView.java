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
 * Created by Kamran Ramzan on 5/25/16.
 */
public class LoginView extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static EditText ETuname;
    private EditText ETpassword;
    private TextInputLayout TIname, TIpass;
    private int spinnerOption;
    private AlertDialog alertDialog = null;
    private Spinner SPloginOptions;
    public static String designation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        TIname = (TextInputLayout) findViewById(R.id.TILname);
        if (TIname != null) {
            TIname.setHint("Username");
        }
        TIpass = (TextInputLayout) findViewById(R.id.TILpass);
        if (TIpass != null) {
            TIpass.setHint("Password");
        }
        ETuname = (EditText) findViewById(R.id.ETname);
        ETpassword = (EditText) findViewById(R.id.ETpassword);

        setActionBarLogo();

        SPloginOptions = (Spinner) findViewById(R.id.SPloginOptions);
        new LoginController(this).populateSpinner(SPloginOptions);
        if (SPloginOptions != null) {
            SPloginOptions.setOnItemSelectedListener(this);
        }
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(this);
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
        designation = SPloginOptions.getSelectedItem().toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:{
                if (spinnerOption != 0){
                    switch (spinnerOption){
                        case 1:{
                            String username = ETuname.getText().toString().trim();
                            String password = ETpassword.getText().toString().trim();
                            new LoginModal(LoginView.this).execute(username, password);

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
        TIname.setError("Invalid username");
        TIpass.setError("Invalid password");
    }
}
