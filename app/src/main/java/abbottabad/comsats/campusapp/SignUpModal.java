package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * Created by Kamran Ramzan on 6/26/16.
 */
public class SignUpModal extends AsyncTask<String, Void, String> {
    private Context context;
    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public SignUpModal(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final int TOKEN_GOT = sharedPreferences.getInt("TOKEN_GOT", 0);

        if (TOKEN_GOT != 1){
            context.startService(new Intent(context, RegistrationIntentService.class));
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SharedPreferences applicationStatus = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = applicationStatus.edit();
        editor.putString("APPLICATION_STATUS", "STUDENT");
        editor.apply();
    }
}
