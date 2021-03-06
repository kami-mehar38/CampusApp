package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

/**
 * This project CampusApp is created by Kamran Ramzan on 6/11/16.
 */
class InitialPageController extends AsyncTask<Void, Void, String> {
    private ProgressDialog progressDialog;
    private Context context;
    private AlertDialog alertDialog;

    InitialPageController(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Checking internet connection");
        progressDialog.show();

    }

    @Override
    protected String doInBackground(Void... params) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            return "true";
        }
        else return "false";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.cancel();
        if (s.equals("false")){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setTitle("No internet connection");
            builder.setMessage("Please check your internet connection is working or not.");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity)(context)).finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.cancel();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
    }
}