package com.example.fssupportcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fssupportcenter.Modules.DirectionFinder;
import com.example.fssupportcenter.Modules.DirectionFinderListener;
import com.example.fssupportcenter.Modules.Route;
import com.example.fssupportcenter.Object.ObjectDistanceCenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Direction extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    Button direc;
    public int checkDistance,minDistance;
    private String currentLocation = "16.02531, 108.023142",finalIndex;;
    public List<ObjectDistanceCenter> distanceList;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        direc = findViewById(R.id.direction);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        distanceList = new ArrayList<>();
        direc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendRequest();
                getListcenter();
                toatlist();
            }
        });
    }

    public String mergLatLg(String latitude, String longitude){
        return latitude + "," + longitude;
    }
    public void toatlist(){
        sendRequest(currentLocation,mergLatLg(distanceList.get(0).getLatitude(),distanceList.get(0).getLongitude()));
        minDistance = checkDistance;
        for(ObjectDistanceCenter emplement : distanceList){
            sendRequest(currentLocation,mergLatLg(emplement.getLatitude(),emplement.getLongitude()));
            if (checkDistance < minDistance){
                minDistance = checkDistance;
                finalIndex = emplement.getName();
            }
        }

    }
    public void getListcenter(){
        mDatabase = mDatabase.child("CenterLocation").child("Police").child("QuangBing");
       mDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    distanceList.add(snap.getValue(ObjectDistanceCenter.class));
                }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void sendRequest(String origin, String destination ) {
        if (origin.isEmpty()) {
            return;
        }
        if (destination.isEmpty()) {
            return;
        }
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
            checkDistance = route.get(0).distance.value;
    }
}