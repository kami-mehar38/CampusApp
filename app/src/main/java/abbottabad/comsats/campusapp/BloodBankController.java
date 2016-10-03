package abbottabad.comsats.campusapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/4/16.
 */
class BloodBankController {

    private static String stdName;
    private static String stdReg;
    private static String stdContact;
    private static String bloodType;

    private Context context;

    static String getBloodType() {
        return bloodType;
    }

    static void setBloodType(String bloodType) {
        BloodBankController.bloodType = bloodType;
    }

    static String getStdContact() {
        return stdContact;
    }

    static void setStdContact(String stdContact) {
        BloodBankController.stdContact = stdContact;
    }

    static String getStdReg() {
        return stdReg;
    }

    static void setStdReg(String stdReg) {
        BloodBankController.stdReg = stdReg;
    }

    static String getStdName() {
        return stdName;
    }

    static void setStdName(String stdName) {
        BloodBankController.stdName = stdName;
    }

    BloodBankController(Context context) {
        this.context = context;
    }

    void populateSpinner(Spinner SPbloodTypeOptions) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.bloodGroup_options,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to Spinner
        SPbloodTypeOptions.setAdapter(spinnerAdapter);
    }

    void populateToolbarSpinner(Spinner SPbloodTypeOptions) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.toolbar_bloodGroup_options,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to Spinner
        SPbloodTypeOptions.setAdapter(spinnerAdapter);
    }

    void setupViewPagerAdmin(ViewPager viewPagerAdmin, FragmentManager supportFragmentManager) {
        ViewPagerAdapterAdmin viewPagerAdapterAdmin = new ViewPagerAdapterAdmin(supportFragmentManager);
        viewPagerAdapterAdmin.addFragment(new BloodRequestsFragment(), "Requests");
        viewPagerAdapterAdmin.addFragment(new BloodDonorsFragment(), "Donors");
        viewPagerAdapterAdmin.addFragment(new BloodDonorsAddFragment(),"Add Donor");
       viewPagerAdmin.setAdapter(viewPagerAdapterAdmin);
    }
    private class ViewPagerAdapterAdmin extends FragmentPagerAdapter {

        private final List<Fragment> fragmentsList = new ArrayList<>();
        private final List<String> titlesList = new ArrayList<>();

        ViewPagerAdapterAdmin(FragmentManager fm) {
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
        void addFragment(Fragment fragment, String title){
            fragmentsList.add(fragment);
            titlesList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlesList.get(position);
        }
    }

    void setupViewPagerRequester(ViewPager viewPager, FragmentManager supportFragmentManager) {
        ViewPagerAdapterRequester viewPagerAdapterRequester = new ViewPagerAdapterRequester(supportFragmentManager);
        viewPagerAdapterRequester.addFragment(new BloodRequestsFragment(), "Requests");
        viewPagerAdapterRequester.addFragment(new BloodRequestsSendFragment(), "Send Request");
        viewPagerAdapterRequester.addFragment(new BloodRequestResponseFragment(), "Responses");
        viewPagerAdapterRequester.addFragment(new BloodDonorsAddFragment(),"Become a Donor");
        viewPager.setAdapter(viewPagerAdapterRequester);
    }
    private class ViewPagerAdapterRequester extends FragmentPagerAdapter {

        private final List<Fragment> fragmentsList = new ArrayList<>();
        private final List<String> titlesList = new ArrayList<>();

        ViewPagerAdapterRequester(FragmentManager fm) {
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

        void addFragment(Fragment fragment, String title){
            fragmentsList.add(fragment);
            titlesList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlesList.get(position);
        }
    }

}
