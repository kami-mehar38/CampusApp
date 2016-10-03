package abbottabad.comsats.campusapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * This project CampusApp is created by Kamran Ramzan on 03-Oct-16.
 */

public class BloodRequestResponseFragment extends Fragment {

    public static RecyclerView recyclerView;
    private BloodBankResponseModal bloodBankResponseModal;
    public static ResponeViewAdapter responeViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloodBankResponseModal = new BloodBankResponseModal(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blood_request_response, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDivider(getContext()));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        responeViewAdapter = new ResponeViewAdapter();
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(responeViewAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(scaleInAnimationAdapter);
        bloodBankResponseModal.viewBloodRequestResponses();
    }

}
