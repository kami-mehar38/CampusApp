package abbottabad.comsats.campusapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * This project CampusApp is created by Kamran Ramzan on 9/1/16.
 */
public class TimeTableView extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private RelativeLayout slot11, slot21, slot31, slot41, slot51;
    private AlertDialog AD_editSlot;
    private EditText ET_editSlotSubject;
    private EditText ET_editSlotTeacher;
    private Validation validation;
    private int SELECTED_SLOT;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_page);
        setStatusBarColor();
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initializingVariables();
    }

    private void initializingVariables() {
        View view = LayoutInflater.from(TimeTableView.this).inflate(R.layout.edit_slot, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(TimeTableView.this);
        builder.setView(view);
        builder.setCancelable(false);
        AD_editSlot = builder.create();

        ET_editSlotSubject = (EditText) view.findViewById(R.id.ET_editSlotSubject);
        ET_editSlotTeacher = (EditText) view.findViewById(R.id.ET_editSlotTeacher);
        Button btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        Button btn_cancelEditDialog = (Button) view.findViewById(R.id.btn_cancelEditDialog);
        btn_cancelEditDialog.setOnClickListener(this);


        slot11 = (RelativeLayout) findViewById(R.id.slot11);
        if (slot11 != null) {
            String subjectName = sharedPreferences.getString("SLOT11_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT11_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot11.getChildAt(0)).setText(subjectName);
                ((TextView) slot11.getChildAt(1)).setText(teacherName);
            }
            slot11.setOnLongClickListener(this);
        }
        slot21 = (RelativeLayout) findViewById(R.id.slot21);
        if (slot21 != null) {
            slot21.setOnLongClickListener(this);
        }
        slot31 = (RelativeLayout) findViewById(R.id.slot31);
        if (slot31 != null) {
            slot31.setOnLongClickListener(this);
        }
        slot41 = (RelativeLayout) findViewById(R.id.slot41);
        if (slot41 != null) {
            slot41.setOnLongClickListener(this);
        }
        slot51 = (RelativeLayout) findViewById(R.id.slot51);
        if (slot51 != null) {
            slot51.setOnLongClickListener(this);
        }
        CheckBox slot11Reminder = (CheckBox) findViewById(R.id.slot11Reminder);
        if (slot11Reminder != null) {
            slot11Reminder.setChecked(sharedPreferences.getBoolean("SLOT11_CHECKED", false));
            slot11Reminder.setOnClickListener(this);
        }
        CheckBox slot21Reminder = (CheckBox) findViewById(R.id.slot21Reminder);
        if (slot21Reminder != null) {
            slot21Reminder.setOnClickListener(this);
        }
        CheckBox slot31Reminder = (CheckBox) findViewById(R.id.slot31Reminder);
        if (slot31Reminder != null) {
            slot31Reminder.setOnClickListener(this);
        }
        CheckBox slot41Reminder = (CheckBox) findViewById(R.id.slot41Reminder);
        if (slot41Reminder != null) {
            slot41Reminder.setOnClickListener(this);
        }
        CheckBox slot51Reminder = (CheckBox) findViewById(R.id.slot51Reminder);
        if (slot51Reminder != null) {
            slot51Reminder.setOnClickListener(this);
        }

        validation = new Validation();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.slot11: {
                SELECTED_SLOT = 11;
                AD_editSlot.show();
                break;
            }
            case R.id.slot21: {
                SELECTED_SLOT = 21;
                AD_editSlot.show();
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit: {

                String subjectName = ET_editSlotSubject.getText().toString().trim();
                String teacherName = ET_editSlotTeacher.getText().toString().trim();
                if (validation.validateSubjectName(subjectName)) {
                    if (validation.validateTeacherName(teacherName)) {
                        View subject = null;
                        View teacher = null;
                        switch (SELECTED_SLOT) {
                            case 11: {
                                subject = slot11.getChildAt(0);
                                teacher = slot11.getChildAt(1);
                                editor.putString("SLOT11_SUBJECT", subjectName);
                                editor.putString("SLOT11_TEACHER", teacherName);
                                editor.apply();
                                break;
                            }
                            case 21: {
                                subject = slot21.getChildAt(0);
                                teacher = slot21.getChildAt(1);
                                break;
                            }
                        }
                        if (subject != null) {
                            ((TextView) subject).setText(subjectName);
                        }
                        ET_editSlotSubject.setText("");
                        if (teacher != null) {
                            ((TextView) teacher).setText(teacherName);
                        }
                        ET_editSlotTeacher.setText("");
                        Toast.makeText(TimeTableView.this, "Slot edited", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(TimeTableView.this, "Invalid teacher name", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(TimeTableView.this, "Invalid subject name", Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.btn_cancelEditDialog: {
                AD_editSlot.cancel();
                ET_editSlotSubject.setText("");
                break;
            }
            case R.id.slot11Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(1, subject, teacher, 1, 7, 45, 0);
                        editor.putBoolean("SLOT11_CHECKED", true);
                        editor.apply();
                    }
                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(1);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT11_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot21Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(1, subject, teacher, 2, 7, 45, 0);
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(1);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void cancelSlotNotification(int id) {
        Intent alarmIntent = new Intent(this, TimeTableReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private void handleNotification(int id, String subject, String teacher, int day, int hours, int minutes, int seconds) {
        Toast.makeText(TimeTableView.this, "HERE", Toast.LENGTH_SHORT).show();
        Intent alarmIntent = new Intent(this, TimeTableReceiver.class);
        alarmIntent.putExtra("SUBJECT", subject);
        alarmIntent.putExtra("TEACHER", teacher);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        switch (day) {
            case 1: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            }
            case 2: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        //check whether the time is earlier than current time. If so, set it to next week.

        Calendar now = Calendar.getInstance();
        if (calendar.before(now)) {
            calendar.add(Calendar.DATE, 7);
            Toast.makeText(TimeTableView.this, "Past", Toast.LENGTH_SHORT).show();

        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.timetableStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
