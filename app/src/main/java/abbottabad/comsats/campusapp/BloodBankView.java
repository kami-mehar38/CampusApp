package abbottabad.comsats.campusapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodBankView extends AppCompatActivity implements ActionBar.OnNavigationListener, android.support.v7.app.ActionBar.OnNavigationListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private ViewPager viewPagerAdmin;



    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;

    // Navigation adapter
    private TitleNavigationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodbank_page_admin);



        setActionBarLogo();
        // Spinner title navigation data
        navSpinner = new ArrayList<SpinnerNavItem>();
        navSpinner.add(new SpinnerNavItem("All"));
        navSpinner.add(new SpinnerNavItem("O +ve"));
        navSpinner.add(new SpinnerNavItem("O -ve"));
        navSpinner.add(new SpinnerNavItem("A +ve"));
        navSpinner.add(new SpinnerNavItem("A -ve"));
        navSpinner.add(new SpinnerNavItem("B +ve"));
        navSpinner.add(new SpinnerNavItem("B -ve"));
        navSpinner.add(new SpinnerNavItem("AB +ve"));
        navSpinner.add(new SpinnerNavItem("AB -ve"));

        // title drop down adapter
        adapter = new TitleNavigationAdapter(getApplicationContext(), navSpinner);
        getSupportActionBar().setListNavigationCallbacks(adapter, this);

        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");

        if (APPLICATION_STATUS.equals("STUDENT")){
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
            new BloodBankController(this).setupViewPagerRequester(viewPager, getSupportFragmentManager());
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);
            }
        }else if (APPLICATION_STATUS.equals("BLOOD_BANK")){

            viewPagerAdmin = (ViewPager) findViewById(R.id.viewPager);
            new BloodBankController(this).setupViewPagerAdmin(viewPagerAdmin, getSupportFragmentManager());
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPagerAdmin);
            }

        }
    }

    private void setActionBarLogo() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setIcon(R.drawable.comsats_logo);
        getSupportActionBar().setElevation(0);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        switch (itemPosition){
            case 1:{
                break;
            }
        }
        return true;
    }
}
