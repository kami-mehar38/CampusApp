package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/15/16.
 */
public class TrackFacultyView extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public static SwipeRefreshLayout SRL_facultyStatus;
    public static TextView TV_myStatus;
    private String TEACHER_ID;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private String status;
    public static List<StatusInfo> statusInfoList;
    private LinearLayoutManager layoutManager;
    public static ImageView imageView;
    private Spinner SPstatus;
    public static RecyclerView recyclerView;
    private LinearLayout LL_autoReset;
    private CheckBox CB_available;
    private CheckBox CB_busy;
    private CheckBox CB_onLeave;
    private SimpleDateFormat dateFormat;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_faculty_page);
        setStatusBarColor();
        setUpCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.RV_facultyStatus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDivider(this));

        imageView = (ImageView) findViewById(R.id.timetable);

        TV_myStatus = (TextView) findViewById(R.id.TV_myStatus);
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        String[] STATUS_LIST = new String[]{
                "Available",
                "Busy",
                "On Leave"};

        int selectedStatus = sharedPreferences.getInt("SELECTED_STATUS", 0);
        editor = sharedPreferences.edit();

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your status");
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
        */

        View view = LayoutInflater.from(this).inflate(R.layout.track_faculty_settings, null);
        LL_autoReset = (LinearLayout) view.findViewById(R.id.LL_autoReset);
        LL_autoReset.setVisibility(View.GONE);
        Switch SW_autoReset = (Switch) view.findViewById(R.id.SW_autoReset);
        SW_autoReset.setChecked(false);
        SW_autoReset.setOnCheckedChangeListener(this);

        CB_available = (CheckBox) view.findViewById(R.id.CB_available);
        CB_available.setOnClickListener(this);
        CB_busy = (CheckBox) view.findViewById(R.id.CB_busy);
        CB_busy.setOnClickListener(this);
        CB_onLeave = (CheckBox) view.findViewById(R.id.CB_onLeave);
        CB_onLeave.setOnClickListener(this);

        if (selectedStatus == 0) {
            CB_available.setChecked(true);
        } else if (selectedStatus == 1) {
            CB_busy.setChecked(true);
        } else if (selectedStatus == 2) {
            CB_onLeave.setChecked(true);
        }

        SPstatus = (Spinner) view.findViewById(R.id.SPstatus);
        SPstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("RESET_OPTION", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        populateSpinner(SPstatus);
        SPstatus.setSelection(sharedPreferences.getInt("RESET_OPTION", 0));

        TextView TV_dateTime = (TextView) view.findViewById(R.id.TV_dateTime);
        TV_dateTime.setOnClickListener(this);

        ImageView IV_openStatusSpinner = (ImageView) view.findViewById(R.id.IV_openStatusSpinner);
        if (IV_openStatusSpinner != null) {
            IV_openStatusSpinner.setOnClickListener(this);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alertDialog = builder.create();

        FloatingActionButton FAB_edit = (FloatingActionButton) findViewById(R.id.FAB_edit);
        FAB_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });


        if (APPLICATION_STATUS.equals("TEACHER")) {
            TEACHER_ID = sharedPreferences.getString("REG_ID", null);
            Toast.makeText(this, TEACHER_ID, Toast.LENGTH_LONG).show();
            new TrackFacultyModal(this).retrieveStatus(TEACHER_ID);
        } else if (APPLICATION_STATUS.equals("BLOOD_BANK") ||
                APPLICATION_STATUS.equals("STUDENT") ||
                APPLICATION_STATUS.equals("FOOD")) {
            FAB_edit.setVisibility(View.GONE);
            TV_myStatus.setVisibility(View.GONE);
            new TrackFacultyModal(this).retrieveStatus("ALL");
        }
        SRL_facultyStatus = (SwipeRefreshLayout) findViewById(R.id.SRL_facultyStatus);
        SRL_facultyStatus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (APPLICATION_STATUS.equals("TEACHER")) {
                    new TrackFacultyModal(TrackFacultyView.this).
                            retrieveStatus(TEACHER_ID);
                } else if (APPLICATION_STATUS.equals("BLOOD_BANK")
                        || APPLICATION_STATUS.equals("STUDENT") ||
                        APPLICATION_STATUS.equals("FOOD")) {
                    new TrackFacultyModal(TrackFacultyView.this).retrieveStatus("ALL");
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

    void showdateTimePicker() {
        // Initialize
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Time-Date",
                "OK",
                "Cancel"
        );

        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            currentDate = dateFormat.parse(String.valueOf(calendar.get(Calendar.YEAR))
                    + "/" + String.valueOf(calendar.get(Calendar.MONTH))
                    +"/"+ String.valueOf(calendar.get(Calendar.DATE)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Assign values
        dateTimeDialogFragment.startAtTimeView();
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setDefaultHourOfDay(calendar.get(Calendar.HOUR));
        dateTimeDialogFragment.setDefaultMinute(calendar.get(Calendar.MINUTE));
        dateTimeDialogFragment.setDefaultDay(calendar.get(Calendar.DATE));
        dateTimeDialogFragment.setDefaultMonth(calendar.get(Calendar.MONTH));
        dateTimeDialogFragment.setDefaultYear(calendar.get(Calendar.YEAR));

        // Define new day and month format
        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e("TAG", e.getMessage());
        }

        // Set listener
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                Toast.makeText(TrackFacultyView.this, String.valueOf(date.getMonth()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });

        // Show

        dateTimeDialogFragment.show(this.getSupportFragmentManager(), "dialog_time");
    }

    void populateSpinner(Spinner SPloginOptions) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(TrackFacultyView.this,
                R.array.status_options,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to Spinner
        SPloginOptions.setAdapter(spinnerAdapter);
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

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.trackFacultyStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_openStatusSpinner: {
                SPstatus.performClick();
                break;
            }
            case R.id.CB_available: {
                if (CB_busy.isChecked())
                    CB_busy.setChecked(false);
                if (CB_onLeave.isChecked())
                    CB_onLeave.setChecked(false);
                editor.putInt("SELECTED_STATUS", 0);
                editor.apply();
                break;
            }
            case R.id.CB_busy: {
                if (CB_available.isChecked())
                    CB_available.setChecked(false);
                if (CB_onLeave.isChecked())
                    CB_onLeave.setChecked(false);
                editor.putInt("SELECTED_STATUS", 1);
                editor.apply();
                break;
            }
            case R.id.CB_onLeave: {
                if (CB_busy.isChecked())
                    CB_busy.setChecked(false);
                if (CB_available.isChecked())
                    CB_available.setChecked(false);
                editor.putInt("SELECTED_STATUS", 2);
                editor.apply();
                break;
            }
            case R.id.TV_dateTime: {
                showdateTimePicker();
                break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.SW_autoReset: {
                if (!isChecked) {
                    LL_autoReset.setVisibility(View.GONE);
                } else {
                    LL_autoReset.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }
}
