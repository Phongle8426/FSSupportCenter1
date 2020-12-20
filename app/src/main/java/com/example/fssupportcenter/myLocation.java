package com.example.fssupportcenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.fssupportcenter.WelconeTeamCenter.IDCENTER;
import static com.example.fssupportcenter.WelconeTeamCenter.CODE;
import static com.example.fssupportcenter.WelconeTeamCenter.MyPREFERENCESLOGINTEAM;

public class myLocation extends BroadcastReceiver {
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferencesAcountTeam;
    String centerName,uid;
    public myLocation() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static final String ACTION_PROCESS_UPDATE ="com.example.fssupportcenter.UPDATE_LOCATION";
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferencesAcountTeam = context.getSharedPreferences(MyPREFERENCESLOGINTEAM, Context.MODE_PRIVATE);
        uid = sharedPreferencesAcountTeam.getString(CODE,"");
        centerName = sharedPreferencesAcountTeam.getString(IDCENTER,"");
        if (intent !=null){
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if (result!=null){
                    Location location = result.getLastLocation();
                    mDatabase.child("CenterTeam").child(centerName).child("Transactions").
                            child(uid).child("team_latitude").setValue(String.valueOf(location.getLatitude()));
                    mDatabase.child("CenterTeam").child(centerName).child("Transactions").
                            child(uid).child("team_longitude").setValue(String.valueOf(location.getLongitude()));
            }
            }
        }
    }
}
