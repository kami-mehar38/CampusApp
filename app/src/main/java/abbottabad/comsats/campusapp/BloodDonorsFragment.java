package abbottabad.comsats.campusapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodDonorsFragment extends Fragment{

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

        List<DonorsInfo> donorsInfoList = new ArrayList<>();
        DonorsInfo donorsInfo1, donorsInfo2;
        donorsInfo1 = new DonorsInfo();
        donorsInfo1.setName("Kamran Ramzan");
        donorsInfo1.setBloodType("B+");
        donorsInfo1.setContact("03450578052");

        donorsInfo2 = new DonorsInfo();
        donorsInfo2.setName("M.Zeeshan");
        donorsInfo2.setBloodType("O+");
        donorsInfo2.setContact("03450578052");

        donorsInfoList.add(donorsInfo1);
        donorsInfoList.add(donorsInfo2);
        recyclerView.setAdapter(new DonorsViewAdapter(donorsInfoList));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            new BloodBankModal(context).retrieveDonors();
        }
    }
}