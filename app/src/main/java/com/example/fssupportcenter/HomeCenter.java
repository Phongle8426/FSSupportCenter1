package com.example.fssupportcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.ghyeok.stickyswitch.widget.StickySwitch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeCenter extends AppCompatActivity {
    private static final String TAG = "helo";
    StickySwitch stickySwitch;
    LottieAnimationView finding;
    ImageView off,online;
    private String uidCenter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_home_center);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Anhxa();
        finding.setVisibility(View.INVISIBLE);
        setEvent();
        getUIDCenter();
    }

    public void getUIDCenter(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uidCenter = user.getUid();
    }
    public void activeStatusOn(){
        mDatabase.child("ActiveStatusCenter").child("Police").child(uidCenter).child("status").setValue("true");
    }
    public void activeStatusOff(){
        mDatabase.child("ActiveStatusCenter").child("Police").child(uidCenter).child("status").setValue("false");
    }

    public void activeItemBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigate);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileCenter.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        //startActivity(new Intent(getApplicationContext(),Contact.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
    }
    public void setEvent(){
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                if (direction.name()=="RIGHT"){
                    activeStatusOn();
                    off.setVisibility(View.INVISIBLE);
                    finding.setVisibility(View.VISIBLE);
                    online.setImageResource(R.drawable.ic_online);
                }
                else{
                    activeStatusOff();
                    finding.setVisibility(View.INVISIBLE);
                    off.setVisibility(View.VISIBLE);
                    online.setImageResource(R.drawable.ic_offline);
                }

            }
        });

        activeItemBottomNavigation();
    }


    public void Anhxa(){
        stickySwitch = findViewById(R.id.sticky_switch);
        finding = findViewById(R.id.find);
        off = findViewById(R.id.imgoff);
        online = findViewById(R.id.dotOnline);
    }
    }