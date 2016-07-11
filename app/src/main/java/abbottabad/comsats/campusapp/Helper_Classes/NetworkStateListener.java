package abbottabad.comsats.campusapp.helper_classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Kamran Ramzan on 6/12/16.
 */
public class NetworkStateListener extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected){
            Toast.makeText(context, "No internet connection.", Toast.LENGTH_LONG).show();
        }
    }
}
