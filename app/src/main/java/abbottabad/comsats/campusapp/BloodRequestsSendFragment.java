package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/8/16.
 */
public class BloodRequestsSendFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private EditText ETname;
    private EditText ETregistration;
    private EditText ETcontact;
    private int spinnerOption;
    private String bloodType;
    private Spinner SPbloodTypeOptions;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blood_send_request, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final Validation validation = new Validation();

        SPbloodTypeOptions = (Spinner) view.findViewById(R.id.SPbloodType);
        new BloodBankController(getContext()).populateSpinner(SPbloodTypeOptions);
        SPbloodTypeOptions.setOnItemSelectedListener(this);

        ETname = (EditText) view.findViewById(R.id.ETname);
        ETname.setText(sharedPreferences.getString("NAME", ""));
        ETregistration = (EditText) view.findViewById(R.id.ETregistration);
        ETregistration.setText(sharedPreferences.getString("REG_ID", ""));
        ETcontact = (EditText) view.findViewById(R.id.ETcontact);
        ETcontact.setText(sharedPreferences.getString("CONTACT", ""));
        ImageView IV_openBloodSpinner = (ImageView) view.findViewById(R.id.IV_openBloodSpinner);
        IV_openBloodSpinner.setOnClickListener(this);

        Button btnSendRequest = (Button) view.findViewById(R.id.btnsendRequest);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerOption != 0){
                    String name = ETname.getText().toString().trim();
                    String registration = ETregistration.getText().toString().trim();
                    String contact = ETcontact.getText().toString().trim();
                    if (validation.validateName(name)){
                        if (validation.validateReg(registration)){
                            if (validation.validatePhoneNumber(contact)){
                                new BloodBankModal(view.getContext()).sendRequest(name, registration, contact, bloodType);
                                Log.i("TAG", "onClick: " + String.valueOf(LocationController.getLatitide()));
                                Log.i("TAG", "onClick: " + String.valueOf(LocationController.getLongitude()));
                            } else Toast.makeText(getContext(), "Invalid contac#", Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(getContext(), "Invalid registration id", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Warning");
                    builder.setMessage("Please select the required blood group");
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerOption = position;
        bloodType = SPbloodTypeOptions.getSelectedItem().toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IV_openBloodSpinner: {
                SPbloodTypeOptions.performClick();
                break;
            }
        }
    }
}
