package abbottabad.comsats.campusapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/4/16.
 */

public class BloodRequestsFragment extends Fragment {


    private BloodBankLocalModal bloodBankLocalModal;
    public static RecyclerView RV_bloodRequests;
    public static RequestsViewAdapter requestsViewAdapter;

    public BloodRequestsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloodBankLocalModal = new BloodBankLocalModal(getContext());

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
        RV_bloodRequests.addItemDecoration(new RecyclerViewDivider(getContext()));
        RV_bloodRequests.setItemAnimator(new SlideInLeftAnimator());
        requestsViewAdapter = new RequestsViewAdapter(getContext());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(requestsViewAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        RV_bloodRequests.setAdapter(scaleInAnimationAdapter);
        bloodBankLocalModal.viewBloodRequests();
    }
}