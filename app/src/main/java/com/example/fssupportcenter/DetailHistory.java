package com.example.fssupportcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.fssupportcenter.Object.ObjectInfoUser;
import com.example.fssupportcenter.Object.ObjectProfileCenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailHistory extends AppCompatActivity implements OnMapReadyCallback{

    TextView time,location,user_name,user_birthday,user_address,user_idcard,user_blood,user_mail,user_phone,back;
    String timeUser,latitudeUser,longitudeUser,idUser,nameUser,birthdayUser,addressUser,idcardUser,bloodUser,mailUser,phoneUser;
    FloatingActionButton call;
    List<ObjectInfoUser> infoUsersList;
    GoogleMap map;
    SupportMapFragment mapFragment;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        mapFragment.getMapAsync( this);
        getPutExtraHistory();
        getInforUser();
        setEvent();
    }

    public void setEvent(){
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneUser, null));
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_ToHistory = new Intent(getApplicationContext(),History.class);
                startActivity(intent_ToHistory);
            }
        });
        activeItemBottomNavigation();
    }
    public void getPutExtraHistory(){
        Bundle extras = getIntent().getExtras();
        timeUser = extras.getString("TIMEUSER");
        latitudeUser = extras.getString("LATITUDEUSER");
        longitudeUser = extras.getString("LONGITUDEUSER");
        idUser = extras.getString("IDUSER");

    }

    public void getInforUser(){
        mDatabase.child("InfoUser").child(idUser).child("Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ObjectInfoUser info = new ObjectInfoUser();
                    info.setAddress(snapshot.child("address").getValue(String.class));
                    info.setBirthday(snapshot.child("birthday").getValue(String.class));
                    info.setBlood(snapshot.child("blood").getValue(String.class));
                    info.setEmail(snapshot.child("email").getValue(String.class));
                    info.setIdcard(snapshot.child("idcard").getValue(String.class));
                    info.setName(snapshot.child("name").getValue(String.class));
                    info.setNote(snapshot.child("note").getValue(String.class));
                    info.setPhone(snapshot.child("phone").getValue(String.class));

                    nameUser = info.getName();
                    birthdayUser = info.getBirthday();
                    addressUser = info.getAddress();
                    idcardUser = info.getIdcard();
                    mailUser = info.getEmail();
                    bloodUser = info.getBlood();
                    phoneUser = info.getPhone();
                }
                setInfoHistory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setInfoHistory(){
        time.setText(timeUser);
        location.setText(latitudeUser+","+longitudeUser);
        user_name.setText(nameUser);
        user_birthday.setText(birthdayUser);
        user_address.setText(addressUser);
        user_idcard.setText(idcardUser);
        user_mail.setText(mailUser);
        user_blood.setText(bloodUser);
        user_phone.setText(phoneUser);
    }
    public void AnhXa(){
        time = findViewById(R.id.time_user);
        location = findViewById(R.id.location_user);
        user_name = findViewById(R.id.name_user);
        user_birthday = findViewById(R.id.birthday_user);
        user_address = findViewById(R.id.address_user);
        user_idcard = findViewById(R.id.idcard_user);
        user_blood = findViewById(R.id.blood_user);
        user_mail = findViewById(R.id.email_user);
        user_phone = findViewById(R.id.phone_user);
        back = findViewById(R.id.backToHistory);
        call = findViewById(R.id.call);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_history);
        infoUsersList = new ArrayList<>();
    }
    public void activeItemBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigate);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileCenter.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeCenter.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        final LatLng latLng = new LatLng(Double.parseDouble(latitudeUser),Double.parseDouble(longitudeUser));
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                map.addMarker(new MarkerOptions().title("user's location").position(latLng));
            }
        });
    }

    @Override
    public void onBackPressed(){

    }
}