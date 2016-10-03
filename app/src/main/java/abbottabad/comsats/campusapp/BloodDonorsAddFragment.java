package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/30/16.
 */
public class BloodDonorsAddFragment extends Fragment implements View.OnClickListener {

    private Spinner SPbloodTypes;
    private BloodBankController bloodBankController;
    private String bloodType;
    private CheckBox CB_bleeded;
    private DatePicker datePicker;
    private int curr_year;
    private int curr_month;
    private int curr_date;
    private String bleededDate = "0000/00/00";
    private SimpleDateFormat dateFormat;
    private Date selectedDate;
    private AlertDialog alertDialogDate;
    private int spinnerOption;
    private AlertDialog alertDialogBlood;
    private Validation validation;

    public BloodDonorsAddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloodBankController = new BloodBankController(getContext());
        validation = new Validation();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        curr_year = calendar.get(Calendar.YEAR);
        curr_month = calendar.get(Calendar.MONTH);
        curr_date = calendar.get(Calendar.DATE);

        AlertDialog.Builder builderDate = new AlertDialog.Builder(getContext());
        builderDate.setTitle("Invalid date");
        builderDate.setMessage("Please select the valid date i.e. Date should be from past or today but not from future.");
        builderDate.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogDate.cancel();
            }
        });
        alertDialogDate = builderDate.create();

        AlertDialog.Builder builderBlood = new AlertDialog.Builder(getContext());
        builderBlood.setTitle("Error");
        builderBlood.setMessage("Please select the blood group of the donor.");
        builderBlood.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogBlood.cancel();
            }
        });
        alertDialogBlood = builderBlood.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_donor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SPbloodTypes = (Spinner) view.findViewById(R.id.SP_bloodTypes);
        bloodBankController.populateSpinner(SPbloodTypes);
        SPbloodTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerOption = position;
                bloodType = SPbloodTypes.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        try {
            selectedDate = dateFormat.parse("0000/00/00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker = (DatePicker)view.findViewById(R.id.date_picker);
        datePicker.init(curr_year, curr_month, curr_date, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    selectedDate = dateFormat.parse(String.valueOf(year) + "/" + String.valueOf(monthOfYear) +"/"+ String.valueOf(dayOfMonth));
                    bleededDate = String.valueOf(year) + "/" + String.valueOf(monthOfYear+1) +"/"+ String.valueOf(dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        datePicker.setVisibility(View.GONE);
        CB_bleeded = (CheckBox) view.findViewById(R.id.CB_bleeding);
        CB_bleeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CB_bleeded.isChecked()){
                    datePicker.setVisibility(View.VISIBLE);
                    int year = datePicker.getYear();
                    int monthOfYear = datePicker.getMonth();
                    int dayOfMonth = datePicker.getDayOfMonth();
                    bleededDate = String.valueOf(year) + "/" + String.valueOf(monthOfYear+1) +"/"+ String.valueOf(dayOfMonth);
                }else {
                    datePicker.setVisibility(View.GONE);
                    bleededDate = "0000/00/00";
                }
            }
        });

        final EditText ETname = (EditText) view.findViewById(R.id.ETDname);
        ETname.setText(sharedPreferences.getString("NAME", ""));
        final EditText ETregId = (EditText) view.findViewById(R.id.ETDregID);
        ETregId.setText(sharedPreferences.getString("REG_ID", ""));
        final EditText ETcontact = (EditText) view.findViewById(R.id.ETDcontact);
        ETcontact.setText(sharedPreferences.getString("CONTACT", ""));

        final Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date currentDate = dateFormat.parse(String.valueOf(curr_year)
                            + "/" + String.valueOf(curr_month)
                            +"/"+ String.valueOf(curr_date));
                    String name = ETname.getText().toString().trim();
                    String regID = ETregId.getText().toString().trim();
                    String contact = ETcontact.getText().toString().trim();
                    if (validation.validateName(name)) {
                        if (validation.validateReg(regID)) {
                            if (validation.validatePhoneNumber(contact)) {
                                if (selectedDate.before(currentDate) || selectedDate.equals(currentDate)) {
                                    if (spinnerOption != 0) {
                                        new BloodBankModal(getContext()).addDonor(name, regID, bloodType, contact, bleededDate);
                                    } else {
                                        alertDialogBlood.show();
                                    }
                                } else {
                                    alertDialogDate.show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Invalid contac#", Toast.LENGTH_SHORT).show();
                            }
                        } else Toast.makeText(getContext(), "Invalid registration id", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        ImageView IV_openBloodSpinner = (ImageView) view.findViewById(R.id.IV_openBloodSpinner);
        IV_openBloodSpinner.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IV_openBloodSpinner: {
                SPbloodTypes.performClick();
                break;
            }
        }
    }
}
