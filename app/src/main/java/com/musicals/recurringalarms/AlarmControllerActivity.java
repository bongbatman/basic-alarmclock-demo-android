package com.musicals.recurringalarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import static com.musicals.recurringalarms.MainActivity.ACTION_TRIGGER_ALARM;

public class AlarmControllerActivity extends AppCompatActivity {
    Button dismiss;
    Button snooze;
    AudioManager audioManager;
    AlarmManager alarmManager;
    PendingIntent alarmPendingIntent;
    private Handler myHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_controller);

        dismiss = findViewById(R.id.button_cancel_alarm);
        snooze = findViewById(R.id.button_snooze);

        //Intent to for alarm receiver
        Intent alarmActionIntent;
        alarmActionIntent = new Intent(this, AlarmReceiver.class);
        alarmActionIntent.setAction(ACTION_TRIGGER_ALARM);

        alarmPendingIntent = PendingIntent.getBroadcast(this,
                1, alarmActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //create new intent to start controller activity when alarm clock is clicked from settings
        Intent startActivityIntent = new Intent(this, MainActivity.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent startActivityPendingIntent = PendingIntent.getActivity(this, 2, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //get alarm manager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set alarm volume
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This dismisses the notification with id  = 1
                NotificationManagerCompat.from(AlarmControllerActivity.this).cancel(1);

                //set the volume of currently playing stream to lower and remove sound/vibrate
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

                //not works while activity is shown on lock screen
                Toast.makeText(AlarmControllerActivity.this, "huh bc dismiss bhi karo ab", Toast.LENGTH_SHORT).show();

                myHandler.removeCallbacks(mRunnable);

                finish();
            }
        });


        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first dismiss alarm
                NotificationManagerCompat.from(AlarmControllerActivity.this).cancel(1);

                //set the volume of currently playing stream to lower and remove sound/vibrate
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);


                //snooze for 3 minutes
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + (60000 * 3), null), alarmPendingIntent);

                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        //auto snooze alarm after 1 minute of inactivity
        mRunnable = new Runnable() {
            @Override
            public void run() {
                //first dismiss alarm
                NotificationManagerCompat.from(AlarmControllerActivity.this).cancel(1);

                //set the volume of currently playing stream to lower and remove sound/vibrate
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);


                //snooze for 3 minutes
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + (60000 * 3), null), alarmPendingIntent);

                finish();
            }
        };

        //tell handler to run runnable after 1 of inactivity
        myHandler = new Handler();
        myHandler.postDelayed(mRunnable, 60000);

        //sets flag to wake up screen and dismiss keyguard
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        myHandler.removeCallbacks(mRunnable);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        //remove all runnable callbacks if user dismisses alarm
        myHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
