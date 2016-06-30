package abbottabad.comsats.campusapp;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

/**
 * Created by Kamran Ramzan on 6/30/16.
 */
public class DonorsAddFragment extends Fragment {
    private Spinner SPbloodTypes;
    private BloodBankController bloodBankController;
    private Context context;
    private TextInputLayout TILname, TILregId, TILcontact;
    private TextInputEditText ETname, ETregId, ETcontact;

    public DonorsAddFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloodBankController = new BloodBankController(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_donor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SPbloodTypes = (Spinner) view.findViewById(R.id.SP_bloodTypes);
        bloodBankController.populateSpinner(SPbloodTypes);
        TILname = (TextInputLayout) view.findViewById(R.id.TILDname);
        TILname.setHint("Name");
        TILregId = (TextInputLayout) view.findViewById(R.id.TILDregID);
        TILregId.setHint("Registration ID");
        TILcontact = (TextInputLayout) view.findViewById(R.id.TILDcontact);
        TILcontact.setHint("Contact #");
    }
}
