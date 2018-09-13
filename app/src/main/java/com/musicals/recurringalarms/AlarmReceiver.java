package com.musicals.recurringalarms;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "AlarmWorker";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent.getAction().contains(MainActivity.ACTION_TRIGGER_ALARM)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentText("Alarm Trigger Broadcast Received")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("onReceive()")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            //get default ringtone uri here
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            if (alert == null){
                // alert is null, using backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (alert == null){
                    // alert backup is null, using 2nd backup
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }

            //create new intent to start controller activity even on lock screen
            Intent startActivityIntent = new Intent(context, AlarmControllerActivity.class);
            context.startActivity(startActivityIntent);

            //get ringtone from that uri here
            Ringtone ringtone = RingtoneManager.getRingtone(context, alert);


            //setting audio attributes is very important for firing up the alarm notification
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setLegacyStreamType(AudioManager.STREAM_ALARM)
                    .build();
            ringtone.setAudioAttributes(audioAttributes);

            //ringtone will not play unless you call this method
            ringtone.play();
            NotificationManagerCompat.from(context).notify(1,builder.build());
        }

    }
}
