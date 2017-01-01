package abbottabad.comsats.campusapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 01-Jan-17.
 */

class EventNotificationsController {

    void setupViewPager(ViewPager viewPager, FragmentManager supportFragmentManager) {
        EventNotificationsController.ViewPagerAdapter viewPagerAdapter = new EventNotificationsController.ViewPagerAdapter(supportFragmentManager);
        viewPagerAdapter.addFragment(new NotificationsFragment(), "Notifications");
        viewPagerAdapter.addFragment(new NotificationsGroupFragment(), "Groups");
        viewPager.setAdapter(viewPagerAdapter);
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentsList = new ArrayList<>();
        private final List<String> titlesList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
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
