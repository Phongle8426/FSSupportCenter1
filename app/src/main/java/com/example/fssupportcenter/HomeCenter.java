package com.example.fssupportcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.ghyeok.stickyswitch.widget.StickySwitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.fssupportcenter.LogInCenter.MyPREFERENCES;
import static com.example.fssupportcenter.ProfileCenter.CITYCENTER;
import static com.example.fssupportcenter.ProfileCenter.MyPREFERENCESPROFILE;
import static com.example.fssupportcenter.ProfileCenter.TYPECENTER;

public class HomeCenter extends AppCompatActivity implements Animation.AnimationListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "helo";
    StickySwitch stickySwitch;
    LottieAnimationView finding;
    ImageView online;
    TextView textNotificate1;
    Button setting;
    Animation animSlideRight,animRotateSnip;
    private String uidCenter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferencesSaveAccount,sRInforCenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_home_center);
        mAuth =FirebaseAuth.getInstance();
        sharedpreferencesSaveAccount = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sRInforCenter = getSharedPreferences(MyPREFERENCESPROFILE, Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Anhxa();
      //  finding.setVisibility(View.INVISIBLE);
        setEvent();
        getUIDCenter();
    }

    public void getUIDCenter(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uidCenter = user.getUid();
    }
    public void activeStatusOn(){
        String center_Type =sRInforCenter.getString(TYPECENTER,"");
        String center_City =sRInforCenter.getString(CITYCENTER,"");
        mDatabase.child("InformationCenter").child(center_Type).child(center_City).child(uidCenter).child("center_status").setValue("true");
    }
    public void activeStatusOff(){
        String center_Type =sRInforCenter.getString(TYPECENTER,"");
        String center_City =sRInforCenter.getString(CITYCENTER,"");
        mDatabase.child("InformationCenter").child(center_Type).child(center_City).child(uidCenter).child("center_status").setValue("false");
    }
    public void showSetting(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }
    public void sigOut(){
        mAuth.signOut();
        clearData();
        Intent intent_toLogin = new Intent(HomeCenter.this,LogInCenter.class);
        startActivity(intent_toLogin);
    }

    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferencesSaveAccount.edit();
        editor.clear();
        editor.commit();
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
        animSlideRight= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anime_slide_right);
        animSlideRight.setAnimationListener((Animation.AnimationListener) this);
        animRotateSnip = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anime_rotate_snip);
        animRotateSnip.setAnimationListener((Animation.AnimationListener) this);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                if (direction.name()=="RIGHT"){
                    stickySwitch.setSwitchColor(Color.parseColor("#FF4CAF50"));
                    textNotificate1.setText("You are ready to receive the request... ");
                    textNotificate1.setTextColor(Color.parseColor("#FF4CAF50"));
                    textNotificate1.setVisibility(View.VISIBLE);
                    textNotificate1.startAnimation(animSlideRight);
                    activeStatusOn();
                    online.setImageResource(R.drawable.ic_online);
                }
                else{
                    stickySwitch.setSwitchColor(Color.parseColor("#FFF43C2F"));
                    textNotificate1.setText("You aren't ready to receive the request.");
                    textNotificate1.setTextColor(Color.parseColor("#FFF43C2F"));
                    textNotificate1.setVisibility(View.VISIBLE);
                    textNotificate1.startAnimation(animSlideRight);
                    activeStatusOff();
                    online.setImageResource(R.drawable.ic_offline);
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


    public void Anhxa(){
        stickySwitch = findViewById(R.id.sticky_switch);
        online = findViewById(R.id.dotOnline);
        textNotificate1 = findViewById(R.id.text1);
        setting = findViewById(R.id.btn_setting);
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
        switch (menuItem.getItemId()){
            case R.id.item1:
                sigOut();
                return true;
            default: return false;
        }
    }
}