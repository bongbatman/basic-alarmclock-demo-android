package com.musicals.recurringalarms;

import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import androidx.work.Worker;

public class AlarmWorker extends Worker {
    private static final String CHANNEL_ID = "AlarmWorker";
    private static final String LOG_TAG = AlarmWorker.class.getSimpleName() ;

    @NonNull
    @Override
    public Result doWork() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
        .setContentText("Do Work Called")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("doWork()")
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat.from(getApplicationContext()).notify(1,builder.build());
        Log.d(LOG_TAG, "doWork: DO WORK CALLED");

        return Result.SUCCESS;
    }
}
