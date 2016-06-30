package abbottabad.comsats.campusapp;

import android.content.Context;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;


import abbottabad.comsats.campusapp.Modals.BloodBankModal;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodDonorsFragment extends Fragment implements  android.support.v7.app.ActionBar.OnNavigationListener {


    private Context context;
    private RecyclerView recyclerView;

    public BloodDonorsFragment() {
        // Required empty public constructor
    }
    public BloodDonorsFragment(Context context){
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }

        // Spinner title navigation data
        ArrayList<SpinnerNavItem> navSpinner = new ArrayList<SpinnerNavItem>();
        navSpinner.add(new SpinnerNavItem("All"));
        navSpinner.add(new SpinnerNavItem("O +ve"));
        navSpinner.add(new SpinnerNavItem("O -ve"));
        navSpinner.add(new SpinnerNavItem("A +ve"));
        navSpinner.add(new SpinnerNavItem("A -ve"));
        navSpinner.add(new SpinnerNavItem("B +ve"));
        navSpinner.add(new SpinnerNavItem("B -ve"));
        navSpinner.add(new SpinnerNavItem("AB +ve"));
        navSpinner.add(new SpinnerNavItem("AB -ve"));

        supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        TitleNavigationAdapter adapter = new TitleNavigationAdapter(context, navSpinner);
        supportActionBar.setListNavigationCallbacks(adapter, this);
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
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser) {
            new BloodBankModal(context).retrieveDonors(recyclerView, "All");
        }

    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        switch (itemPosition){
            case 0: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "All");
                break;
            }
            case 1: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "O+");
                break;
            }
            case 2: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "O-");
                break;
            }
            case 3: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "A+");
                break;
            }
            case 4: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "A-");
                break;
            }
            case 5: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "B+");
                break;
            }
            case 6: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "B-");
                break;
            }
            case 7: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "AB+");
                break;
            }
            case 8: {
                new BloodBankModal(context).retrieveDonors(recyclerView, "AB-");
                break;
            }
        }
        return true;
    }
}