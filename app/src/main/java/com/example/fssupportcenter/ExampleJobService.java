package com.example.fssupportcenter;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import static com.example.fssupportcenter.getValueChange.CHANNEL_ID;

public class ExampleJobService extends Service {

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    private DatabaseReference mDatabase;
    UpdateLocation updateLocation = new UpdateLocation();
    Handler mHandler = new Handler();
    boolean check = false;
    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("Start")) {
            mHandler.postDelayed(new Runnable() {
                int i = 0;
                @Override
                public void run() {
                    if (!check){
                        updateLocation();
                       // Log.d("receiver","run: "+ i++);
                        Toast.makeText(ExampleJobService.this, ""+i, Toast.LENGTH_SHORT).show();
                        mHandler.postDelayed(this, 3000);
                    }
                }
            }, 3000);
        }
        if (intent.getAction().equals("Stop")){
            check = true;
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mHandler.removeCallbacks(null);
        Log.d("LogService","SERVICE HAS BEEN DESTROYED!!!");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, myLocation.class);
        intent.setAction(myLocation.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setSmallestDisplacement(30f);
    }
    public void findCurentLocationUser(){

        final String[] lati = new String[1];
        final String[] longi = new String[1];
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                       /* try {
                            Geocoder geocoder = new Geocoder(HomeCenter.this, Locale.getDefault());
                            List<Address> userLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                            latitudeUser = userLocation.get(0).getLatitude()+"";
                            longitudeUser =  userLocation.get(0).getLongitude()+"";


                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        //if(!lati[0].equals(location.getLatitude()+"")){
                            lati[0] = location.getLatitude()+"";
                         //   longi[0] = location.getLongitude()+"";
                       // }
                        Log.d("LogService", "runnnnnnn");
                        mDatabase.child("TestValue").child("idvalue").child("aaa").setValue(lati[0]);
                    }
                }
            });
        }
    }
}