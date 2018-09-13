package com.musicals.recurringalarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    static public final String ACTION_TRIGGER_ALARM = "com.musicals.recurringalarms.alarmreceiver.ACTION_TRIGGER_ALARM";
    private static final String LOG_TAG = MainActivity.class.getSimpleName() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        /**
         * This pending intent open up a controller activity for alarm clock interaction
         * and also builds notification all with broadcast receiver
         * the app needs permission for WAKE_LOCK and DISABLE_KEYGUARD with
         * android:showOnLockScreen="true"
         * android:screenOrientation="sensorPortrait"
         * setting in manifest.
         * Then in the activity which we want to launch has some window manager flags in its onStart
         *getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
         *                 WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
         *                 WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
         *                 WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
         */
        Intent alarmActionIntent = new Intent(ACTION_TRIGGER_ALARM);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this,
        1, alarmActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Calender is used to set and get time for the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 45);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime()+1000,
//                    60000,
//                    alarmPendingIntent);
//        }

        if (alarmManager != null) {
//            sets general alarm
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//                    calendar.getTimeInMillis(),
//                    alarmPendingIntent);

//            sets alarm clock with system alarm clock icon
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), alarmPendingIntent), alarmPendingIntent);
            Log.i(LOG_TAG, "onCreate: ALARMCLOCK " + alarmManager.getNextAlarmClock().getTriggerTime());
        }


    }
}
