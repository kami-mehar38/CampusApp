package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodBankView extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private ViewPager viewPagerAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodbank_page_admin);

        setActionBarLogo();

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

            new BloodBankModal(this);
        }
    }

    private void setActionBarLogo() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.comsats_logo);
        getSupportActionBar().setElevation(0);
    }

}
