package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/30/16.
 */
public class ComplaintPollView extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private EditText ET_description;
    private EditText ET_name;
    private EditText ET_regID;
    private EditText ET_contact;
    private TextView TV_descriptionCounter;
    private final int descriptionLength = 320;
    public static RecyclerView RV_complaints;
    public static ComplaintPollVIewAdapter complaintPollVIewAdapter;
    private Validation validation;
    private Spinner SPadminOptions;
    private int adminOption;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        SharedPreferences applicationStatus = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String APPLICATION_STATUS = applicationStatus.getString("APPLICATION_STATUS", null);
        if (APPLICATION_STATUS != null) {
            if (APPLICATION_STATUS.equals("FOOD")) {
                setContentView(R.layout.complaintpoll_page_admin);
                RV_complaints = (RecyclerView) findViewById(R.id.RV_complaints);
                if (RV_complaints != null) {
                    RV_complaints.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    RV_complaints.setLayoutManager(layoutManager);
                    RV_complaints.addItemDecoration(new RecyclerViewDivider(this));
                    complaintPollVIewAdapter = new ComplaintPollVIewAdapter();
                    ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(complaintPollVIewAdapter);
                    RV_complaints.setAdapter(scaleInAnimationAdapter);
                    new ComplaintPollLocalModal(this).retrieveComplaints();
                }
            } else {
                setContentView(R.layout.complaintpoll_page_others);
                ImageView IV_openAdminSpinner = (ImageView) findViewById(R.id.IV_openAdminSpinner);
                if (IV_openAdminSpinner != null) {
                    IV_openAdminSpinner.setOnClickListener(this);
                }

                SPadminOptions = (Spinner) findViewById(R.id.SPadmin);
                populateSpinner();
                if (SPadminOptions != null) {
                    SPadminOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            adminOption = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                TV_descriptionCounter = (TextView) findViewById(R.id.TV_descriptionCounter);
                ET_description = (EditText) findViewById(R.id.ET_desciption);
                ET_description.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        TV_descriptionCounter.setText(String.valueOf(descriptionLength - s.length()));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                ET_name = (EditText) findViewById(R.id.ET_name);
                ET_regID = (EditText) findViewById(R.id.ET_regID);
                ET_contact = (EditText) findViewById(R.id.ET_contact);
                Button btn_sendComplaint = (Button) findViewById(R.id.btn_sendComplaint);
                if (btn_sendComplaint != null) {
                    btn_sendComplaint.setOnClickListener(this);
                }
                validation = new Validation();
            }
        }
    }

    void populateSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(ComplaintPollView.this,
                R.array.admin_options,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to Spinner
        SPadminOptions.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendComplaint: {
                String name = ET_name.getText().toString();
                String contact = ET_contact.getText().toString();
                String regID = ET_regID.getText().toString();
                String description = ET_description.getText().toString();
                if (validation.validateName(name)) {
                    if (validation.validateReg(regID)) {
                        if (validation.validatePhoneNumber(contact)) {
                            if (adminOption != 0) {
                                switch (adminOption){
                                    case 1:{
                                        new ComplaintPollModal(ComplaintPollView.this).sendComplaint(name, regID, contact, description);
                                        break;
                                    }
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Error");
                                builder.setMessage("Please select complaint admin");
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
                            Toast.makeText(ComplaintPollView.this, "Invalid contact #", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ComplaintPollView.this, "Invalid registration id", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ComplaintPollView.this, "Invalid name", Toast.LENGTH_SHORT).show();
            }
            case R.id.IV_openAdminSpinner: {
                SPadminOptions.performClick();
                break;
            }
        }
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.complaintPollStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
