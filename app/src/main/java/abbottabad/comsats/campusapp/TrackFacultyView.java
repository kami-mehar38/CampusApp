package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/15/16.
 */
public class TrackFacultyView extends Activity {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private SwipeRefreshLayout SRL_facultyStatus;
    private TextView TV_myStatus;
    private String TEACHER_ID;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private String status;
    public static List<StatusInfo> statusInfoList;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_faculty_page);
        setUpCollapsingToolbar();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RV_facultyStatus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        TV_myStatus = (TextView) findViewById(R.id.TV_myStatus);
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        String[] STATUS_LIST = new String[]{
                "Available",
                "Busy",
                "On Leave"};

        int selectedStatus = sharedPreferences.getInt("SELECTED_STATUS", 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your status!");
        builder.setSingleChoiceItems(STATUS_LIST, selectedStatus, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor = sharedPreferences.edit();
                editor.putInt("SELECTED_STATUS", which);
                editor.apply();
                TEACHER_ID = sharedPreferences.getString("REG_ID", null);
                status = alertDialog.getListView().getItemAtPosition(which).toString();

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new TrackFacultyModal(TrackFacultyView.this).updateStatus(
                        status,
                        TEACHER_ID, TV_myStatus
                );
                alertDialog.cancel();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog = builder.create();
        FloatingActionButton FAB_edit = (FloatingActionButton) findViewById(R.id.FAB_edit);
        FAB_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });

        SRL_facultyStatus = (SwipeRefreshLayout) findViewById(R.id.SRL_facultyStatus);

        if (APPLICATION_STATUS.equals("TEACHER")) {
            TEACHER_ID = sharedPreferences.getString("REG_ID", null);
            Toast.makeText(this, TEACHER_ID, Toast.LENGTH_LONG).show();
            new TrackFacultyModal(this).retrieveStatus(recyclerView, SRL_facultyStatus, TV_myStatus, TEACHER_ID);
        }else if (APPLICATION_STATUS.equals("BLOOD_BANK") || APPLICATION_STATUS.equals("STUDENT")) {
            FAB_edit.setVisibility(View.GONE);
            TV_myStatus.setVisibility(View.GONE);
            new TrackFacultyModal(this).retrieveStatus(recyclerView, SRL_facultyStatus, TV_myStatus, "ALL");
        }
        SRL_facultyStatus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (APPLICATION_STATUS.equals("TEACHER")) {
                    new TrackFacultyModal(TrackFacultyView.this).
                            retrieveStatus(recyclerView, SRL_facultyStatus, TV_myStatus, TEACHER_ID);
                }else if (APPLICATION_STATUS.equals("BLOOD_BANK") || APPLICATION_STATUS.equals("STUDENT")){
                    new TrackFacultyModal(TrackFacultyView.this).retrieveStatus(recyclerView, SRL_facultyStatus, TV_myStatus, "ALL");
                }
            }
        });

        final EditText ET_trackFaculty = (EditText) findViewById(R.id.ET_trackFaculty);
        ET_trackFaculty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final List<StatusInfo> filteredList = new ArrayList<>();
                String text = s.toString().toLowerCase().trim();
                if (statusInfoList != null) {
                    if (text.isEmpty()) {
                        StatusViewAdapter statusViewAdapter = new StatusViewAdapter(statusInfoList);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(statusViewAdapter);
                        statusViewAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < statusInfoList.size(); i++) {
                            StatusInfo statusInfo = statusInfoList.get(i);
                            String teacherName = statusInfo.getTeacherName().toLowerCase();
                            if (teacherName.contains(text)) {
                                filteredList.add(statusInfoList.get(i));
                            }
                        }
                        StatusViewAdapter statusViewAdapter = new StatusViewAdapter(filteredList);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(statusViewAdapter);
                        statusViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpCollapsingToolbar() {
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Track Faculty");
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#4d9c2d"));
        collapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor("#4d9c2d"));
    }

}
