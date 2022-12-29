package com.example.oderingfood;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class addBookingActivity extends AppCompatActivity {

    TextInputEditText edt_name;
    TextInputEditText edt_phone;
    TextInputEditText edt_date;
    TextInputEditText edt_timeStart;
    TextInputEditText edt_timeEnd;
    Button btn_order;
    private ActivityResultLauncher<Intent> mAcResultLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();


                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_table);
        edt_name = findViewById(R.id.booking_edt_name);
        edt_phone = findViewById(R.id.booking_edt_phone);
        edt_date = findViewById(R.id.booking_edt_date);
        edt_timeStart = findViewById(R.id.booking_edt_timeStart);
        edt_timeEnd = findViewById(R.id.booking_edt_timeEnd);
        btn_order = findViewById(R.id.booking_btn_book);

        edt_date.setInputType(InputType.TYPE_NULL);
        edt_timeStart.setInputType(InputType.TYPE_NULL);
        edt_timeEnd.setInputType(InputType.TYPE_NULL);



        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edt_name.getText().toString();
                String phone = edt_phone.getText().toString();
                String date = edt_date.getText().toString();
                String timeStart = edt_timeStart.getText().toString();
                String timeEnd = edt_timeEnd.getText().toString();

                if (ten.matches("") || phone.matches("")) {
                    Toast.makeText(addBookingActivity.this, "Không để trống các trường thông tin.", Toast.LENGTH_SHORT).show();
                    try {
                        Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                        LocalTime t1 = LocalTime.parse(timeStart);
                        LocalTime t2 = LocalTime.parse(timeEnd);

                    } catch (ParseException e) {
                        Log.e("===============================", "err: " + e);
                    }

                } else {
                    Intent intent = new Intent(addBookingActivity.this, activityBookingChooseTable.class);
                    Bundle b = new Bundle();
                    b.putBoolean("isBooking", true);
                    b.putString("ten", ten); //Your id
                    b.putString("phone", phone);
                    date = String.join("-",date.split("/"));
                    b.putString("date", date);
                    b.putString("timeS", timeStart);
                    b.putString("timeE", timeEnd);
                    intent.putExtra("bd", b);
                    startActivity(intent);
                    finish();
                }
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        edt_date.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));

        edt_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();

                } else {


                }
            }
        });
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        edt_timeStart.setText(simpleTimeFormat.format(Calendar.getInstance().getTime()));

        edt_timeStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showTimeStartPickerDialog();

                } else {


                }
            }
        });
        edt_timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeStartPickerDialog();
            }
        });

        edt_timeEnd.setText(simpleTimeFormat.format(Calendar.getInstance().getTime()));

        edt_timeEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showTimeEndPickerDialog();

                } else {


                }
            }
        });
        edt_timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeEndPickerDialog();
            }
        });



    }
    private void showDatePickerDialog(){
        Calendar calender = Calendar.getInstance();
        int year = calender.get(calender.YEAR);
        int month = calender.get(calender.MONTH);
        int day = calender.get(calender.DATE);
        String previousDate = edt_date.getText().toString();

        DatePickerDialog datePickerDialog = new DatePickerDialog(addBookingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calender.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String newDate = simpleDateFormat.format(calender.getTime());
                edt_date.setText(newDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimeStartPickerDialog(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(addBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mcurrentTime.set(Calendar.HOUR_OF_DAY,selectedHour);
                mcurrentTime.set(Calendar.MINUTE,selectedMinute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String newTime = simpleDateFormat.format(mcurrentTime.getTime());
                edt_timeStart.setText(newTime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();
    }
    private void showTimeEndPickerDialog(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(addBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mcurrentTime.set(Calendar.HOUR_OF_DAY,selectedHour);
                mcurrentTime.set(Calendar.MINUTE,selectedMinute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String newTime = simpleDateFormat.format(mcurrentTime.getTime());
                edt_timeEnd.setText(newTime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();
    }
}