package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/4/16.
 */
public class BloodBankView extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public static Spinner toolbarSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodbank_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bloodbank_toolbar);
        setSupportActionBar(toolbar);

        toolbarSpinner = (Spinner) findViewById(R.id.toolbarSpinner);
        if (toolbarSpinner != null) {
            toolbarSpinner.setVisibility(View.GONE);
        }

        BloodBankController bloodBankController = new BloodBankController(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");

        if (APPLICATION_STATUS.equals("STUDENT") || APPLICATION_STATUS.equals("TEACHER")){
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
            bloodBankController.setupViewPagerRequester(viewPager, getSupportFragmentManager());
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
            }
        }else if (APPLICATION_STATUS.equals("BLOOD_BANK")){
            bloodBankController.populateToolbarSpinner(toolbarSpinner);
            ViewPager viewPagerAdmin = (ViewPager) findViewById(R.id.viewPager);
            bloodBankController.setupViewPagerAdmin(viewPagerAdmin, getSupportFragmentManager());
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPagerAdmin);
                tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
            }
        }
    }
}
