package com.example.fssupportcenter;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class UpdateLocation extends AppCompatActivity {

    static UpdateLocation instance;
    LocationRequest locationRequest;
    Handler mHandler = new Handler();
    FusedLocationProviderClient fusedLocationProviderClient;
    myLocation myBroadcastReceiver = new myLocation();
    TextView locationed;
    private DatabaseReference mDatabase;
    boolean check = false;
    String latLong;

    public static UpdateLocation getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location);
        locationed = findViewById(R.id.text);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.fssupportcenter.UPDATE_LOCATION");
        registerReceiver(myBroadcastReceiver, filter);
        instance = this;

        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                updateLocation();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(UpdateLocation.this, "Accept the location", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }

    public void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    public PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, myLocation.class);
        intent.setAction(myLocation.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void updateText(final String value) {
        UpdateLocation.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("LogService", "run: " + value);
                //mDatabase.child("TestValue").child("idvalue").child("aaa").setValue(value);
                // latLong = value;
                locationed.setText(value);
             /*   mHandler.postDelayed(new Runnable() {
                    //int i = 0;
                    @Override
                    public void run() {
                        if (!check){
                            Log.d("LogService", "run: " + value);
                            mHandler.postDelayed(this, 3000);
                        }
                    }
                }, 3000);*/
                //  Toast.makeText(UpdateLocation.this, value, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}