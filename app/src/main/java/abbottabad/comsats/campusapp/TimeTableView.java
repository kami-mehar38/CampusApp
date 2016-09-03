package abbottabad.comsats.campusapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
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

    private RelativeLayout slot1;
    private AlertDialog AD_editSlot;
    private AlertDialog.Builder builder;
    private EditText ET_editSlotSubject;
    private EditText ET_editSlotTeacher;
    private Button btn_edit;
    private Button btn_cancelEditDialog;
    private Validation validation;
    private CheckBox slot1Reminder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_page);

        View view = LayoutInflater.from(TimeTableView.this).inflate(R.layout.edit_slot, null);
        builder = new AlertDialog.Builder(TimeTableView.this);

        ET_editSlotSubject = (EditText) view.findViewById(R.id.ET_editSlotSubject);
        ET_editSlotTeacher = (EditText) view.findViewById(R.id.ET_editSlotTeacher);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        btn_cancelEditDialog = (Button) view.findViewById(R.id.btn_cancelEditDialog);
        btn_cancelEditDialog.setOnClickListener(this);

        builder.setView(view);
        builder.setCancelable(false);
        AD_editSlot = builder.create();

        slot1 = (RelativeLayout) findViewById(R.id.slot1);
        if (slot1 != null) {
            slot1.setOnLongClickListener(this);
        }
        slot1Reminder = (CheckBox) findViewById(R.id.slot1Reminder);
        if (slot1Reminder != null) {
            slot1Reminder.setOnClickListener(this);
        }

        validation = new Validation();

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.slot1: {
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
                if (validation.validateSubjectName(subjectName)){
                    if (validation.validateTeacherName(teacherName)) {
                        View subject = slot1.getChildAt(0);
                        ((TextView) subject).setText(subjectName);
                        ET_editSlotSubject.setText("");
                        View teacher = slot1.getChildAt(1);
                        ((TextView) teacher).setText(teacherName);
                        ET_editSlotTeacher.setText("");
                        Toast.makeText(TimeTableView.this, "Slot edited", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(TimeTableView.this, "Invalid teacher name", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(TimeTableView.this, "Invalid subject name", Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.btn_cancelEditDialog: {
                AD_editSlot.cancel();
                ET_editSlotSubject.setText("");
                break;
            }
            case R.id.slot1Reminder: {
                if (((CheckBox)v).isChecked()){
                    //Toast.makeText(TimeTableView.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    String subject = ((TextView)((RelativeLayout)v.getParent()).getChildAt(0)).getText().toString();
                    String teacher = ((TextView)((RelativeLayout)v.getParent()).getChildAt(1)).getText().toString();
                    Toast.makeText(TimeTableView.this, subject, Toast.LENGTH_SHORT).show();
                    handleNotification();
                }
                break;
            }
        }
    }

    private void handleNotification() {
        Intent alarmIntent = new Intent(this, TimeTableReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 3);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
    }
}
