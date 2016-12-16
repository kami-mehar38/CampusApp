package abbottabad.comsats.campusapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * This project CampusApp is created by Kamran Ramzan on 9/1/16.
 */
public class TimeTableView extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private RelativeLayout slot11, slot21, slot31, slot41, slot51;
    private RelativeLayout slot12, slot22, slot32, slot42, slot52;
    private RelativeLayout slot13, slot23, slot33, slot43, slot53;
    private RelativeLayout slot14, slot24, slot34, slot44, slot54;
    private RelativeLayout slot15, slot25, slot35, slot45, slot55;
    private RelativeLayout slot16, slot26, slot36, slot46, slot56;
    private RelativeLayout slot17, slot27, slot37, slot47, slot57;
    private RelativeLayout slot18, slot28, slot38, slot48, slot58;
    private AlertDialog AD_editSlot;
    private EditText ET_editSlotSubject;
    private EditText ET_editSlotTeacher;
    private EditText ET_editSlotRoom;
    private Validation validation;
    private int SELECTED_SLOT;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Bitmap bitmap;
    private CheckBox slot11Reminder;
    private CheckBox slot21Reminder;
    private CheckBox slot31Reminder;
    private CheckBox slot41Reminder;
    private CheckBox slot51Reminder;
    private CheckBox slot12Reminder;
    private CheckBox slot22Reminder;
    private CheckBox slot32Reminder;
    private CheckBox slot42Reminder;
    private CheckBox slot52Reminder;
    private CheckBox slot13Reminder;
    private CheckBox slot23Reminder;
    private CheckBox slot33Reminder;
    private CheckBox slot43Reminder;
    private CheckBox slot53Reminder;
    private CheckBox slot14Reminder;
    private CheckBox slot24Reminder;
    private CheckBox slot34Reminder;
    private CheckBox slot44Reminder;
    private CheckBox slot54Reminder;
    private CheckBox slot15Reminder;
    private CheckBox slot25Reminder;
    private CheckBox slot35Reminder;
    private CheckBox slot45Reminder;
    private CheckBox slot55Reminder;
    private CheckBox slot16Reminder;
    private CheckBox slot26Reminder;
    private CheckBox slot36Reminder;
    private CheckBox slot46Reminder;
    private CheckBox slot56Reminder;
    private CheckBox slot17Reminder;
    private CheckBox slot27Reminder;
    private CheckBox slot37Reminder;
    private CheckBox slot47Reminder;
    private CheckBox slot57Reminder;
    private CheckBox slot18Reminder;
    private CheckBox slot28Reminder;
    private CheckBox slot38Reminder;
    private CheckBox slot48Reminder;
    private CheckBox slot58Reminder;

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
        builder.setCancelable(true);
        AD_editSlot = builder.create();

        ET_editSlotSubject = (EditText) view.findViewById(R.id.ET_editSlotSubject);
        ET_editSlotTeacher = (EditText) view.findViewById(R.id.ET_editSlotTeacher);
        ET_editSlotRoom = (EditText) view.findViewById(R.id.ET_editSlotRoom);
        Button btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);

        slot11Reminder = (CheckBox) findViewById(R.id.slot11Reminder);
        if (slot11Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT11_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot11Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot11Reminder.setChecked(IS_CHECKED);
            slot11Reminder.setOnClickListener(this);
        }
        slot21Reminder = (CheckBox) findViewById(R.id.slot21Reminder);
        if (slot21Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT21_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot21Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot21Reminder.setChecked(IS_CHECKED);
            slot21Reminder.setOnClickListener(this);
        }
        slot31Reminder = (CheckBox) findViewById(R.id.slot31Reminder);
        if (slot31Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT31_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot31Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot31Reminder.setChecked(IS_CHECKED);
            slot31Reminder.setOnClickListener(this);
        }
        slot41Reminder = (CheckBox) findViewById(R.id.slot41Reminder);
        if (slot41Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT41_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot41Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot41Reminder.setChecked(IS_CHECKED);
            slot41Reminder.setOnClickListener(this);
        }
        slot51Reminder = (CheckBox) findViewById(R.id.slot51Reminder);
        if (slot51Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT51_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot51Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot51Reminder.setChecked(IS_CHECKED);
            slot51Reminder.setOnClickListener(this);
        }
        slot12Reminder = (CheckBox) findViewById(R.id.slot12Reminder);
        if (slot12Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT12_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot12Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot12Reminder.setChecked(IS_CHECKED);
            slot12Reminder.setOnClickListener(this);
        }
        slot22Reminder = (CheckBox) findViewById(R.id.slot22Reminder);
        if (slot22Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT22_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot22Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot22Reminder.setChecked(IS_CHECKED);
            slot22Reminder.setOnClickListener(this);
        }
        slot32Reminder = (CheckBox) findViewById(R.id.slot32Reminder);
        if (slot32Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT32_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot32Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot32Reminder.setChecked(IS_CHECKED);
            slot32Reminder.setOnClickListener(this);
        }
        slot42Reminder = (CheckBox) findViewById(R.id.slot42Reminder);
        if (slot42Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT42_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot42Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot42Reminder.setChecked(IS_CHECKED);
            slot42Reminder.setOnClickListener(this);
        }
        slot52Reminder = (CheckBox) findViewById(R.id.slot52Reminder);
        if (slot52Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT52_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot52Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot52Reminder.setChecked(IS_CHECKED);
            slot52Reminder.setOnClickListener(this);
        }
        slot13Reminder = (CheckBox) findViewById(R.id.slot13Reminder);
        if (slot13Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT13_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot13Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot13Reminder.setChecked(IS_CHECKED);
            slot13Reminder.setOnClickListener(this);
        }
        slot23Reminder = (CheckBox) findViewById(R.id.slot23Reminder);
        if (slot23Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT23_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot23Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot23Reminder.setChecked(IS_CHECKED);
            slot23Reminder.setOnClickListener(this);
        }
        slot33Reminder = (CheckBox) findViewById(R.id.slot33Reminder);
        if (slot33Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT33_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot33Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot33Reminder.setChecked(IS_CHECKED);
            slot33Reminder.setOnClickListener(this);
        }
        slot43Reminder = (CheckBox) findViewById(R.id.slot43Reminder);
        if (slot43Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT43_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot43Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot43Reminder.setChecked(IS_CHECKED);
            slot43Reminder.setOnClickListener(this);
        }
        slot53Reminder = (CheckBox) findViewById(R.id.slot53Reminder);
        if (slot53Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT53_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot53Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot53Reminder.setChecked(IS_CHECKED);
            slot53Reminder.setOnClickListener(this);
        }
        slot14Reminder = (CheckBox) findViewById(R.id.slot14Reminder);
        if (slot14Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT14_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot14Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot14Reminder.setChecked(IS_CHECKED);
            slot14Reminder.setOnClickListener(this);
        }
        slot24Reminder = (CheckBox) findViewById(R.id.slot24Reminder);
        if (slot24Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT24_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot24Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot24Reminder.setChecked(IS_CHECKED);
            slot24Reminder.setOnClickListener(this);
        }
        slot34Reminder = (CheckBox) findViewById(R.id.slot34Reminder);
        if (slot34Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT34_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot34Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot34Reminder.setChecked(IS_CHECKED);
            slot34Reminder.setOnClickListener(this);
        }
        slot44Reminder = (CheckBox) findViewById(R.id.slot44Reminder);
        if (slot44Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT44_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot44Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot44Reminder.setChecked(IS_CHECKED);
            slot44Reminder.setOnClickListener(this);
        }
        slot54Reminder = (CheckBox) findViewById(R.id.slot54Reminder);
        if (slot54Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT54_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot54Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot54Reminder.setChecked(IS_CHECKED);
            slot54Reminder.setOnClickListener(this);
        }
        slot15Reminder = (CheckBox) findViewById(R.id.slot15Reminder);
        if (slot15Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT15_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot15Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot15Reminder.setChecked(IS_CHECKED);
            slot15Reminder.setOnClickListener(this);
        }
        slot25Reminder = (CheckBox) findViewById(R.id.slot25Reminder);
        if (slot25Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT25_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot25Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot25Reminder.setChecked(IS_CHECKED);
            slot25Reminder.setOnClickListener(this);
        }
        slot35Reminder = (CheckBox) findViewById(R.id.slot35Reminder);
        if (slot35Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT35_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot35Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot35Reminder.setChecked(IS_CHECKED);
            slot35Reminder.setOnClickListener(this);
        }
        slot45Reminder = (CheckBox) findViewById(R.id.slot45Reminder);
        if (slot45Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT45_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot45Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot45Reminder.setChecked(IS_CHECKED);
            slot45Reminder.setOnClickListener(this);
        }
        slot55Reminder = (CheckBox) findViewById(R.id.slot55Reminder);
        if (slot55Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT55_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot55Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot55Reminder.setChecked(IS_CHECKED);
            slot55Reminder.setOnClickListener(this);
        }

        slot16Reminder = (CheckBox) findViewById(R.id.slot16Reminder);
        if (slot16Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT16_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot16Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot16Reminder.setChecked(IS_CHECKED);
            slot16Reminder.setOnClickListener(this);
        }

        slot26Reminder = (CheckBox) findViewById(R.id.slot26Reminder);
        if (slot26Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT26_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot26Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot26Reminder.setChecked(IS_CHECKED);
            slot26Reminder.setOnClickListener(this);
        }

        slot36Reminder = (CheckBox) findViewById(R.id.slot36Reminder);
        if (slot36Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT36_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot36Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot36Reminder.setChecked(IS_CHECKED);
            slot36Reminder.setOnClickListener(this);
        }

        slot46Reminder = (CheckBox) findViewById(R.id.slot46Reminder);
        if (slot46Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT46_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot46Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot46Reminder.setChecked(IS_CHECKED);
            slot46Reminder.setOnClickListener(this);
        }

        slot56Reminder = (CheckBox) findViewById(R.id.slot56Reminder);
        if (slot56Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT56_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot56Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot56Reminder.setChecked(IS_CHECKED);
            slot56Reminder.setOnClickListener(this);
        }

        slot17Reminder = (CheckBox) findViewById(R.id.slot17Reminder);
        if (slot17Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT17_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot17Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot17Reminder.setChecked(IS_CHECKED);
            slot17Reminder.setOnClickListener(this);
        }

        slot27Reminder = (CheckBox) findViewById(R.id.slot27Reminder);
        if (slot27Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT27_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot27Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot27Reminder.setChecked(IS_CHECKED);
            slot27Reminder.setOnClickListener(this);
        }

        slot37Reminder = (CheckBox) findViewById(R.id.slot37Reminder);
        if (slot37Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT37_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot37Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot37Reminder.setChecked(IS_CHECKED);
            slot37Reminder.setOnClickListener(this);
        }

        slot47Reminder = (CheckBox) findViewById(R.id.slot47Reminder);
        if (slot47Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT47_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot47Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot47Reminder.setChecked(IS_CHECKED);
            slot47Reminder.setOnClickListener(this);
        }

        slot57Reminder = (CheckBox) findViewById(R.id.slot57Reminder);
        if (slot57Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT57_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot57Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot57Reminder.setChecked(IS_CHECKED);
            slot57Reminder.setOnClickListener(this);
        }

        slot18Reminder = (CheckBox) findViewById(R.id.slot18Reminder);
        if (slot18Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT18_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot18Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot18Reminder.setChecked(IS_CHECKED);
            slot18Reminder.setOnClickListener(this);
        }

        slot28Reminder = (CheckBox) findViewById(R.id.slot28Reminder);
        if (slot28Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT28_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot28Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot28Reminder.setChecked(IS_CHECKED);
            slot28Reminder.setOnClickListener(this);
        }

        slot38Reminder = (CheckBox) findViewById(R.id.slot38Reminder);
        if (slot38Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT38_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot38Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot38Reminder.setChecked(IS_CHECKED);
            slot38Reminder.setOnClickListener(this);
        }

        slot48Reminder = (CheckBox) findViewById(R.id.slot48Reminder);
        if (slot48Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT48_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot48Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot48Reminder.setChecked(IS_CHECKED);
            slot48Reminder.setOnClickListener(this);
        }

        slot58Reminder = (CheckBox) findViewById(R.id.slot58Reminder);
        if (slot58Reminder != null) {
            boolean IS_CHECKED = sharedPreferences.getBoolean("SLOT58_CHECKED", false);
            if (IS_CHECKED)
                ((RelativeLayout) slot58Reminder.getParent()).setBackground(ContextCompat
                        .getDrawable(TimeTableView.this,
                                R.drawable.timetable_slot_checked));
            slot58Reminder.setChecked(IS_CHECKED);
            slot58Reminder.setOnClickListener(this);
        }

        slot11 = (RelativeLayout) findViewById(R.id.slot11);
        if (slot11 != null) {
            String subjectName = sharedPreferences.getString("SLOT11_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT11_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT11_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot11.getChildAt(0)).setText(subjectName);
                ((TextView) slot11.getChildAt(1)).setText(teacherName);
                ((TextView) slot11.getChildAt(2)).setText(roomNo);
            }else if (slot11Reminder != null){
                slot11Reminder.setVisibility(View.INVISIBLE);
            }
            slot11.setOnLongClickListener(this);
        }
        slot21 = (RelativeLayout) findViewById(R.id.slot21);
        if (slot21 != null) {
            String subjectName = sharedPreferences.getString("SLOT21_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT21_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT21_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot21.getChildAt(0)).setText(subjectName);
                ((TextView) slot21.getChildAt(1)).setText(teacherName);
                ((TextView) slot21.getChildAt(2)).setText(roomNo);
            }else if (slot21Reminder != null){
                slot21Reminder.setVisibility(View.INVISIBLE);
            }
            slot21.setOnLongClickListener(this);
        }
        slot31 = (RelativeLayout) findViewById(R.id.slot31);
        if (slot31 != null) {
            String subjectName = sharedPreferences.getString("SLOT31_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT31_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT31_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot31.getChildAt(0)).setText(subjectName);
                ((TextView) slot31.getChildAt(1)).setText(teacherName);
                ((TextView) slot31.getChildAt(2)).setText(roomNo);
            }else if (slot31Reminder != null){
                slot31Reminder.setVisibility(View.INVISIBLE);
            }
            slot31.setOnLongClickListener(this);
        }
        slot41 = (RelativeLayout) findViewById(R.id.slot41);
        if (slot41 != null) {
            String subjectName = sharedPreferences.getString("SLOT41_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT41_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT41_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot41.getChildAt(0)).setText(subjectName);
                ((TextView) slot41.getChildAt(1)).setText(teacherName);
                ((TextView) slot41.getChildAt(2)).setText(roomNo);
            }else if (slot41Reminder != null){
                slot41Reminder.setVisibility(View.INVISIBLE);
            }
            slot41.setOnLongClickListener(this);
        }
        slot51 = (RelativeLayout) findViewById(R.id.slot51);
        if (slot51 != null) {
            String subjectName = sharedPreferences.getString("SLOT51_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT51_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT51_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot51.getChildAt(0)).setText(subjectName);
                ((TextView) slot51.getChildAt(1)).setText(teacherName);
                ((TextView) slot51.getChildAt(2)).setText(roomNo);
            } else if (slot51Reminder != null){
                slot51Reminder.setVisibility(View.INVISIBLE);
            }
            slot51.setOnLongClickListener(this);
        }
        slot12 = (RelativeLayout) findViewById(R.id.slot12);
        if (slot12 != null) {
            String subjectName = sharedPreferences.getString("SLOT12_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT12_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT12_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot12.getChildAt(0)).setText(subjectName);
                ((TextView) slot12.getChildAt(1)).setText(teacherName);
                ((TextView) slot12.getChildAt(2)).setText(roomNo);
            }else if (slot12Reminder != null){
                slot12Reminder.setVisibility(View.INVISIBLE);
            }
            slot12.setOnLongClickListener(this);
        }
        slot22 = (RelativeLayout) findViewById(R.id.slot22);
        if (slot22 != null) {
            String subjectName = sharedPreferences.getString("SLOT22_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT22_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT22_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot22.getChildAt(0)).setText(subjectName);
                ((TextView) slot22.getChildAt(1)).setText(teacherName);
                ((TextView) slot22.getChildAt(2)).setText(roomNo);
            }else if (slot22Reminder != null){
                slot22Reminder.setVisibility(View.INVISIBLE);
            }
            slot22.setOnLongClickListener(this);
        }
        slot32 = (RelativeLayout) findViewById(R.id.slot32);
        if (slot32 != null) {
            String subjectName = sharedPreferences.getString("SLOT32_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT32_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT32_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot32.getChildAt(0)).setText(subjectName);
                ((TextView) slot32.getChildAt(1)).setText(teacherName);
                ((TextView) slot32.getChildAt(2)).setText(roomNo);
            }else if (slot32Reminder != null){
                slot32Reminder.setVisibility(View.INVISIBLE);
            }
            slot32.setOnLongClickListener(this);
        }
        slot42 = (RelativeLayout) findViewById(R.id.slot42);
        if (slot42 != null) {
            String subjectName = sharedPreferences.getString("SLOT42_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT42_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT42_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot42.getChildAt(0)).setText(subjectName);
                ((TextView) slot42.getChildAt(1)).setText(teacherName);
                ((TextView) slot42.getChildAt(2)).setText(roomNo);
            }else if (slot42Reminder != null){
                slot42Reminder.setVisibility(View.INVISIBLE);
            }
            slot42.setOnLongClickListener(this);
        }
        slot52 = (RelativeLayout) findViewById(R.id.slot52);
        if (slot52 != null) {
            String subjectName = sharedPreferences.getString("SLOT52_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT52_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT52_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot52.getChildAt(0)).setText(subjectName);
                ((TextView) slot52.getChildAt(1)).setText(teacherName);
                ((TextView) slot52.getChildAt(2)).setText(roomNo);
            }else if (slot52Reminder != null){
                slot52Reminder.setVisibility(View.INVISIBLE);
            }
            slot52.setOnLongClickListener(this);
        }
        slot13 = (RelativeLayout) findViewById(R.id.slot13);
        if (slot13 != null) {
            String subjectName = sharedPreferences.getString("SLOT13_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT13_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT13_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot13.getChildAt(0)).setText(subjectName);
                ((TextView) slot13.getChildAt(1)).setText(teacherName);
                ((TextView) slot13.getChildAt(2)).setText(roomNo);
            }else if (slot13Reminder != null){
                slot13Reminder.setVisibility(View.INVISIBLE);
            }
            slot13.setOnLongClickListener(this);
        }
        slot23 = (RelativeLayout) findViewById(R.id.slot23);
        if (slot23 != null) {
            String subjectName = sharedPreferences.getString("SLOT23_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT23_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT23_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot23.getChildAt(0)).setText(subjectName);
                ((TextView) slot23.getChildAt(1)).setText(teacherName);
                ((TextView) slot23.getChildAt(2)).setText(roomNo);
            }else if (slot23Reminder != null){
                slot23Reminder.setVisibility(View.INVISIBLE);
            }
            slot23.setOnLongClickListener(this);
        }
        slot33 = (RelativeLayout) findViewById(R.id.slot33);
        if (slot33 != null) {
            String subjectName = sharedPreferences.getString("SLOT33_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT33_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT33_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot33.getChildAt(0)).setText(subjectName);
                ((TextView) slot33.getChildAt(1)).setText(teacherName);
                ((TextView) slot33.getChildAt(2)).setText(roomNo);
            }else if (slot33Reminder != null){
                slot33Reminder.setVisibility(View.INVISIBLE);
            }
            slot33.setOnLongClickListener(this);
        }
        slot43 = (RelativeLayout) findViewById(R.id.slot43);
        if (slot43 != null) {
            String subjectName = sharedPreferences.getString("SLOT43_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT43_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT43_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot43.getChildAt(0)).setText(subjectName);
                ((TextView) slot43.getChildAt(1)).setText(teacherName);
                ((TextView) slot43.getChildAt(2)).setText(roomNo);
            }else if (slot43Reminder != null){
                slot43Reminder.setVisibility(View.INVISIBLE);
            }
            slot43.setOnLongClickListener(this);
        }
        slot53 = (RelativeLayout) findViewById(R.id.slot53);
        if (slot53 != null) {
            String subjectName = sharedPreferences.getString("SLOT53_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT53_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT53_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot53.getChildAt(0)).setText(subjectName);
                ((TextView) slot53.getChildAt(1)).setText(teacherName);
                ((TextView) slot53.getChildAt(2)).setText(roomNo);
            }else if (slot53Reminder != null){
                slot53Reminder.setVisibility(View.INVISIBLE);
            }
            slot53.setOnLongClickListener(this);
        }
        slot14 = (RelativeLayout) findViewById(R.id.slot14);
        if (slot14 != null) {
            String subjectName = sharedPreferences.getString("SLOT14_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT14_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT14_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot14.getChildAt(0)).setText(subjectName);
                ((TextView) slot14.getChildAt(1)).setText(teacherName);
                ((TextView) slot14.getChildAt(2)).setText(roomNo);
            }else if (slot14Reminder != null){
                slot14Reminder.setVisibility(View.INVISIBLE);
            }
            slot14.setOnLongClickListener(this);
        }
        slot24 = (RelativeLayout) findViewById(R.id.slot24);
        if (slot24 != null) {
            String subjectName = sharedPreferences.getString("SLOT24_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT24_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT24_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot24.getChildAt(0)).setText(subjectName);
                ((TextView) slot24.getChildAt(1)).setText(teacherName);
                ((TextView) slot24.getChildAt(2)).setText(roomNo);
            }else if (slot24Reminder != null){
                slot24Reminder.setVisibility(View.INVISIBLE);
            }
            slot24.setOnLongClickListener(this);
        }
        slot34 = (RelativeLayout) findViewById(R.id.slot34);
        if (slot34 != null) {
            String subjectName = sharedPreferences.getString("SLOT34_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT34_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT34_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot34.getChildAt(0)).setText(subjectName);
                ((TextView) slot34.getChildAt(1)).setText(teacherName);
                ((TextView) slot34.getChildAt(2)).setText(roomNo);
            }else if (slot34Reminder != null){
                slot34Reminder.setVisibility(View.INVISIBLE);
            }
            slot34.setOnLongClickListener(this);
        }
        slot44 = (RelativeLayout) findViewById(R.id.slot44);
        if (slot44 != null) {
            String subjectName = sharedPreferences.getString("SLOT44_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT44_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT44_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot44.getChildAt(0)).setText(subjectName);
                ((TextView) slot44.getChildAt(1)).setText(teacherName);
                ((TextView) slot44.getChildAt(2)).setText(roomNo);
            }else if (slot44Reminder != null){
                slot44Reminder.setVisibility(View.INVISIBLE);
            }
            slot44.setOnLongClickListener(this);
        }
        slot54 = (RelativeLayout) findViewById(R.id.slot54);
        if (slot54 != null) {
            String subjectName = sharedPreferences.getString("SLOT54_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT54_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT54_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot54.getChildAt(0)).setText(subjectName);
                ((TextView) slot54.getChildAt(1)).setText(teacherName);
                ((TextView) slot54.getChildAt(2)).setText(roomNo);
            }else if (slot54Reminder != null){
                slot54Reminder.setVisibility(View.INVISIBLE);
            }
            slot54.setOnLongClickListener(this);
        }
        slot15 = (RelativeLayout) findViewById(R.id.slot15);
        if (slot15 != null) {
            String subjectName = sharedPreferences.getString("SLOT15_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT15_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT15_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot15.getChildAt(0)).setText(subjectName);
                ((TextView) slot15.getChildAt(1)).setText(teacherName);
                ((TextView) slot15.getChildAt(2)).setText(roomNo);
            }else if (slot15Reminder != null){
                slot15Reminder.setVisibility(View.INVISIBLE);
            }
            slot15.setOnLongClickListener(this);
        }
        slot25 = (RelativeLayout) findViewById(R.id.slot25);
        if (slot25 != null) {
            String subjectName = sharedPreferences.getString("SLOT25_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT25_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT25_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot25.getChildAt(0)).setText(subjectName);
                ((TextView) slot25.getChildAt(1)).setText(teacherName);
                ((TextView) slot25.getChildAt(2)).setText(roomNo);
            }else if (slot25Reminder != null){
                slot25Reminder.setVisibility(View.INVISIBLE);
            }
            slot25.setOnLongClickListener(this);
        }
        slot35 = (RelativeLayout) findViewById(R.id.slot35);
        if (slot35 != null) {
            String subjectName = sharedPreferences.getString("SLOT35_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT35_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT35_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot35.getChildAt(0)).setText(subjectName);
                ((TextView) slot35.getChildAt(1)).setText(teacherName);
                ((TextView) slot35.getChildAt(2)).setText(roomNo);
            }else if (slot35Reminder != null){
                slot35Reminder.setVisibility(View.INVISIBLE);
            }
            slot35.setOnLongClickListener(this);
        }
        slot45 = (RelativeLayout) findViewById(R.id.slot45);
        if (slot45 != null) {
            String subjectName = sharedPreferences.getString("SLOT45_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT45_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT45_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot45.getChildAt(0)).setText(subjectName);
                ((TextView) slot45.getChildAt(1)).setText(teacherName);
                ((TextView) slot45.getChildAt(2)).setText(roomNo);
            }else if (slot45Reminder != null){
                slot45Reminder.setVisibility(View.INVISIBLE);
            }
            slot45.setOnLongClickListener(this);
        }
        slot55 = (RelativeLayout) findViewById(R.id.slot55);
        if (slot55 != null) {
            String subjectName = sharedPreferences.getString("SLOT55_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT55_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT55_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot55.getChildAt(0)).setText(subjectName);
                ((TextView) slot55.getChildAt(1)).setText(teacherName);
                ((TextView) slot55.getChildAt(2)).setText(roomNo);
            }else if (slot55Reminder != null){
                slot55Reminder.setVisibility(View.INVISIBLE);
            }
            slot55.setOnLongClickListener(this);
        }

        slot16 = (RelativeLayout) findViewById(R.id.slot16);
        if (slot16 != null) {
            String subjectName = sharedPreferences.getString("SLOT16_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT16_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT16_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot16.getChildAt(0)).setText(subjectName);
                ((TextView) slot16.getChildAt(1)).setText(teacherName);
                ((TextView) slot16.getChildAt(2)).setText(roomNo);
            }else if (slot16Reminder != null){
                slot16Reminder.setVisibility(View.INVISIBLE);
            }
            slot16.setOnLongClickListener(this);
        }

        slot26 = (RelativeLayout) findViewById(R.id.slot26);
        if (slot26 != null) {
            String subjectName = sharedPreferences.getString("SLOT26_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT26_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT26_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot26.getChildAt(0)).setText(subjectName);
                ((TextView) slot26.getChildAt(1)).setText(teacherName);
                ((TextView) slot26.getChildAt(2)).setText(roomNo);
            }else if (slot26Reminder != null){
                slot26Reminder.setVisibility(View.INVISIBLE);
            }
            slot26.setOnLongClickListener(this);
        }

        slot36 = (RelativeLayout) findViewById(R.id.slot36);
        if (slot36 != null) {
            String subjectName = sharedPreferences.getString("SLOT36_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT36_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT36_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot36.getChildAt(0)).setText(subjectName);
                ((TextView) slot36.getChildAt(1)).setText(teacherName);
                ((TextView) slot36.getChildAt(2)).setText(roomNo);
            }else if (slot36Reminder != null){
                slot36Reminder.setVisibility(View.INVISIBLE);
            }
            slot36.setOnLongClickListener(this);
        }

        slot46 = (RelativeLayout) findViewById(R.id.slot46);
        if (slot46 != null) {
            String subjectName = sharedPreferences.getString("SLOT46_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT46_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT46_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot46.getChildAt(0)).setText(subjectName);
                ((TextView) slot46.getChildAt(1)).setText(teacherName);
                ((TextView) slot46.getChildAt(2)).setText(roomNo);
            }else if (slot46Reminder != null){
                slot46Reminder.setVisibility(View.INVISIBLE);
            }
            slot46.setOnLongClickListener(this);
        }

        slot56 = (RelativeLayout) findViewById(R.id.slot56);
        if (slot56 != null) {
            String subjectName = sharedPreferences.getString("SLOT56_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT56_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT56_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot56.getChildAt(0)).setText(subjectName);
                ((TextView) slot56.getChildAt(1)).setText(teacherName);
                ((TextView) slot56.getChildAt(2)).setText(roomNo);
            }else if (slot56Reminder != null){
                slot56Reminder.setVisibility(View.INVISIBLE);
            }
            slot56.setOnLongClickListener(this);
        }

        slot17 = (RelativeLayout) findViewById(R.id.slot17);
        if (slot17 != null) {
            String subjectName = sharedPreferences.getString("SLOT17_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT17_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT17_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot17.getChildAt(0)).setText(subjectName);
                ((TextView) slot17.getChildAt(1)).setText(teacherName);
                ((TextView) slot17.getChildAt(2)).setText(roomNo);
            }else if (slot17Reminder != null){
                slot17Reminder.setVisibility(View.INVISIBLE);
            }
            slot17.setOnLongClickListener(this);
        }

        slot27 = (RelativeLayout) findViewById(R.id.slot27);
        if (slot27 != null) {
            String subjectName = sharedPreferences.getString("SLOT27_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT27_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT27_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot27.getChildAt(0)).setText(subjectName);
                ((TextView) slot27.getChildAt(1)).setText(teacherName);
                ((TextView) slot27.getChildAt(2)).setText(roomNo);
            }else if (slot27Reminder != null){
                slot27Reminder.setVisibility(View.INVISIBLE);
            }
            slot27.setOnLongClickListener(this);
        }

        slot37 = (RelativeLayout) findViewById(R.id.slot37);
        if (slot37 != null) {
            String subjectName = sharedPreferences.getString("SLOT37_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT37_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT37_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot37.getChildAt(0)).setText(subjectName);
                ((TextView) slot37.getChildAt(1)).setText(teacherName);
                ((TextView) slot37.getChildAt(2)).setText(roomNo);
            }else if (slot37Reminder != null){
                slot37Reminder.setVisibility(View.INVISIBLE);
            }
            slot37.setOnLongClickListener(this);
        }

        slot47 = (RelativeLayout) findViewById(R.id.slot47);
        if (slot47 != null) {
            String subjectName = sharedPreferences.getString("SLOT47_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT47_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT47_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot47.getChildAt(0)).setText(subjectName);
                ((TextView) slot47.getChildAt(1)).setText(teacherName);
                ((TextView) slot47.getChildAt(2)).setText(roomNo);
            }else if (slot47Reminder != null){
                slot47Reminder.setVisibility(View.INVISIBLE);
            }
            slot47.setOnLongClickListener(this);
        }

        slot57 = (RelativeLayout) findViewById(R.id.slot57);
        if (slot57 != null) {
            String subjectName = sharedPreferences.getString("SLOT57_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT57_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT57_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot57.getChildAt(0)).setText(subjectName);
                ((TextView) slot57.getChildAt(1)).setText(teacherName);
                ((TextView) slot57.getChildAt(2)).setText(roomNo);
            }else if (slot57Reminder != null){
                slot57Reminder.setVisibility(View.INVISIBLE);
            }
            slot57.setOnLongClickListener(this);
        }

        slot18 = (RelativeLayout) findViewById(R.id.slot18);
        if (slot18 != null) {
            String subjectName = sharedPreferences.getString("SLOT18_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT18_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT18_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot18.getChildAt(0)).setText(subjectName);
                ((TextView) slot18.getChildAt(1)).setText(teacherName);
                ((TextView) slot18.getChildAt(2)).setText(roomNo);
            }else if (slot18Reminder != null){
                slot18Reminder.setVisibility(View.INVISIBLE);
            }
            slot18.setOnLongClickListener(this);
        }

        slot28 = (RelativeLayout) findViewById(R.id.slot28);
        if (slot28 != null) {
            String subjectName = sharedPreferences.getString("SLOT28_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT28_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT28_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot28.getChildAt(0)).setText(subjectName);
                ((TextView) slot28.getChildAt(1)).setText(teacherName);
                ((TextView) slot28.getChildAt(2)).setText(roomNo);
            }else if (slot28Reminder != null){
                slot28Reminder.setVisibility(View.INVISIBLE);
            }
            slot28.setOnLongClickListener(this);
        }

        slot38 = (RelativeLayout) findViewById(R.id.slot38);
        if (slot38 != null) {
            String subjectName = sharedPreferences.getString("SLOT38_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT38_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT38_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot38.getChildAt(0)).setText(subjectName);
                ((TextView) slot38.getChildAt(1)).setText(teacherName);
                ((TextView) slot38.getChildAt(2)).setText(roomNo);
            }else if (slot38Reminder != null){
                slot38Reminder.setVisibility(View.INVISIBLE);
            }
            slot38.setOnLongClickListener(this);
        }

        slot48 = (RelativeLayout) findViewById(R.id.slot48);
        if (slot48 != null) {
            String subjectName = sharedPreferences.getString("SLOT48_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT48_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT48_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot48.getChildAt(0)).setText(subjectName);
                ((TextView) slot48.getChildAt(1)).setText(teacherName);
                ((TextView) slot48.getChildAt(2)).setText(roomNo);
            }else if (slot48Reminder != null){
                slot48Reminder.setVisibility(View.INVISIBLE);
            }
            slot48.setOnLongClickListener(this);
        }

        slot58 = (RelativeLayout) findViewById(R.id.slot58);
        if (slot58 != null) {
            String subjectName = sharedPreferences.getString("SLOT58_SUBJECT", null);
            String teacherName = sharedPreferences.getString("SLOT58_TEACHER", null);
            String roomNo = sharedPreferences.getString("SLOT58_ROOM", null);
            if (subjectName != null && teacherName != null && roomNo != null) {
                ((TextView) slot58.getChildAt(0)).setText(subjectName);
                ((TextView) slot58.getChildAt(1)).setText(teacherName);
                ((TextView) slot58.getChildAt(2)).setText(roomNo);
            }else if (slot58Reminder != null){
                slot58Reminder.setVisibility(View.INVISIBLE);
            }
            slot58.setOnLongClickListener(this);
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
            case R.id.slot13: {
                SELECTED_SLOT = 13;
                AD_editSlot.show();
                break;
            }
            case R.id.slot23: {
                SELECTED_SLOT = 23;
                AD_editSlot.show();
                break;
            }
            case R.id.slot33: {
                SELECTED_SLOT = 33;
                AD_editSlot.show();
                break;
            }
            case R.id.slot43: {
                SELECTED_SLOT = 43;
                AD_editSlot.show();
                break;
            }
            case R.id.slot53: {
                SELECTED_SLOT = 53;
                AD_editSlot.show();
                break;
            }
            case R.id.slot14: {
                SELECTED_SLOT = 14;
                AD_editSlot.show();
                break;
            }
            case R.id.slot24: {
                SELECTED_SLOT = 24;
                AD_editSlot.show();
                break;
            }
            case R.id.slot34: {
                SELECTED_SLOT = 34;
                AD_editSlot.show();
                break;
            }
            case R.id.slot44: {
                SELECTED_SLOT = 44;
                AD_editSlot.show();
                break;
            }
            case R.id.slot54: {
                SELECTED_SLOT = 54;
                AD_editSlot.show();
                break;
            }
            case R.id.slot15: {
                SELECTED_SLOT = 15;
                AD_editSlot.show();
                break;
            }
            case R.id.slot25: {
                SELECTED_SLOT = 25;
                AD_editSlot.show();
                break;
            }

            case R.id.slot35: {
                SELECTED_SLOT = 35;
                AD_editSlot.show();
                break;
            }

            case R.id.slot45: {
                SELECTED_SLOT = 45;
                AD_editSlot.show();
                break;
            }

            case R.id.slot55: {
                SELECTED_SLOT = 55;
                AD_editSlot.show();
                break;
            }
            case R.id.slot16: {
                SELECTED_SLOT = 16;
                AD_editSlot.show();
                break;
            }
            case R.id.slot26: {
                SELECTED_SLOT = 26;
                AD_editSlot.show();
                break;
            }
            case R.id.slot36: {
                SELECTED_SLOT = 36;
                AD_editSlot.show();
                break;
            }
            case R.id.slot46: {
                SELECTED_SLOT = 46;
                AD_editSlot.show();
                break;
            }
            case R.id.slot56: {
                SELECTED_SLOT = 56;
                AD_editSlot.show();
                break;
            }
            case R.id.slot17: {
                SELECTED_SLOT = 17;
                AD_editSlot.show();
                break;
            }
            case R.id.slot27: {
                SELECTED_SLOT = 27;
                AD_editSlot.show();
                break;
            }
            case R.id.slot37: {
                SELECTED_SLOT = 37;
                AD_editSlot.show();
                break;
            }
            case R.id.slot47: {
                SELECTED_SLOT = 47;
                AD_editSlot.show();
                break;
            }
            case R.id.slot57: {
                SELECTED_SLOT = 57;
                AD_editSlot.show();
                break;
            }
            case R.id.slot18: {
                SELECTED_SLOT = 18;
                AD_editSlot.show();
                break;
            }
            case R.id.slot28: {
                SELECTED_SLOT = 28;
                AD_editSlot.show();
                break;
            }
            case R.id.slot38: {
                SELECTED_SLOT = 38;
                AD_editSlot.show();
                break;
            }
            case R.id.slot48: {
                SELECTED_SLOT = 48;
                AD_editSlot.show();
                break;
            }
            case R.id.slot58: {
                SELECTED_SLOT = 58;
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
                String roomNo = ET_editSlotRoom.getText().toString().trim();
                if (validation.validateSubjectName(subjectName)) {
                    if (validation.validateTeacherName(teacherName)) {
                        if (validation.validateRoomNo(roomNo)) {
                            View subject = null;
                            View teacher = null;
                            View room = null;
                            View reminder = null;
                            switch (SELECTED_SLOT) {
                                case 11: {
                                    editor.putString("SLOT11_SUBJECT", subjectName);
                                    editor.putString("SLOT11_TEACHER", teacherName);
                                    editor.putString("SLOT11_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot11.getChildAt(0);
                                    teacher = slot11.getChildAt(1);
                                    room = slot11.getChildAt(2);
                                    reminder = slot11.getChildAt(3);
                                    break;
                                }
                                case 21: {
                                    editor.putString("SLOT21_SUBJECT", subjectName);
                                    editor.putString("SLOT21_TEACHER", teacherName);
                                    editor.putString("SLOT21_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot21.getChildAt(0);
                                    teacher = slot21.getChildAt(1);
                                    room = slot21.getChildAt(2);
                                    reminder = slot21.getChildAt(3);
                                    break;
                                }
                                case 31: {
                                    editor.putString("SLOT31_SUBJECT", subjectName);
                                    editor.putString("SLOT31_TEACHER", teacherName);
                                    editor.putString("SLOT31_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot31.getChildAt(0);
                                    teacher = slot31.getChildAt(1);
                                    room = slot31.getChildAt(2);
                                    reminder = slot31.getChildAt(3);
                                    break;
                                }
                                case 41: {
                                    editor.putString("SLOT41_SUBJECT", subjectName);
                                    editor.putString("SLOT41_TEACHER", teacherName);
                                    editor.putString("SLOT41_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot41.getChildAt(0);
                                    teacher = slot41.getChildAt(1);
                                    room = slot41.getChildAt(2);
                                    reminder = slot41.getChildAt(3);
                                    break;
                                }
                                case 51: {
                                    editor.putString("SLOT51_SUBJECT", subjectName);
                                    editor.putString("SLOT51_TEACHER", teacherName);
                                    editor.putString("SLOT51_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot51.getChildAt(0);
                                    teacher = slot51.getChildAt(1);
                                    room = slot51.getChildAt(2);
                                    reminder = slot51.getChildAt(3);
                                    break;
                                }
                                case 12: {
                                    editor.putString("SLOT12_SUBJECT", subjectName);
                                    editor.putString("SLOT12_TEACHER", teacherName);
                                    editor.putString("SLOT12_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot12.getChildAt(0);
                                    teacher = slot12.getChildAt(1);
                                    room = slot12.getChildAt(2);
                                    reminder = slot12.getChildAt(3);
                                    break;
                                }
                                case 22: {
                                    editor.putString("SLOT22_SUBJECT", subjectName);
                                    editor.putString("SLOT22_TEACHER", teacherName);
                                    editor.putString("SLOT22_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot22.getChildAt(0);
                                    teacher = slot22.getChildAt(1);
                                    room = slot22.getChildAt(2);
                                    reminder = slot22.getChildAt(3);
                                    break;
                                }
                                case 32: {
                                    editor.putString("SLOT32_SUBJECT", subjectName);
                                    editor.putString("SLOT32_TEACHER", teacherName);
                                    editor.putString("SLOT32_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot32.getChildAt(0);
                                    teacher = slot32.getChildAt(1);
                                    room = slot32.getChildAt(2);
                                    reminder = slot32.getChildAt(3);
                                    break;
                                }
                                case 42: {
                                    editor.putString("SLOT42_SUBJECT", subjectName);
                                    editor.putString("SLOT42_TEACHER", teacherName);
                                    editor.putString("SLOT42_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot42.getChildAt(0);
                                    teacher = slot42.getChildAt(1);
                                    room = slot42.getChildAt(2);
                                    reminder = slot42.getChildAt(3);
                                    break;
                                }
                                case 52: {
                                    editor.putString("SLOT52_SUBJECT", subjectName);
                                    editor.putString("SLOT52_TEACHER", teacherName);
                                    editor.putString("SLOT52_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot52.getChildAt(0);
                                    teacher = slot52.getChildAt(1);
                                    room = slot52.getChildAt(2);
                                    reminder = slot52.getChildAt(3);
                                    break;
                                }
                                case 13: {
                                    editor.putString("SLOT13_SUBJECT", subjectName);
                                    editor.putString("SLOT13_TEACHER", teacherName);
                                    editor.putString("SLOT13_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot13.getChildAt(0);
                                    teacher = slot13.getChildAt(1);
                                    room = slot13.getChildAt(2);
                                    reminder = slot13.getChildAt(3);
                                    break;
                                }
                                case 23: {
                                    editor.putString("SLOT23_SUBJECT", subjectName);
                                    editor.putString("SLOT23_TEACHER", teacherName);
                                    editor.putString("SLOT23_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot23.getChildAt(0);
                                    teacher = slot23.getChildAt(1);
                                    room = slot23.getChildAt(2);
                                    reminder = slot23.getChildAt(3);
                                    break;
                                }
                                case 33: {
                                    editor.putString("SLOT33_SUBJECT", subjectName);
                                    editor.putString("SLOT33_TEACHER", teacherName);
                                    editor.putString("SLOT33_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot33.getChildAt(0);
                                    teacher = slot33.getChildAt(1);
                                    room = slot33.getChildAt(2);
                                    reminder = slot33.getChildAt(3);
                                    break;
                                }
                                case 43: {
                                    editor.putString("SLOT43_SUBJECT", subjectName);
                                    editor.putString("SLOT43_TEACHER", teacherName);
                                    editor.putString("SLOT43_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot43.getChildAt(0);
                                    teacher = slot43.getChildAt(1);
                                    room = slot43.getChildAt(2);
                                    reminder = slot43.getChildAt(3);
                                    break;
                                }
                                case 53: {
                                    editor.putString("SLOT53_SUBJECT", subjectName);
                                    editor.putString("SLOT53_TEACHER", teacherName);
                                    editor.putString("SLOT53_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot53.getChildAt(0);
                                    teacher = slot53.getChildAt(1);
                                    room = slot53.getChildAt(2);
                                    reminder = slot53.getChildAt(3);
                                    break;
                                }
                                case 14: {
                                    editor.putString("SLOT14_SUBJECT", subjectName);
                                    editor.putString("SLOT14_TEACHER", teacherName);
                                    editor.putString("SLOT14_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot14.getChildAt(0);
                                    teacher = slot14.getChildAt(1);
                                    room = slot14.getChildAt(2);
                                    reminder = slot14.getChildAt(3);
                                    break;
                                }
                                case 24: {
                                    editor.putString("SLOT24_SUBJECT", subjectName);
                                    editor.putString("SLOT24_TEACHER", teacherName);
                                    editor.putString("SLOT24_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot24.getChildAt(0);
                                    teacher = slot24.getChildAt(1);
                                    room = slot24.getChildAt(2);
                                    reminder = slot24.getChildAt(3);
                                    break;
                                }
                                case 34: {
                                    editor.putString("SLOT34_SUBJECT", subjectName);
                                    editor.putString("SLOT34_TEACHER", teacherName);
                                    editor.putString("SLOT34_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot34.getChildAt(0);
                                    teacher = slot34.getChildAt(1);
                                    room = slot34.getChildAt(2);
                                    reminder = slot34.getChildAt(3);
                                    break;
                                }
                                case 44: {
                                    editor.putString("SLOT44_SUBJECT", subjectName);
                                    editor.putString("SLOT44_TEACHER", teacherName);
                                    editor.putString("SLOT44_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot44.getChildAt(0);
                                    teacher = slot44.getChildAt(1);
                                    room = slot44.getChildAt(2);
                                    reminder = slot44.getChildAt(3);
                                    break;
                                }
                                case 54: {
                                    editor.putString("SLOT54_SUBJECT", subjectName);
                                    editor.putString("SLOT54_TEACHER", teacherName);
                                    editor.putString("SLOT54_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot54.getChildAt(0);
                                    teacher = slot54.getChildAt(1);
                                    room = slot54.getChildAt(2);
                                    reminder = slot54.getChildAt(3);
                                    break;
                                }
                                case 15: {
                                    editor.putString("SLOT15_SUBJECT", subjectName);
                                    editor.putString("SLOT15_TEACHER", teacherName);
                                    editor.putString("SLOT15_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot15.getChildAt(0);
                                    teacher = slot15.getChildAt(1);
                                    room = slot15.getChildAt(2);
                                    reminder = slot15.getChildAt(3);
                                    break;
                                }
                                case 25: {
                                    editor.putString("SLOT25_SUBJECT", subjectName);
                                    editor.putString("SLOT25_TEACHER", teacherName);
                                    editor.putString("SLOT25_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot25.getChildAt(0);
                                    teacher = slot25.getChildAt(1);
                                    room = slot25.getChildAt(2);
                                    reminder = slot25.getChildAt(3);
                                    break;
                                }
                                case 35: {
                                    editor.putString("SLOT35_SUBJECT", subjectName);
                                    editor.putString("SLOT35_TEACHER", teacherName);
                                    editor.putString("SLOT35_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot35.getChildAt(0);
                                    teacher = slot35.getChildAt(1);
                                    room = slot35.getChildAt(2);
                                    reminder = slot35.getChildAt(3);
                                    break;
                                }
                                case 45: {
                                    editor.putString("SLOT45_SUBJECT", subjectName);
                                    editor.putString("SLOT45_TEACHER", teacherName);
                                    editor.putString("SLOT45_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot45.getChildAt(0);
                                    teacher = slot45.getChildAt(1);
                                    room = slot45.getChildAt(2);
                                    reminder = slot45.getChildAt(3);
                                    break;
                                }
                                case 55: {
                                    editor.putString("SLOT55_SUBJECT", subjectName);
                                    editor.putString("SLOT55_TEACHER", teacherName);
                                    editor.putString("SLOT55_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot55.getChildAt(0);
                                    teacher = slot55.getChildAt(1);
                                    room = slot55.getChildAt(2);
                                    reminder = slot55.getChildAt(3);
                                    break;
                                }
                                case 16: {
                                    editor.putString("SLOT16_SUBJECT", subjectName);
                                    editor.putString("SLOT16_TEACHER", teacherName);
                                    editor.putString("SLOT16_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot16.getChildAt(0);
                                    teacher = slot16.getChildAt(1);
                                    room = slot16.getChildAt(2);
                                    reminder = slot16.getChildAt(3);
                                    break;
                                }
                                case 26: {
                                    editor.putString("SLOT26_SUBJECT", subjectName);
                                    editor.putString("SLOT26_TEACHER", teacherName);
                                    editor.putString("SLOT26_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot26.getChildAt(0);
                                    teacher = slot26.getChildAt(1);
                                    room = slot26.getChildAt(2);
                                    reminder = slot26.getChildAt(3);
                                    break;
                                }
                                case 36: {
                                    editor.putString("SLOT36_SUBJECT", subjectName);
                                    editor.putString("SLOT36_TEACHER", teacherName);
                                    editor.putString("SLOT36_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot36.getChildAt(0);
                                    teacher = slot36.getChildAt(1);
                                    room = slot36.getChildAt(2);
                                    reminder = slot36.getChildAt(3);
                                    break;
                                }
                                case 46: {
                                    editor.putString("SLOT46_SUBJECT", subjectName);
                                    editor.putString("SLOT46_TEACHER", teacherName);
                                    editor.putString("SLOT46_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot46.getChildAt(0);
                                    teacher = slot46.getChildAt(1);
                                    room = slot46.getChildAt(2);
                                    reminder = slot46.getChildAt(3);
                                    break;
                                }
                                case 56: {
                                    editor.putString("SLOT56_SUBJECT", subjectName);
                                    editor.putString("SLOT56_TEACHER", teacherName);
                                    editor.putString("SLOT56_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot56.getChildAt(0);
                                    teacher = slot56.getChildAt(1);
                                    room = slot56.getChildAt(2);
                                    reminder = slot56.getChildAt(3);
                                    break;
                                }
                                case 17: {
                                    editor.putString("SLOT17_SUBJECT", subjectName);
                                    editor.putString("SLOT17_TEACHER", teacherName);
                                    editor.putString("SLOT17_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot17.getChildAt(0);
                                    teacher = slot17.getChildAt(1);
                                    room = slot17.getChildAt(2);
                                    reminder = slot17.getChildAt(3);
                                    break;
                                }
                                case 27: {
                                    editor.putString("SLOT27_SUBJECT", subjectName);
                                    editor.putString("SLOT27_TEACHER", teacherName);
                                    editor.putString("SLOT27_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot27.getChildAt(0);
                                    teacher = slot27.getChildAt(1);
                                    room = slot27.getChildAt(2);
                                    reminder = slot27.getChildAt(3);
                                    break;
                                }
                                case 37: {
                                    editor.putString("SLOT37_SUBJECT", subjectName);
                                    editor.putString("SLOT37_TEACHER", teacherName);
                                    editor.putString("SLOT37_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot37.getChildAt(0);
                                    teacher = slot37.getChildAt(1);
                                    room = slot37.getChildAt(2);
                                    reminder = slot37.getChildAt(3);
                                    break;
                                }
                                case 47: {
                                    editor.putString("SLOT47_SUBJECT", subjectName);
                                    editor.putString("SLOT47_TEACHER", teacherName);
                                    editor.putString("SLOT47_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot47.getChildAt(0);
                                    teacher = slot47.getChildAt(1);
                                    room = slot47.getChildAt(2);
                                    reminder = slot47.getChildAt(3);
                                    break;
                                }
                                case 57: {
                                    editor.putString("SLOT57_SUBJECT", subjectName);
                                    editor.putString("SLOT57_TEACHER", teacherName);
                                    editor.putString("SLOT57_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot57.getChildAt(0);
                                    teacher = slot57.getChildAt(1);
                                    room = slot57.getChildAt(2);
                                    reminder = slot57.getChildAt(3);
                                    break;
                                }
                                case 18: {
                                    editor.putString("SLOT18_SUBJECT", subjectName);
                                    editor.putString("SLOT18_TEACHER", teacherName);
                                    editor.putString("SLOT18_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot18.getChildAt(0);
                                    teacher = slot18.getChildAt(1);
                                    room = slot18.getChildAt(2);
                                    reminder = slot18.getChildAt(3);
                                    break;
                                }
                                case 28: {
                                    editor.putString("SLOT28_SUBJECT", subjectName);
                                    editor.putString("SLOT28_TEACHER", teacherName);
                                    editor.putString("SLOT28_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot28.getChildAt(0);
                                    teacher = slot28.getChildAt(1);
                                    room = slot28.getChildAt(2);
                                    reminder = slot28.getChildAt(3);
                                    break;
                                }
                                case 38: {
                                    editor.putString("SLOT38_SUBJECT", subjectName);
                                    editor.putString("SLOT38_TEACHER", teacherName);
                                    editor.putString("SLOT38_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot38.getChildAt(0);
                                    teacher = slot38.getChildAt(1);
                                    room = slot38.getChildAt(2);
                                    reminder = slot38.getChildAt(3);
                                    break;
                                }
                                case 48: {
                                    editor.putString("SLOT48_SUBJECT", subjectName);
                                    editor.putString("SLOT48_TEACHER", teacherName);
                                    editor.putString("SLOT48_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot48.getChildAt(0);
                                    teacher = slot48.getChildAt(1);
                                    room = slot48.getChildAt(2);
                                    reminder = slot48.getChildAt(3);
                                    break;
                                }
                                case 58: {
                                    editor.putString("SLOT58_SUBJECT", subjectName);
                                    editor.putString("SLOT58_TEACHER", teacherName);
                                    editor.putString("SLOT58_ROOM", roomNo);
                                    editor.apply();
                                    subject = slot58.getChildAt(0);
                                    teacher = slot58.getChildAt(1);
                                    room = slot58.getChildAt(2);
                                    reminder = slot58.getChildAt(3);
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
                            if (room != null) {
                                ((TextView) room).setText(roomNo);
                            }
                            ET_editSlotRoom.setText("");
                            if (reminder != null){
                                reminder.setVisibility(View.VISIBLE);
                            }
                            AD_editSlot.cancel();
                        } else
                            Toast.makeText(TimeTableView.this, "Invalid room #", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(TimeTableView.this, "Invalid teacher name", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(TimeTableView.this, "Invalid subject name", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.slot11Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(11, subject, room, 1, 7, 45, 0);
                        editor.putBoolean("SLOT11_CHECKED", true);
                        editor.apply();
                    }
                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(11);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(21, subject, room, 2, 7, 45, 0);
                        editor.putBoolean("SLOT21_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(21);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(31, subject, room, 3, 7, 45, 0);
                        editor.putBoolean("SLOT31_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(31);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(41, subject, room, 4, 7, 45, 0);
                        editor.putBoolean("SLOT41_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(41);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(51, subject, room, 5, 7, 45, 0);
                        editor.putBoolean("SLOT51_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(51);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(12, subject, room, 1, 9, 30, 0);
                        editor.putBoolean("SLOT12_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(12);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(22, subject, room, 2, 9, 30, 0);
                        editor.putBoolean("SLOT22_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(22);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(32, subject, room, 3, 9, 30, 0);
                        editor.putBoolean("SLOT32_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(32);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(42, subject, room, 4, 9, 30, 0);
                        editor.putBoolean("SLOT42_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(42);
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
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(52, subject, room, 5, 9, 30, 0);
                        editor.putBoolean("SLOT52_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(52);
                    editor.putBoolean("SLOT52_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot13Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(13, subject, room, 1, 11, 15, 0);
                        editor.putBoolean("SLOT13_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(13);
                    editor.putBoolean("SLOT13_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot23Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(23, subject, room, 2, 11, 15, 0);
                        editor.putBoolean("SLOT23_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(23);
                    editor.putBoolean("SLOT23_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot33Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(33, subject, room, 3, 11, 15, 0);
                        editor.putBoolean("SLOT33_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(33);
                    editor.putBoolean("SLOT33_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot43Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(43, subject, room, 4, 11, 15, 0);
                        editor.putBoolean("SLOT43_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(43);
                    editor.putBoolean("SLOT43_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot53Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(53, subject, room, 5, 11, 15, 0);
                        editor.putBoolean("SLOT53_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(53);
                    editor.putBoolean("SLOT53_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot14Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(14, subject, room, 1, 13, 0, 0);
                        editor.putBoolean("SLOT14_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(14);
                    editor.putBoolean("SLOT14_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot24Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(24, subject, room, 2, 13, 0, 0);
                        editor.putBoolean("SLOT24_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(24);
                    editor.putBoolean("SLOT24_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot34Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(34, subject, room, 3, 13, 0, 0);
                        editor.putBoolean("SLOT14_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(34);
                    editor.putBoolean("SLOT34_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot44Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(44, subject, room, 4, 13, 0, 0);
                        editor.putBoolean("SLOT44_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(44);
                    editor.putBoolean("SLOT44_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot54Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(54, subject, room, 5, 13, 0, 0);
                        editor.putBoolean("SLOT54_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(54);
                    editor.putBoolean("SLOT54_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot15Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(15, subject, room, 1, 14, 45, 0);
                        editor.putBoolean("SLOT15_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(15);
                    editor.putBoolean("SLOT15_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot25Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(25, subject, room, 2, 14, 45, 0);
                        editor.putBoolean("SLOT25_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(25);
                    editor.putBoolean("SLOT25_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot35Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(35, subject, room, 3, 14, 45, 0);
                        editor.putBoolean("SLOT35_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(35);
                    editor.putBoolean("SLOT35_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot45Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(45, subject, room, 4, 14, 45, 0);
                        editor.putBoolean("SLOT45_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(45);
                    editor.putBoolean("SLOT45_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot55Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(55, subject, room, 5, 14, 45, 0);
                        editor.putBoolean("SLOT55_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(55);
                    editor.putBoolean("SLOT55_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot16Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(16, subject, room, 1, 16, 30, 0);
                        editor.putBoolean("SLOT16_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(16);
                    editor.putBoolean("SLOT16_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot26Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(26, subject, room, 2, 20, 13, 0);
                        editor.putBoolean("SLOT26_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(26);
                    editor.putBoolean("SLOT26_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot36Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(36, subject, room, 3, 16, 30, 0);
                        editor.putBoolean("SLOT36_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(36);
                    editor.putBoolean("SLOT36_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot46Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(46, subject, room, 4, 16, 30, 0);
                        editor.putBoolean("SLOT46_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(46);
                    editor.putBoolean("SLOT46_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot56Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(56, subject, room, 5, 16, 30, 0);
                        editor.putBoolean("SLOT56_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(56);
                    editor.putBoolean("SLOT56_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot17Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(17, subject, room, 1, 18, 15, 0);
                        editor.putBoolean("SLOT17_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(17);
                    editor.putBoolean("SLOT17_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot27Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(27, subject, room, 2, 18, 15, 0);
                        editor.putBoolean("SLOT27_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(27);
                    editor.putBoolean("SLOT27_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot37Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(37, subject, room, 3, 18, 15, 0);
                        editor.putBoolean("SLOT37_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(37);
                    editor.putBoolean("SLOT37_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot47Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(47, subject, room, 4, 18, 15, 0);
                        editor.putBoolean("SLOT47_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(47);
                    editor.putBoolean("SLOT47_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot57Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(57, subject, room, 5, 18, 15, 0);
                        editor.putBoolean("SLOT57_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(57);
                    editor.putBoolean("SLOT57_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot18Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(18, subject, room, 1, 20, 0, 0);
                        editor.putBoolean("SLOT18_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(18);
                    editor.putBoolean("SLOT18_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot28Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(28, subject, room, 2, 20, 0, 0);
                        editor.putBoolean("SLOT28_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(28);
                    editor.putBoolean("SLOT28_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot38Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(38, subject, room, 3, 20, 0, 0);
                        editor.putBoolean("SLOT38_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(38);
                    editor.putBoolean("SLOT38_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot48Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(48, subject, room, 4, 20, 0, 0);
                        editor.putBoolean("SLOT48_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(48);
                    editor.putBoolean("SLOT48_CHECKED", false);
                    editor.apply();
                }
                break;
            }
            case R.id.slot58Reminder: {
                if (((CheckBox) v).isChecked()) {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_checked));
                    String subject = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
                    String room = ((TextView) ((RelativeLayout) v.getParent()).getChildAt(2)).getText().toString();
                    if (!subject.isEmpty()) {
                        handleNotification(58, subject, room, 5, 20, 0, 0);
                        editor.putBoolean("SLOT58_CHECKED", true);
                        editor.apply();
                    }

                } else {
                    ((RelativeLayout) v.getParent()).setBackground(ContextCompat
                            .getDrawable(TimeTableView.this,
                                    R.drawable.timetable_slot_unchecked));
                    cancelSlotNotification(58);
                    editor.putBoolean("SLOT58_CHECKED", false);
                    editor.apply();
                }
                break;
            }
        }
    }

    private void takeScreenShot() {
        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.SV_parentView);
        if (scrollView != null) {
            bitmap = Bitmap.createBitmap(
                    scrollView.getChildAt(0).getWidth(),
                    scrollView.getChildAt(0).getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas c = null;
            if (bitmap != null) {
                c = new Canvas(bitmap);
            }
            scrollView.getChildAt(0).draw(c);
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void handleNotification(int id, String subject, String room, int day, int hours, int minutes, int seconds) {
        Toast.makeText(TimeTableView.this, "Notification on", Toast.LENGTH_SHORT).show();
        Intent alarmIntent = new Intent(this, TimeTableReceiver.class);
        alarmIntent.putExtra("SUBJECT", subject);
        alarmIntent.putExtra("ROOM", room);
        alarmIntent.putExtra("ID", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        switch (day) {
            case 1: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            }
            case 2: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            }
            case 3: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            }
            case 4: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            }
            case 5: {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        //check whether the time is earlier than current time. If so, set it to next week.

        Calendar now = Calendar.getInstance();
        if (calendar.before(now)) {
            calendar.add(Calendar.DATE, 7);
            Toast.makeText(TimeTableView.this, "In Past", Toast.LENGTH_SHORT).show();
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    private void cancelSlotNotification(int id) {
        Toast.makeText(TimeTableView.this, "Notification off", Toast.LENGTH_SHORT).show();
        Intent alarmIntent = new Intent(this, TimeTableReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.timetableStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String applicationStatus = sharedPreferences.getString("APPLICATION_STATUS", "");
        if (applicationStatus.equals("TEACHER")) {
            hideSlotReminder();
            takeScreenShot();
            String imageString = getStringImage(bitmap);
            new TimetableModal(TimeTableView.this).uploadImage(imageString);
        }
    }

    void hideSlotReminder(){

        slot11Reminder.setVisibility(View.INVISIBLE);
        slot21Reminder.setVisibility(View.INVISIBLE);
        slot31Reminder.setVisibility(View.INVISIBLE);
        slot41Reminder.setVisibility(View.INVISIBLE);
        slot51Reminder.setVisibility(View.INVISIBLE);

        slot12Reminder.setVisibility(View.INVISIBLE);
        slot22Reminder.setVisibility(View.INVISIBLE);
        slot32Reminder.setVisibility(View.INVISIBLE);
        slot42Reminder.setVisibility(View.INVISIBLE);
        slot52Reminder.setVisibility(View.INVISIBLE);

        slot13Reminder.setVisibility(View.INVISIBLE);
        slot23Reminder.setVisibility(View.INVISIBLE);
        slot33Reminder.setVisibility(View.INVISIBLE);
        slot43Reminder.setVisibility(View.INVISIBLE);
        slot53Reminder.setVisibility(View.INVISIBLE);

        slot14Reminder.setVisibility(View.INVISIBLE);
        slot24Reminder.setVisibility(View.INVISIBLE);
        slot34Reminder.setVisibility(View.INVISIBLE);
        slot44Reminder.setVisibility(View.INVISIBLE);
        slot54Reminder.setVisibility(View.INVISIBLE);

        slot15Reminder.setVisibility(View.INVISIBLE);
        slot25Reminder.setVisibility(View.INVISIBLE);
        slot35Reminder.setVisibility(View.INVISIBLE);
        slot45Reminder.setVisibility(View.INVISIBLE);
        slot55Reminder.setVisibility(View.INVISIBLE);

        slot16Reminder.setVisibility(View.INVISIBLE);
        slot26Reminder.setVisibility(View.INVISIBLE);
        slot36Reminder.setVisibility(View.INVISIBLE);
        slot46Reminder.setVisibility(View.INVISIBLE);
        slot56Reminder.setVisibility(View.INVISIBLE);

        slot17Reminder.setVisibility(View.INVISIBLE);
        slot27Reminder.setVisibility(View.INVISIBLE);
        slot37Reminder.setVisibility(View.INVISIBLE);
        slot47Reminder.setVisibility(View.INVISIBLE);
        slot57Reminder.setVisibility(View.INVISIBLE);

        slot18Reminder.setVisibility(View.INVISIBLE);
        slot28Reminder.setVisibility(View.INVISIBLE);
        slot38Reminder.setVisibility(View.INVISIBLE);
        slot48Reminder.setVisibility(View.INVISIBLE);
        slot58Reminder.setVisibility(View.INVISIBLE);
    }
}
