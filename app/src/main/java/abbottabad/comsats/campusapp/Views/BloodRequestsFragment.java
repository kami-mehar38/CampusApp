package abbottabad.comsats.campusapp.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import abbottabad.comsats.campusapp.Helper_Classes.DividerItemDecoration;
import abbottabad.comsats.campusapp.Modals.BloodBankModal;
import abbottabad.comsats.campusapp.R;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodRequestsFragment extends Fragment{

    private Context context;

    private BloodBankModal bloodBankModal;

    private RecyclerView RV_bloodRequests;

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
        RV_bloodRequests = (RecyclerView) view.findViewById(R.id.RV_bloodRequests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_bloodRequests.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        RV_bloodRequests.addItemDecoration(itemDecoration);
        bloodBankModal.viewBloodRequests(RV_bloodRequests);
        Toast.makeText(context, "Ok it got it", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

        }
    }
}