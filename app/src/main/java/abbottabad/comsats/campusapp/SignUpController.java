package abbottabad.comsats.campusapp;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/1/16.
 */
class SignUpController {

    private Context context;
    SignUpController(Context context) {
        this.context = context;
    }


    void populateSpinner(Spinner SPloginOptions) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.signup_options,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to Spinner
        SPloginOptions.setAdapter(spinnerAdapter);
    }

}
