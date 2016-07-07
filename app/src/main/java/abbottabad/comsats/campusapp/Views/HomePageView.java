package abbottabad.comsats.campusapp.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import abbottabad.comsats.campusapp.R;

/**
 * Created by Kamran Ramzan on 6/2/16.
 */
public class HomePageView extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");

        CardView cv_bloodBank = (CardView) findViewById(R.id.CV_bloodBank);
        if (APPLICATION_STATUS.equals("STUDENT")){
            if (cv_bloodBank != null) {
                cv_bloodBank.setVisibility(View.VISIBLE);
                cv_bloodBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomePageView.this, BloodBankView.class));
                    }
                });
            }
        }
        else if (APPLICATION_STATUS.equals("BLOOD_BANK")){
            if (cv_bloodBank != null) {
                cv_bloodBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(HomePageView.this, "Blood Bank", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomePageView.this, BloodBankView.class));
                    }
                });
            }
        }

    }
}
