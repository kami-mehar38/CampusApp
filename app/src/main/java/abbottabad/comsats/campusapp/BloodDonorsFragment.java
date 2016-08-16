package abbottabad.comsats.campusapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


import abbottabad.comsats.campusapp.DividerItemDecoration;
import abbottabad.comsats.campusapp.DonorsInfo;
import abbottabad.comsats.campusapp.BloodBankModal;
import abbottabad.comsats.campusapp.R;
import abbottabad.comsats.campusapp.SpinnerNavItem;
import abbottabad.comsats.campusapp.TitleNavigationAdapter;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodDonorsFragment extends Fragment implements  android.support.v7.app.ActionBar.OnNavigationListener {


    private static RecyclerView recyclerView;

    public BloodDonorsFragment() {
        // Required empty public constructor
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
        navSpinner.add(new SpinnerNavItem("Select Group"));
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
        TitleNavigationAdapter adapter = new TitleNavigationAdapter(getContext(), navSpinner);
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
            new BloodBankModal(getContext()).retrieveDonors(recyclerView, "All");
        }

    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        switch (itemPosition){
            case 1: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "All");
                break;
            }
            case 2: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "O+");
                break;
            }
            case 3: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "O-");
                break;
            }
            case 4: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "A+");
                break;
            }
            case 5: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "A-");
                break;
            }
            case 6: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "B+");
                break;
            }
            case 7: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "B-");
                break;
            }
            case 8: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "AB+");
                break;
            }
            case 9: {
                new BloodBankModal(getContext()).retrieveDonors(recyclerView, "AB-");
                break;
            }
        }
        return true;
    }

    /**
     * Created by Kamran Ramzan on 6/28/16.
     */

    public static class DonorsViewAdapter extends RecyclerView.Adapter <DonorsViewAdapter.ViewHolder> {

        private List<DonorsInfo> donorsInfoList;

        public DonorsViewAdapter(List<DonorsInfo> donorsInfoList) {
            this.donorsInfoList = donorsInfoList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_donors, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DonorsInfo donorsInfo = donorsInfoList.get(position);
            holder.TV_donorName.setText(donorsInfo.getName());
            holder.TV_donorRegistration.setText(donorsInfo.getRegistration());
            holder.TV_donorBloodtype.setText(donorsInfo.getBloodType());
            holder.TV_donorContact.setText(donorsInfo.getContact());
        }

        @Override
        public int getItemCount() {
            return donorsInfoList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            private TextView TV_donorName;
            private TextView TV_donorRegistration;
            private TextView TV_donorBloodtype;
            private TextView TV_donorContact;
            private Button btnCall;
            private Button btnMessage;
            private CheckBox CB_bleed;
            private AlertDialog alertDialog;
            public ViewHolder(final View itemView) {
                super(itemView);

                TV_donorName = (TextView) itemView.findViewById(R.id.TV_donorName);
                TV_donorRegistration = (TextView) itemView.findViewById(R.id.TV_donorRegistration);
                TV_donorBloodtype = (TextView) itemView.findViewById(R.id.TV_donorBloodType);
                TV_donorContact = (TextView) itemView.findViewById(R.id.TV_donorContact);
                btnCall = (Button) itemView.findViewById(R.id.btnCall);
                btnMessage = (Button) itemView.findViewById(R.id.btnMessage);
                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL,
                                Uri.parse("tel:" + TV_donorContact.getText().toString().trim()));
                        itemView.getContext().startActivity(intent);
                    }
                });
                btnMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("sms:" + TV_donorContact.getText().toString().trim()));
                        itemView.getContext().startActivity(intent);
                    }
                });
                CB_bleed = (CheckBox) itemView.findViewById(R.id.CB_bleed);
                CB_bleed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CB_bleed.isChecked()){
                            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                            int curr_year = calendar.get(Calendar.YEAR);
                            int curr_month = calendar.get(Calendar.MONTH);
                            int curr_date = calendar.get(Calendar.DATE);
                            final String bleededDate = String.valueOf(curr_year) + "/" +
                                    String.valueOf(curr_month + 1) + "/" +
                                    String.valueOf(curr_date);
                            final String registration = TV_donorRegistration.getText().toString().trim();
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                            builder.setTitle("Alert");
                            builder.setMessage("If you are sure about the bleeding of donor then press OK, otherwise press CANCEL.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new BloodBankModal(itemView.getContext())
                                            .updateBleedingDate(registration, bleededDate);
                                    new BloodBankModal(itemView.getContext()).retrieveDonors(recyclerView, "All");
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                    CB_bleed.setChecked(false);
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
            }
        }
    }
}