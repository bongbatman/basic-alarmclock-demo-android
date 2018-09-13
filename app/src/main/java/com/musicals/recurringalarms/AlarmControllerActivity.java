package com.musicals.recurringalarms;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class AlarmControllerActivity extends AppCompatActivity {
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_controller);

        b = findViewById(R.id.button_cancel_alarm);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //not works while activity is shown on lock screen
                Toast.makeText(AlarmControllerActivity.this, "huh bc dismiss bhi karo ab", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //sets flag to wake up screen and dismiss keyguard
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }
}
