package zamboanga.antao.pockethajj.Fragments.Admin;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import zamboanga.antao.pockethajj.DataUsers.AlarmReceiver;
import zamboanga.antao.pockethajj.R;
import zamboanga.antao.pockethajj.RegisterHajjGuideActivity;

import static java.util.Calendar.YEAR;

public class AddScheduleActivity extends AppCompatActivity {

    private EditText mSchedTitle;
    private EditText mSchedContent;
    private EditText mSchedTime;
    private EditText mSchedDate;

    private Button btnSaveSchedule;
    private Button btnCancelSchedule;

    private Toolbar mToolbar;

    private int mYear, mMonth, mDay, mHour, mMinute;

    final static int RQS_1 = 4;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mScheduleDatabase;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_addSchedule) ;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSchedTitle = (EditText) findViewById(R.id.etSchedTitle);
        mSchedContent = (EditText) findViewById(R.id.etSchedContent);
        mSchedTime = (EditText) findViewById(R.id.etSchedTime);
        mSchedDate = (EditText) findViewById(R.id.etSchedDate);

        btnSaveSchedule = (Button) findViewById(R.id.btnSchedSave);
        btnCancelSchedule = (Button) findViewById(R.id.btnSchedCancel);

        mSchedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get date from pickerdialog then set to editext

                final Calendar c = Calendar.getInstance();
                mYear = c.get(YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

                //final String month_name = month_date.format(c.getTime());

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        //mSchedDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String date = dateFormat.format(c.getTime());

                        mSchedDate.setText(date);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        mSchedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get time from pickerdialog then set to editext

                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                final SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm aa");
                //final String time = simpleDateFormat.format(c.getTime());


                TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        //mSchedTime.setText(hourOfDay + ":" + minute);
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);



                        String time = timeformat.format(c.getTime());

                        mSchedTime.setText(time);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();

            }
        });

        btnCancelSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set alarm and save to database

                //set alarm from edittext
                Calendar current = Calendar.getInstance();

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.YEAR, mYear);
                calendar.set(Calendar.MONTH, mMonth);
                calendar.set(Calendar.DAY_OF_MONTH, mDay);
                calendar.set(Calendar.HOUR, mHour);
                calendar.set(Calendar.MINUTE, mMinute);

                if (calendar.before(current)){
                    Toast.makeText(getApplicationContext(), "Invalid Date/Time", Toast.LENGTH_LONG).show();
                    //calendar.add(Calendar.DATE, 1);
                    Log.v("calendar", calendar.toString());

                } else {

                    setAlarm(calendar);
                    saveSchedule();
                }

            }
        });
    }

    private void setAlarm(Calendar targetCal) {

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class); //ALARM IS SET
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

        Log.d("calendar", targetCal.toString());

        Toast.makeText(getApplicationContext(), "Alarm Set", Toast.LENGTH_LONG).show();

    }

    private void saveSchedule() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Adding Schedule of Activities");
        mProgressDialog.setMessage("Please Wait...");

        String schedTitle = mSchedTitle.getText().toString().trim();
        String schedContent = mSchedContent.getText().toString().trim();
        String schedDate = mSchedDate.getText().toString().trim();
        String schedTime = mSchedTime.getText().toString().trim();

        if (!TextUtils.isEmpty(schedTitle) ||
             !TextUtils.isEmpty(schedContent) ||
                !TextUtils.isEmpty(schedDate) ||
                !TextUtils.isEmpty(schedTime)){

            mProgressDialog.show();
            saveScheduleToDabase();

        } else {
            mProgressDialog.hide();
            Toast.makeText(AddScheduleActivity.this, "Please check the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveScheduleToDabase() {

        String schedTitle = mSchedTitle.getText().toString().trim();
        String schedContent = mSchedContent.getText().toString().trim();
        String schedDate = mSchedDate.getText().toString().trim();
        String schedTime = mSchedTime.getText().toString().trim();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = current_user.getUid();

        mScheduleDatabase = FirebaseDatabase.getInstance().getReference().child("ScheduleOfActivities");

        HashMap<String, String> scheduleMap = new HashMap<>();
        scheduleMap.put("title", schedTitle);
        scheduleMap.put("content", schedContent);
        scheduleMap.put("date", schedDate);
        scheduleMap.put("time", schedTime);

        mScheduleDatabase.push().setValue(scheduleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(AddScheduleActivity.this, "Schedule Added", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    finish();
                }
            }
        });
    }
}