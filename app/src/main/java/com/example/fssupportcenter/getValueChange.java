package com.example.fssupportcenter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class getValueChange extends AppCompatActivity {
    public static final String CHANNEL_ID = "exampleServiceChannel";
    private static final String TAG = "MainActivity";
    Button start,stop;
    Handler mHandler = new Handler();
    boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_value_change);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        createNotificationChannel();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ExampleJobService.class);
                intent.setAction("Start");
                startService(intent);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ExampleJobService.class);
                intent.setAction("Stop");
                startService(intent);
            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    public void Task(){
        mHandler.postDelayed(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                if (!check){
                    Log.d("LogService", "run: " + i++);
                    mHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

}