package com.example.annse.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    public SensorManager sensormanager;
    public Sensor proxsensor;
    public Sensor acclsensor;
    public SensorEventListener proximityEvListener,acclevent;

    public TextView alarmtxt;
    public TimePicker alarmtime;
    public Intent intent;
    public PendingIntent pendInt;
    public AlarmManager alarmmang;
    public Button setalarm;
    public Button alarmoff;


    //public Ringtone ringtone;
    int count1=0;
    int count2=0;
    int status=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmtime = (TimePicker) findViewById(R.id.alarmclock);
        alarmmang = (AlarmManager) getSystemService(ALARM_SERVICE);
        setalarm = (Button) findViewById(R.id.button4);

        alarmtxt = (TextView) findViewById(R.id.textView);
        alarmoff=(Button)findViewById(R.id.button);

        sensormanager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proxsensor = sensormanager.getDefaultSensor(Sensor.TYPE_PROXIMITY);



        alarmoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainActivity.this, "COUNT2: ALARM OFF " + count2, Toast.LENGTH_LONG).show();

                proximityEvListener=new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {

                        if (sensorEvent.values[0] < proxsensor.getMaximumRange()) {

                            count2++;
                            if(count2==1) {
                                alarmmang.cancel(pendInt);
                                AlarmReceiver.ringtone.stop();
                                Toast.makeText(MainActivity.this, "COUNT2: ALARM OFF " + count2, Toast.LENGTH_LONG).show();
                                count2 = 0;
                            }

                        }

                       }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                };
                sensormanager.registerListener(proximityEvListener, proxsensor,2 * 1000 * 1000);

            }
        });





        setalarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, alarmtime.getCurrentHour());
                cal.set(Calendar.MINUTE, alarmtime.getCurrentMinute());
                int hour=alarmtime.getHour();
                int min=alarmtime.getMinute();
                String hourstr=String.valueOf(hour);
                String minstr=String.valueOf(min);

                alarmtxt.setText("ALARM SET FOR "+hourstr+":"+minstr);


                intent = new Intent(MainActivity.this, AlarmReceiver.class);
                pendInt = PendingIntent.getBroadcast(MainActivity.this, 1, intent, pendInt.FLAG_UPDATE_CURRENT);

                long time = (cal.getTimeInMillis() - (cal.getTimeInMillis() % 60000));

                alarmmang.set(AlarmManager.RTC_WAKEUP, time, pendInt);
                Toast.makeText(MainActivity.this, "ALARM SET FOR " +hourstr+":"+minstr, Toast.LENGTH_LONG).show();
                if (System.currentTimeMillis() > time) {
                    if (cal.AM_PM == 0)
                        time = time + (1000 * 60 * 60 * 12);
                    else
                        time = time + (1000 * 60 * 60 * 24);

                }
                //alarmmang.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendInt);



                proximityEvListener = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {


                        if (sensorEvent.values[0] < proxsensor.getMaximumRange()) {

                            count1++;


                            if (count1==2) {

                                alarmmang.cancel(pendInt);
                                AlarmReceiver.ringtone.stop();
                                //sendBroadcast(intent);
                                status=1;

                                Calendar cals = Calendar.getInstance();
                                cals.add(Calendar.MINUTE, 2);
                                int hours = cals.get(Calendar.HOUR);
                                int mins = cals.get(Calendar.MINUTE);
                                String hourst = String.valueOf(hours);
                                String minst = String.valueOf(mins);
                                long snoozeval = cals.getTimeInMillis();
                                alarmtxt.setText("ALARM SNOOZED TILL " + hourst + ":" + minst);
                                intent = new Intent(MainActivity.this, AlarmReceiver.class);
                                pendInt = PendingIntent.getBroadcast(MainActivity.this, 0, intent, pendInt.FLAG_UPDATE_CURRENT);
                                //pendInt = PendingIntent.getBroadcast(MainActivity.this, AlarmReceiver.REQUEST_CODE, intent, pendInt.FLAG_UPDATE_CURRENT);

                                Toast.makeText(MainActivity.this, "ALARM SNOOZED TILL: " + hourst + ":" + minst, Toast.LENGTH_LONG).show();
                                alarmmang.set(AlarmManager.RTC, snoozeval, pendInt);
                                Toast.makeText(MainActivity.this, "COUNT: " + count1, Toast.LENGTH_LONG).show();
                                count1=0;
                            }


                            //sensormanager.unregisterListener(proximityEvListener);


                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                };

                sensormanager.registerListener(proximityEvListener, proxsensor,2 * 1000 * 1000);




            }
        });




        }

       @Override
       protected void onPause(){
        super.onPause();
           sensormanager.unregisterListener(proximityEvListener);



    }
}
