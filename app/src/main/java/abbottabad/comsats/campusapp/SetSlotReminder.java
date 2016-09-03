package abbottabad.comsats.campusapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

/**
 * This project CampusApp is created by Kamran Ramzan on 9/3/16.
 */
public class SetSlotReminder extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public SetSlotReminder(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
