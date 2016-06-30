package abbottabad.comsats.campusapp;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import abbottabad.comsats.campusapp.Modals.BloodBankModal;

/**
 * Created by Kamran Ramzan on 6/8/16.
 */
public class BloodRequestsSendFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private TextInputLayout TILname, TILreg, TILcontact;
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

        TILname = (TextInputLayout) view.findViewById(R.id.TILname);
        if (TILname != null) {
            TILname.setHint("Name");
        }
        TILreg = (TextInputLayout) view.findViewById(R.id.TILreg);
        if (TILreg != null) {
            TILreg.setHint("Registration ID");
        }
        TILcontact = (TextInputLayout) view.findViewById(R.id.TILcontact);
        if (TILcontact != null) {
            TILcontact.setHint("Contact no");
        }

        SPbloodTypeOptions = (Spinner) view.findViewById(R.id.SPbloodType);
        new BloodBankController(view.getContext()).populateSpinner(SPbloodTypeOptions);
        SPbloodTypeOptions.setOnItemSelectedListener(this);

        ETname = (EditText) view.findViewById(R.id.ETname);
        ETregistration = (EditText) view.findViewById(R.id.ETregistration);
        ETcontact = (EditText) view.findViewById(R.id.ETcontact);

        Button btnSendRequest = (Button) view.findViewById(R.id.btnsendRequest);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerOption != 0){
                    String name = ETname.getText().toString().trim();
                    String registration = ETregistration.getText().toString().trim();
                    String contact = ETcontact.getText().toString().trim();
                    new BloodBankModal(view.getContext()).sendRequest(name, registration, contact, bloodType);
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
    public void setLoginError() {
        TILname.setError("Invalid name");
        TILreg.setError("Invalid registration id");
        TILcontact.setError("Invalid contact no");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerOption = position;
        bloodType = SPbloodTypeOptions.getSelectedItem().toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
