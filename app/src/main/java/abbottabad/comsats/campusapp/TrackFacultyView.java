package abbottabad.comsats.campusapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.andexert.library.RippleView;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.DateFormat;
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

public class TrackFacultyView extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RippleView.OnRippleCompleteListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public static SwipeRefreshLayout SRL_facultyStatus;
    public static TextView TV_myStatus;
    private String TEACHER_ID;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private AlertDialog alertDialog;
    private String status;
    private String teacherStatus;
    public static List<StatusInfo> statusInfoList;
    private LinearLayoutManager layoutManager;
    private Spinner SPstatus;
    public static RecyclerView recyclerView;
    private LinearLayout LL_autoReset;
    private CheckBox CB_available;
    private CheckBox CB_busy;
    private CheckBox CB_onLeave;
    private int selectedStatus;
    private TextView TV_dateTime;
    private Calendar calendar;
    private Switch SW_autoReset;
    private AlertDialog AD_autoReset;

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

        TV_myStatus = (TextView) findViewById(R.id.TV_myStatus);
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        TV_myStatus.setText(sharedPreferences.getString("CURRENT_STATUS", "Available"));
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");

        TEACHER_ID = sharedPreferences.getString("REG_ID", null);

        View view = LayoutInflater.from(this).inflate(R.layout.track_faculty_settings, null);
        LL_autoReset = (LinearLayout) view.findViewById(R.id.LL_autoReset);
        SW_autoReset = (Switch) view.findViewById(R.id.SW_autoReset);
        SW_autoReset.setOnCheckedChangeListener(this);


        CB_available = (CheckBox) view.findViewById(R.id.CB_available);
        CB_available.setOnClickListener(this);
        CB_busy = (CheckBox) view.findViewById(R.id.CB_busy);
        CB_busy.setOnClickListener(this);
        CB_onLeave = (CheckBox) view.findViewById(R.id.CB_onLeave);
        CB_onLeave.setOnClickListener(this);

        SPstatus = (Spinner) view.findViewById(R.id.SPstatus);
        populateSpinner(SPstatus);
        SPstatus.setSelection(sharedPreferences.getInt("RESET_OPTION", 0));
        SPstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor = sharedPreferences.edit();
                editor.putInt("RESET_OPTION", position);
                editor.apply();
                status = SPstatus.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RippleView BTN_ok = (RippleView) view.findViewById(R.id.BTN_ok);
        BTN_ok.setOnRippleCompleteListener(this);

        TV_dateTime = (TextView) view.findViewById(R.id.TV_dateTime);
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
                if (sharedPreferences.getBoolean("IS_AUTO_RESET", false)) {
                    SW_autoReset.setChecked(true);
                    LL_autoReset.setVisibility(View.VISIBLE);
                } else {
                    SW_autoReset.setChecked(false);
                    LL_autoReset.setVisibility(View.GONE);
                }
                selectedStatus = sharedPreferences.getInt("SELECTED_STATUS", 0);
                if (selectedStatus == 0) {
                    CB_available.setChecked(true);
                    CB_busy.setChecked(false);
                    CB_onLeave.setChecked(false);
                } else if (selectedStatus == 1) {
                    CB_busy.setChecked(true);
                    CB_available.setChecked(false);
                    CB_onLeave.setChecked(false);
                } else if (selectedStatus == 2) {
                    CB_onLeave.setChecked(true);
                    CB_available.setChecked(false);
                    CB_busy.setChecked(false);
                }
            }
        });

        if (APPLICATION_STATUS.equals("TEACHER")) {
            TEACHER_ID = sharedPreferences.getString("REG_ID", null);
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
        calendar = Calendar.getInstance();


        // Assign values
        dateTimeDialogFragment.startAtTimeView();
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setDefaultHourOfDay(calendar.get(Calendar.HOUR_OF_DAY));
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
                DateFormat df = new SimpleDateFormat("d MMM yyyy/h:mm a", Locale.getDefault());
                String currentDateTimeString = df.format(Calendar.getInstance().getTime());

                calendar.setTime(date);
                String resetDateTimeString = df.format(calendar.getTime());
                String[] splitted = resetDateTimeString.split("/");
                String time = null;
                try {
                    if (df.parse(resetDateTimeString).after(df.parse(currentDateTimeString))) {
                        time = "Today at " + splitted[1];
                    } else {
                        time = "Tommorrow at " + splitted[1];
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (time != null)
                    TV_dateTime.setText(time);
                setAutoReset(1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("IS_AUTO_RESET", true);
                editor.putString("RESET_TIME", time);
                editor.apply();
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });

        // Show
        dateTimeDialogFragment.show(this.getSupportFragmentManager(), "dialog_time");
    }

    private void setAutoReset(int id, int year, int month, int date, int hours, int minutes, int seconds) {
        Intent alarmIntent = new Intent(TrackFacultyView.this, StatusResetReceiver.class);
        alarmIntent.putExtra("STATUS", status);
        alarmIntent.putExtra("TEACHER_ID", TEACHER_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TrackFacultyView.this, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hours, minutes, seconds);

        //check whether the time is earlier than current time. If so, set it to next day.
        Calendar now = Calendar.getInstance();
        if (calendar.before(now)) {
            calendar.add(Calendar.DATE, 1);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAutoReset() {
        Intent alarmIntent = new Intent(this, StatusResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Toast.makeText(TrackFacultyView.this, "Auto reset off", Toast.LENGTH_SHORT).show();
        }
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
                teacherStatus = "Available";
                break;
            }
            case R.id.CB_busy: {
                if (CB_available.isChecked())
                    CB_available.setChecked(false);
                if (CB_onLeave.isChecked())
                    CB_onLeave.setChecked(false);
                teacherStatus = "Busy";
                break;
            }
            case R.id.CB_onLeave: {
                if (CB_busy.isChecked())
                    CB_busy.setChecked(false);
                if (CB_available.isChecked())
                    CB_available.setChecked(false);
                teacherStatus = "On Leave";
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
                    buttonView.setTextColor(Color.parseColor("#7f7f7f"));
                    LL_autoReset.setVisibility(View.GONE);
                    if (sharedPreferences.getBoolean("IS_AUTO_RESET", false)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrackFacultyView.this);
                        builder.setMessage("Turn auto reset off?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("IS_AUTO_RESET", false);
                                editor.putString("RESET_TIME", "Set time");
                                editor.apply();
                                cancelAutoReset();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AD_autoReset.cancel();
                                SW_autoReset.setChecked(true);
                            }
                        });
                        AD_autoReset = builder.create();
                        AD_autoReset.show();
                    }
                } else {
                    buttonView.setTextColor(Color.parseColor("#000000"));
                    TV_dateTime.setText(sharedPreferences.getString("RESET_TIME", "Set time"));
                    LL_autoReset.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()) {
            case R.id.BTN_ok: {
                alertDialog.cancel();
                if (teacherStatus != null) {
                    if (!sharedPreferences.getString("CURRENT_STATUS", "Available").equals(teacherStatus))
                        new TrackFacultyModal(TrackFacultyView.this).updateStatus(teacherStatus, TEACHER_ID);
                }
                break;
            }
        }
    }
}
