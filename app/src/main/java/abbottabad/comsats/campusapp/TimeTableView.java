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
    private RelativeLayout slot12, slot22, slot32, slot42, slot52;
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
            String subjectName = sharedPreferences.getString("SLOT21_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT21_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot21.getChildAt(0)).setText(subjectName);
                ((TextView) slot21.getChildAt(1)).setText(teacherName);
            }
            slot21.setOnLongClickListener(this);
        }
        slot31 = (RelativeLayout) findViewById(R.id.slot31);
        if (slot31 != null) {
            String subjectName = sharedPreferences.getString("SLOT31_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT31_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot31.getChildAt(0)).setText(subjectName);
                ((TextView) slot31.getChildAt(1)).setText(teacherName);
            }
            slot31.setOnLongClickListener(this);
        }
        slot41 = (RelativeLayout) findViewById(R.id.slot41);
        if (slot41 != null) {
            String subjectName = sharedPreferences.getString("SLOT41_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT41_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot41.getChildAt(0)).setText(subjectName);
                ((TextView) slot41.getChildAt(1)).setText(teacherName);
            }
            slot41.setOnLongClickListener(this);
        }
        slot51 = (RelativeLayout) findViewById(R.id.slot51);
        if (slot51 != null) {
            String subjectName = sharedPreferences.getString("SLOT51_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT51_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot51.getChildAt(0)).setText(subjectName);
                ((TextView) slot51.getChildAt(1)).setText(teacherName);
            }
            slot51.setOnLongClickListener(this);
        }
        slot12 = (RelativeLayout) findViewById(R.id.slot12);
        if (slot12 != null) {
            String subjectName = sharedPreferences.getString("SLOT12_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT512_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot12.getChildAt(0)).setText(subjectName);
                ((TextView) slot12.getChildAt(1)).setText(teacherName);
            }
            slot12.setOnLongClickListener(this);
        }
        slot22 = (RelativeLayout) findViewById(R.id.slot22);
        if (slot22 != null) {
            String subjectName = sharedPreferences.getString("SLOT22_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT22_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot22.getChildAt(0)).setText(subjectName);
                ((TextView) slot22.getChildAt(1)).setText(teacherName);
            }
            slot22.setOnLongClickListener(this);
        }
        slot32 = (RelativeLayout) findViewById(R.id.slot32);
        if (slot32 != null) {
            String subjectName = sharedPreferences.getString("SLOT32_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT32_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot32.getChildAt(0)).setText(subjectName);
                ((TextView) slot32.getChildAt(1)).setText(teacherName);
            }
            slot32.setOnLongClickListener(this);
        }
        slot42 = (RelativeLayout) findViewById(R.id.slot42);
        if (slot42 != null) {
            String subjectName = sharedPreferences.getString("SLOT42_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT42_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot42.getChildAt(0)).setText(subjectName);
                ((TextView) slot42.getChildAt(1)).setText(teacherName);
            }
            slot42.setOnLongClickListener(this);
        }
        slot52 = (RelativeLayout) findViewById(R.id.slot52);
        if (slot52 != null) {
            String subjectName = sharedPreferences.getString("SLOT52_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT52_TEACHER", null);
            if (subjectName != null && teacherName != null) {
                ((TextView) slot52.getChildAt(0)).setText(subjectName);
                ((TextView) slot52.getChildAt(1)).setText(teacherName);
            }
            slot52.setOnLongClickListener(this);
        }


        CheckBox slot11Reminder = (CheckBox) findViewById(R.id.slot11Reminder);
        if (slot11Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT11_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot11Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot11Reminder.setChecked(IS_CHECKED);
            slot11Reminder.setOnClickListener(this);
        }
        CheckBox slot21Reminder = (CheckBox) findViewById(R.id.slot21Reminder);
        if (slot21Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT21_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot21Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot21Reminder.setChecked(IS_CHECKED);
            slot21Reminder.setOnClickListener(this);
        }
        CheckBox slot31Reminder = (CheckBox) findViewById(R.id.slot31Reminder);
        if (slot31Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT31_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot31Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot31Reminder.setChecked(IS_CHECKED);
            slot31Reminder.setOnClickListener(this);
        }
        CheckBox slot41Reminder = (CheckBox) findViewById(R.id.slot41Reminder);
        if (slot41Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT41_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot41Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot41Reminder.setChecked(IS_CHECKED);
            slot41Reminder.setOnClickListener(this);
        }
        CheckBox slot51Reminder = (CheckBox) findViewById(R.id.slot51Reminder);
        if (slot51Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT51_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot51Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot51Reminder.setChecked(IS_CHECKED);
            slot51Reminder.setOnClickListener(this);
        }
        CheckBox slot12Reminder = (CheckBox) findViewById(R.id.slot12Reminder);
        if (slot12Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT12_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot12Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot12Reminder.setChecked(IS_CHECKED);
            slot12Reminder.setOnClickListener(this);
        }
        CheckBox slot22Reminder = (CheckBox) findViewById(R.id.slot22Reminder);
        if (slot22Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT22_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot22Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot22Reminder.setChecked(IS_CHECKED);
            slot22Reminder.setOnClickListener(this);
        }
        CheckBox slot32Reminder = (CheckBox) findViewById(R.id.slot32Reminder);
        if (slot32Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT32_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot32Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot32Reminder.setChecked(IS_CHECKED);
            slot32Reminder.setOnClickListener(this);
        }
        CheckBox slot42Reminder = (CheckBox) findViewById(R.id.slot42Reminder);
        if (slot42Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT42_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot42Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot42Reminder.setChecked(IS_CHECKED);
            slot42Reminder.setOnClickListener(this);
        }
        CheckBox slot52Reminder = (CheckBox) findViewById(R.id.slot52Reminder);
        if (slot52Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT52_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot52Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot52Reminder.setChecked(IS_CHECKED);
            slot52Reminder.setOnClickListener(this);
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
            case R.id.slot31: {
                SELECTED_SLOT = 31;
                AD_editSlot.show();
                break;
            }
            case R.id.slot41: {
                SELECTED_SLOT = 41;
                AD_editSlot.show();
                break;
            }
            case R.id.slot51: {
                SELECTED_SLOT = 51;
                AD_editSlot.show();
                break;
            }
            case R.id.slot12: {
                SELECTED_SLOT = 12;
                AD_editSlot.show();
                break;
            }
            case R.id.slot22: {
                SELECTED_SLOT = 22;
                AD_editSlot.show();
                break;
            }
            case R.id.slot32: {
                SELECTED_SLOT = 32;
                AD_editSlot.show();
                break;
            }
            case R.id.slot42: {
                SELECTED_SLOT = 42;
                AD_editSlot.show();
                break;
            }
            case R.id.slot52: {
                SELECTED_SLOT = 52;
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
                                editor.putString("SLOT21_SUBJECT", subjectName);
                                editor.putString("SLOT21_TEACHER", teacherName);
                                editor.apply();
                                subject = slot21.getChildAt(0);
                                teacher = slot21.getChildAt(1);
                                break;
                            }
                            case 31: {
                                editor.putString("SLOT31_SUBJECT", subjectName);
                                editor.putString("SLOT31_TEACHER", teacherName);
                                editor.apply();
                                subject = slot31.getChildAt(0);
                                teacher = slot31.getChildAt(1);
                                break;
                            }
                            case 41: {
                                editor.putString("SLOT41_SUBJECT", subjectName);
                                editor.putString("SLOT41_TEACHER", teacherName);
                                editor.apply();
                                subject = slot41.getChildAt(0);
                                teacher = slot41.getChildAt(1);
                                break;
                            }
                            case 51: {
                                editor.putString("SLOT51_SUBJECT", subjectName);
                                editor.putString("SLOT51_TEACHER", teacherName);
                                editor.apply();
                                subject = slot51.getChildAt(0);
                                teacher = slot51.getChildAt(1);
                                break;
                            }
                            case 12: {
                                editor.putString("SLOT12_SUBJECT", subjectName);
                                editor.putString("SLOT12_TEACHER", teacherName);
                                editor.apply();
                                subject = slot12.getChildAt(0);
                                teacher = slot12.getChildAt(1);
                                break;
                            }
                            case 22: {
                                editor.putString("SLOT22_SUBJECT", subjectName);
                                editor.putString("SLOT22_TEACHER", teacherName);
                                editor.apply();
                                subject = slot22.getChildAt(0);
                                teacher = slot22.getChildAt(1);
                                break;
                            }
                            case 32: {
                                editor.putString("SLOT32_SUBJECT", subjectName);
                                editor.putString("SLOT32_TEACHER", teacherName);
                                editor.apply();
                                subject = slot32.getChildAt(0);
                                teacher = slot32.getChildAt(1);
                                break;
                            }
                            case 42: {
                                editor.putString("SLOT42_SUBJECT", subjectName);
                                editor.putString("SLOT42_TEACHER", teacherName);
                                editor.apply();
                                subject = slot42.getChildAt(0);
                                teacher = slot42.getChildAt(1);
                                break;
                            }
                            case 52: {
                                editor.putString("SLOT52_SUBJECT", subjectName);
                                editor.putString("SLOT52_TEACHER", teacherName);
                                editor.apply();
                                subject = slot52.getChildAt(0);
                                teacher = slot52.getChildAt(1);
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
                        handleNotification(11, subject, teacher, 1, 7, 45, 0);
                        editor.putBoolean("SLOT11_CHECKED", true);
                        editor.apply();
                    }
                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(11);
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
                        handleNotification(21, subject, teacher, 2, 7, 45, 0);
                        editor.putBoolean("SLOT21_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(21);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT21_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot31Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(31, subject, teacher, 3, 7, 45, 0);
                        editor.putBoolean("SLOT31_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(31);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT31_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot41Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(41, subject, teacher, 4, 7, 45, 0);
                        editor.putBoolean("SLOT41_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(41);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT41_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot51Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(51, subject, teacher, 5, 7, 45, 0);
                        editor.putBoolean("SLOT51_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(51);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT51_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot12Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(12, subject, teacher, 1, 9, 30, 0);
                        editor.putBoolean("SLOT12_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(12);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT12_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot22Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(22, subject, teacher, 2, 9, 30, 0);
                        editor.putBoolean("SLOT22_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(22);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT22_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot32Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(32, subject, teacher, 3, 9, 30, 0);
                        editor.putBoolean("SLOT32_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(32);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT32_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot42Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(42, subject, teacher, 4, 9, 30, 0);
                        editor.putBoolean("SLOT42_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(42);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT42_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot52Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(1)).getText().toString();
                    if (!subject.equals("Subject") && !teacher.equals("Teacher")) {
                        handleNotification(52, subject, teacher, 5, 9, 30, 0);
                        editor.putBoolean("SLOT52_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(52);
                    Toast.makeText(TimeTableView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("SLOT52_CHECKED", false);
                    editor.apply();
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
            case 3: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            }
            case 4: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            }
            case 5: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        //check whether the time is earlier than current time. If so, set it to next week.

        Calendar now = Calendar.getInstance();
        if (calendar.before(now)) {
            calendar.add(Calendar.DATE, 7);
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
