package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/30/16.
 */
public class ComplaintPollView extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public static RecyclerView RV_complaints;
    public static ComplaintPollVIewAdapter complaintPollVIewAdapter;
    public static TextView TV_noComplaints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.complaintpoll_page_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.complaintpoll_toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        int complaintType = sharedPreferences.getInt("COMPLAINT_TYPE", 0);
        TextView title = (TextView) findViewById(R.id.title);
        if (complaintType == 1) {
            title.setText(R.string.pending_complaints);
        } else if (complaintType == 2) title.setText(R.string.processed_complaints);
        TV_noComplaints = (TextView) findViewById(R.id.TV_noComplaints);
        RV_complaints = (RecyclerView) findViewById(R.id.RV_complaints);
        if (RV_complaints != null) {
            RV_complaints.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_complaints.setLayoutManager(layoutManager);
            RV_complaints.addItemDecoration(new RecyclerViewDivider(this));
            complaintPollVIewAdapter = new ComplaintPollVIewAdapter(this);
            ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(complaintPollVIewAdapter);
            scaleInAnimationAdapter.setFirstOnly(false);
            RV_complaints.setAdapter(scaleInAnimationAdapter);
            new ComplaintPollLocalModal(this).retrieveComplaints(String.valueOf(complaintType));
        }
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.complaintPollStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
