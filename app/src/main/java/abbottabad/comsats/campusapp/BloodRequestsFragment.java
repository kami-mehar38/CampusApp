package abbottabad.comsats.campusapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodRequestsFragment extends Fragment{

    private Context context;
    private ListView LV_requests;
    private BloodBankModal bloodBankModal;

    public BloodRequestsFragment() {
    }

    public BloodRequestsFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloodBankModal = new BloodBankModal(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_blood_requests, container, false);
    }

    // This event is triggered soon after onCreateView().

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Setup any handles to view objects here

        super.onViewCreated(view, savedInstanceState);
        LV_requests = (ListView) view.findViewById(R.id.LV_requests);
        //String[] data = {"1", "2", "3"};

        viewRequests();

        LV_requests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selection = LV_requests.getItemAtPosition(position).toString().trim();
                String[] stdSelected = selection.split("  ");

                bloodBankModal.viewSelectedRequest(stdSelected[1].trim());
            }
        });
    }

    public void viewRequests(){
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                bloodBankModal.viewBloodRequests());
        LV_requests.setAdapter(arrayAdapter);
    }

}