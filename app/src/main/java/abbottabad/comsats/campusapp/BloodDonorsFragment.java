package abbottabad.comsats.campusapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/4/16.
 */
public class BloodDonorsFragment extends Fragment {


    public static RecyclerView recyclerView;

    public BloodDonorsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_donors, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser) {
            new BloodBankModal(getContext()).retrieveDonors("All");
            BloodBankView.toolbarSpinner.setVisibility(View.VISIBLE);
            BloodBankView.toolbarSpinner.setSelection(0);
        } else {
            BloodBankView.toolbarSpinner.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }
}