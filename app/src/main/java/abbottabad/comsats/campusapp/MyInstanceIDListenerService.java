package abbottabad.comsats.campusapp;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/2/16.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        startService(new Intent(this, RegistrationIntentService.class));
    }
}
