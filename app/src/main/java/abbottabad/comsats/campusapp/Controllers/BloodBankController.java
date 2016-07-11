package abbottabad.comsats.campusapp.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import abbottabad.comsats.campusapp.views.BloodDonorsFragment;
import abbottabad.comsats.campusapp.views.BloodRequestsFragment;
import abbottabad.comsats.campusapp.views.BloodRequestsSendFragment;
import abbottabad.comsats.campusapp.views.DonorsAddFragment;
import abbottabad.comsats.campusapp.R;

/**
 * Created by Kamran Ramzan on 6/4/16.
 */
public class BloodBankController {

    private static String stdName;
    private static String stdReg;
    private static String stdContact;
    private static String bloodType;
    private Context context;

    public static String getBloodType() {
        return bloodType;
    }

    public static void setBloodType(String bloodType) {
        BloodBankController.bloodType = bloodType;
    }

    public static String getStdContact() {
        return stdContact;
    }

    public static void setStdContact(String stdContact) {
        BloodBankController.stdContact = stdContact;
    }

    public static String getStdReg() {
        return stdReg;
    }

    public static void setStdReg(String stdReg) {
        BloodBankController.stdReg = stdReg;
    }

    public static String getStdName() {
        return stdName;
    }

    public static void setStdName(String stdName) {
        BloodBankController.stdName = stdName;
    }

    public BloodBankController(Context context) {
        this.context = context;
    }

    public void populateSpinner(Spinner SPbloodTypeOptions) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.bloodGroup_options,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to Spinner
        SPbloodTypeOptions.setAdapter(spinnerAdapter);
    }

    public void setupViewPagerAdmin(ViewPager viewPagerAdmin, FragmentManager supportFragmentManager) {
        ViewPagerAdapterAdmin viewPagerAdapterAdmin = new ViewPagerAdapterAdmin(supportFragmentManager);
        viewPagerAdapterAdmin.addFragment(new BloodRequestsFragment(), "Requests");
        viewPagerAdapterAdmin.addFragment(new BloodDonorsFragment(), "Donors");
        viewPagerAdapterAdmin.addFragment(new DonorsAddFragment(),"Add Donor");
       viewPagerAdmin.setAdapter(viewPagerAdapterAdmin);
    }
    private class ViewPagerAdapterAdmin extends FragmentPagerAdapter {

        private final List<Fragment> fragmentsList = new ArrayList<Fragment>();
        private final List<String> titlesList = new ArrayList<String>();

        public ViewPagerAdapterAdmin(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }
        public void addFragment(Fragment fragment, String title){
            fragmentsList.add(fragment);
            titlesList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlesList.get(position);
        }
    }

    public void setupViewPagerRequester(ViewPager viewPager, FragmentManager supportFragmentManager) {
        ViewPagerAdapterRequester viewPagerAdapterRequester = new ViewPagerAdapterRequester(supportFragmentManager);
        viewPagerAdapterRequester.addFragment(new BloodRequestsSendFragment(), "Blood Request");
        viewPagerAdapterRequester.addFragment(new DonorsAddFragment(),"Become a Donor");
        viewPager.setAdapter(viewPagerAdapterRequester);
    }
    private class ViewPagerAdapterRequester extends FragmentPagerAdapter {

        private final List<Fragment> fragmentsList = new ArrayList<Fragment>();
        private final List<String> titlesList = new ArrayList<String>();

        public ViewPagerAdapterRequester(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentsList.add(fragment);
            titlesList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlesList.get(position);
        }
    }

}
