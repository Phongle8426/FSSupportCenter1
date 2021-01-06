package com.example.fssupportcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.ghyeok.stickyswitch.widget.StickySwitch;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.fssupportcenter.DialogReceiverNotification.LATITUDE1;
import static com.example.fssupportcenter.DialogReceiverNotification.LONGITUDE1;
import static com.example.fssupportcenter.DialogReceiverNotification.MyPREFERENCESINFORUSER;
import static com.example.fssupportcenter.DialogReceiverNotification.NAME1;
import static com.example.fssupportcenter.DialogReceiverNotification.PHONE1;
import static com.example.fssupportcenter.LogInCenter.MyPREFERENCES;
import static com.example.fssupportcenter.ProfileCenter.CITYCENTER;
import static com.example.fssupportcenter.ProfileCenter.MyPREFERENCESPROFILE;
import static com.example.fssupportcenter.ProfileCenter.TYPECENTER;
import static com.example.fssupportcenter.WelconeTeamCenter.CODE;
import static com.example.fssupportcenter.WelconeTeamCenter.IDCENTER;
import static com.example.fssupportcenter.WelconeTeamCenter.MyPREFERENCESLOGINTEAM;

public class HomeCenter extends AppCompatActivity implements Animation.AnimationListener, PopupMenu.OnMenuItemClickListener {
    StickySwitch stickySwitch;
    BottomNavigationView bottomNavigationView;
    LottieAnimationView lottie,lottie_mission;
    ImageView online;
    TextView textNotificate1,textNotificate2,text_mission;
    Button setting;
    Animation animSlideRight, animRotateSnip;
    FusedLocationProviderClient fusedLocationProviderClient;
    DialogReceiverNotification dialogReceiverNotification;
    public String IDCenter, IDTeam,latitude_receiver,longitude_receiver,phone_receiver,name_receiver,iduser_receiver;
    int count = 0;
    private String uidCenterTeam,uidcenter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    SharedPreferences sRInforUser,sharedPreferencesAcountTeam,srstatus;
    public static final String MySRSTATUS = "status";
    public static final String STATUS = "demo";
    static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_home_center);
        sharedPreferencesAcountTeam = getSharedPreferences(MyPREFERENCESLOGINTEAM, Context.MODE_PRIVATE);
        sRInforUser = getSharedPreferences(MyPREFERENCESINFORUSER, Context.MODE_PRIVATE);
        srstatus = getSharedPreferences(MySRSTATUS, Context.MODE_PRIVATE);
        uidCenterTeam = sharedPreferencesAcountTeam.getString(CODE,"");
        uidcenter = sharedPreferencesAcountTeam.getString(IDCENTER,"");
       // dialogReceiverNotification = new DialogReceiverNotification();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermission();
        Anhxa();
        loadData();
        animSlideRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anime_slide_right);
        animSlideRight.setAnimationListener((Animation.AnimationListener) this);
        animRotateSnip = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anime_rotate_snip);
        animRotateSnip.setAnimationListener((Animation.AnimationListener) this);
        lottie_mission.setVisibility(View.INVISIBLE);
        text_mission.setVisibility(View.INVISIBLE);
        setEvent();
        receive_notification();
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDataStatus();
        activeStatusOff();
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeCenter.this,Manifest.permission.ACCESS_FINE_LOCATION)
               || ActivityCompat.shouldShowRequestPermissionRationale(HomeCenter.this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeCenter.this);
                builder.setTitle("Grant those Permission");
                builder.setMessage("We will be use you LOCATION and COARSE LOCATION");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                HomeCenter.this,
                                new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                },
                                REQUEST_CODE
                        );
                    }
                });
                builder.setNegativeButton("Cancel",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else{
                ActivityCompat.requestPermissions(
                        HomeCenter.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        REQUEST_CODE
                );
            }
        }else{
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveData(String status){
        SharedPreferences.Editor editor = srstatus.edit();
        editor.putString(MySRSTATUS, status);
        editor.commit();
    }
    public String loadData(){
       // Toast.makeText(this, ""+srstatus.getString(MySRSTATUS,""), Toast.LENGTH_SHORT).show();
        return srstatus.getString(MySRSTATUS,"");
    }
    private void clearDataStatus() {
        SharedPreferences.Editor editor = srstatus.edit();
        editor.clear();
        editor.commit();
    }
    public void activeStatusOn() {
        try{
            mDatabase.child("CenterTeam").child(uidcenter).child("InforTeam").child(uidCenterTeam).child("status_active").setValue(true);
        }catch (Exception e){
        }
    }

    public void activeStatusOff() {
        try{
            mDatabase.child("CenterTeam").child(uidcenter).child("InforTeam").child(uidCenterTeam).child("status_active").setValue(false);
            mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").
                    child(uidCenterTeam).child("team_latitude").setValue("");
            mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").
                    child(uidCenterTeam).child("team_longitude").setValue("");
            mDatabase.child("CenterTeam").child(uidcenter).child("InforTeam").
                    child(uidCenterTeam).child("team_latitude").setValue("");
            mDatabase.child("CenterTeam").child(uidcenter).child("InforTeam").
                    child(uidCenterTeam).child("team_longitude").setValue("");
        }catch (Exception e){
        }
    }

    public void showSetting(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    public void sigOut() {
        clearData();
        clearDataStatus();
        Intent intent_toLogin = new Intent(HomeCenter.this, WelconeTeamCenter.class);
        intent_toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent_toLogin);
    }

    public void dialogLogOut(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Log Out!");
        dialog.setMessage("Do you want to exit?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sigOut();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }


    public void dialogNotification(){
        String name_receiver1 =sRInforUser.getString(NAME1,"");
        String phone_receiver1 =sRInforUser.getString(PHONE1,"");
        String latitude_receiver1 = sRInforUser.getString(LATITUDE1,"");
        String longitude_receiver1 = sRInforUser.getString(LONGITUDE1,"");
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Your Duty!");
        dialog.setMessage("Name: "+name_receiver1 + "\n"+
                            "Phone: "+phone_receiver1 +"\n"+
                            "Location: "+latitude_receiver1 +","+longitude_receiver1 + "\n"+
                                "See more detail in your history, please!");
        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                lottie_mission.setVisibility(View.INVISIBLE);
                text_mission.setVisibility(View.INVISIBLE);
                stickySwitch.setEnabled(true);
                SharedPreferences.Editor editor = sRInforUser.edit();
                editor.clear();
                editor.commit();
                mDatabase.child("CenterTeam").child(uidcenter).child("InforTeam").child(uidCenterTeam).child("status_active").setValue("true");
                mDatabase.child("CenterTeam").child(uidcenter).child("InforTeam").child(uidCenterTeam).child("Mission").child("status").setValue("false");
                mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").child(uidCenterTeam).child("name").setValue("");
                mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").child(uidCenterTeam).child("phone").setValue("");
                mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").child(uidCenterTeam).child("user_id").setValue("");
                mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").child(uidCenterTeam).child("user_latitude").setValue("");
                mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").child(uidCenterTeam).child("user_longitude").setValue("");
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedPreferencesAcountTeam.edit();
        editor.clear();
        editor.commit();
    }

    public void activeItemBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
//                    case R.id.profile:
//                        startActivity(new Intent(getApplicationContext(), ProfileCenter.class));
//                        overridePendingTransition(0, 0);
//                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
    }

    public void receiveData() {
        mDatabase.child("CenterTeam").child(uidcenter).child("InforTeam").child(uidCenterTeam).child("Mission").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue().toString().equals("true")){
                    getData();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getData() {
        mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").child(uidCenterTeam).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                     latitude_receiver = snapshot.child("user_latitude").getValue(String.class); //thay bang objec
                     longitude_receiver = snapshot.child("user_longitude").getValue(String.class);
                     phone_receiver = snapshot.child("phone").getValue(String.class);//thay bang object
                     name_receiver = snapshot.child("name").getValue(String.class);
                     iduser_receiver = snapshot.child("user_id").getValue(String.class);
                    Bundle bundle = new Bundle(); //Bundle containing data you are passing to the dialog
                    bundle.putString("NAME", name_receiver);
                    bundle.putString("PHONE", phone_receiver);
                    bundle.putString("LATITUDE", latitude_receiver);
                    bundle.putString("LONGITUDE", longitude_receiver);
                    bundle.putString("USERID",iduser_receiver);
                 //   dialogReceiverNotification.show(getSupportFragmentManager(), "receiver");
//                    dialogReceiverNotification = new DialogReceiverNotification();
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    Fragment oldFragment = getSupportFragmentManager().findFragmentByTag("receiver");
//                    if (oldFragment != null){
//                        transaction.remove(oldFragment).commit();
//                    }
//                    transaction.addToBackStack(null);
//                    if(dialogReceiverNotification != null && dialogReceiverNotification.isAdded()){
//                        return;
//                    }
                   // dialogReceiverNotification.show(getSupportFragmentManager(), "receiver");
//                    dialogReceiverNotification.setArguments(bundle);
//                    if(dialogReceiverNotification.isStateSaved()){
//                        Log.d("loiii","loiiiiii");
//                        transaction.remove(dialogReceiverNotification).commit();
//
//                    }
//                    transaction.add(dialogReceiverNotification, "receiver");
//                    dialogReceiverNotification.setArguments(bundle);
//                    transaction.commitAllowingStateLoss();
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment oldFragment = fm.findFragmentByTag("fragment_tag");
                    if (oldFragment != null) {

                        fm.beginTransaction().remove(oldFragment).commit();
                    }
                    dialogReceiverNotification = new DialogReceiverNotification();
                    dialogReceiverNotification.name=name_receiver;
                    dialogReceiverNotification.phone = phone_receiver;
                    dialogReceiverNotification.latitude = latitude_receiver;
                    dialogReceiverNotification.longitude = longitude_receiver;
                    dialogReceiverNotification.iduser = iduser_receiver;
                    fm.beginTransaction().add(dialogReceiverNotification , "fragment_tag").commitAllowingStateLoss();
                  //  dialogReceiverNotification.setArguments(bundle);

                } else
                    Toast.makeText(HomeCenter.this, "Error snapshot data !", Toast.LENGTH_SHORT).show();
                mDatabase.child("CenterTeam").child(uidcenter).child("Transactions").child(uidCenterTeam).removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUIHome(StickySwitch.Direction direction){
        if (direction.name() == "RIGHT") {
            stickySwitch.setSwitchColor(Color.parseColor("#FF4CAF50"));
            textNotificate1.setText("You are ready to receive the mission. ");
            textNotificate2.setText("CONNECTED");
            textNotificate1.setTextColor(Color.parseColor("#FF4CAF50"));
            bottomNavigationView.setBackgroundResource(R.drawable.rounded_connect);
            lottie.setAnimation(R.raw.healconnect);
            lottie.playAnimation();
            textNotificate1.setVisibility(View.VISIBLE);
            textNotificate2.setVisibility(View.VISIBLE);
            textNotificate1.startAnimation(animSlideRight);
            textNotificate2.startAnimation(animSlideRight);
            online.setImageResource(R.drawable.ic_online);
        } else {
            stickySwitch.setSwitchColor(Color.parseColor("#FFF43C2F"));
            textNotificate1.setText("You are not ready to receive the mission.");
            textNotificate2.setText("DISCONNECTED");
            textNotificate1.setTextColor(Color.parseColor("#FFF43C2F"));
            bottomNavigationView.setBackgroundResource(R.drawable.rounded);
            lottie.setAnimation(R.raw.heal);
            lottie.playAnimation();
            lottie.setRepeatMode(LottieDrawable.REVERSE);
            textNotificate1.setVisibility(View.VISIBLE);
            textNotificate2.setVisibility(View.VISIBLE);
            textNotificate1.startAnimation(animSlideRight);
            textNotificate2.startAnimation(animSlideRight);
            online.setImageResource(R.drawable.ic_offline);
        }
    }
    public void setEvent() {
        if (loadData().equals("RIGHT")){
            stickySwitch.setDirection(StickySwitch.Direction.RIGHT,true);
            setUIHome(stickySwitch.getDirection());
        }
        String name_receiver1 = sRInforUser.getString(NAME1,"");
        if (!name_receiver1.equals("")){
            stickySwitch.setEnabled(false);
            lottie_mission.setVisibility(View.VISIBLE);
            text_mission.setVisibility(View.VISIBLE);
            stickySwitch.setDirection(StickySwitch.Direction.RIGHT,true);
            setUIHome(stickySwitch.getDirection());
            lottie_mission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogNotification();
                }
            });
        }
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
               setUIHome(direction);
                if (direction.name() == "RIGHT"){
                    activeStatusOn();
                    saveData(direction.name());
                    Intent intent = new Intent(getApplicationContext(),ExampleJobService.class);
                    intent.setAction("Start");
                    startService(intent);
                }else{
                    activeStatusOff();
                    saveData(direction.name());
                    Intent intent1 = new Intent(getApplicationContext(),ExampleJobService.class);
                    intent1.setAction("Stop");
                    startService(intent1);
                }
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting.startAnimation(animRotateSnip);
                showSetting(view);

            }
        });
        activeItemBottomNavigation();
    }


    public void receive_notification(){
        try {
            String action = getIntent().getAction();
            if (action.equals("OK")){
                //saveData("true");
                mDatabase.child("CenterTeam").child(uidcenter).child("InforTeam").child(uidCenterTeam).child("status_active").setValue("false");
                Uri uri = Uri.parse("geo:0,0?q="+sRInforUser.getString(LATITUDE1,"") +","+sRInforUser.getString(LONGITUDE1,""));
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                lottie_mission.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogNotification();
                    }
                });
            }else{
                Toast.makeText(this, "You skipped this mission", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }

    }

    public void Anhxa() {
        stickySwitch = findViewById(R.id.sticky_switch);
        online = findViewById(R.id.dotOnline);
        textNotificate1 = findViewById(R.id.text1);
        textNotificate2 = findViewById(R.id.text_disconnect);
        text_mission = findViewById(R.id.text_mission);
        setting = findViewById(R.id.btn_setting);
        bottomNavigationView = findViewById(R.id.bottom_navigate);
        lottie = findViewById(R.id.lottie);
        lottie_mission = findViewById(R.id.lottie_mission);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item1:
                dialogLogOut();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed(){
        count++;
        if (count == 2)
            dialogLogOut();
    }
}