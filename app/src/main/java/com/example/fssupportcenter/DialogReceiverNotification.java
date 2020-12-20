package com.example.fssupportcenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.fssupportcenter.Object.ObjectHistoryTeam;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import static com.example.fssupportcenter.WelconeTeamCenter.IDCENTER;
import static com.example.fssupportcenter.WelconeTeamCenter.CODE;
import static com.example.fssupportcenter.WelconeTeamCenter.MyPREFERENCESLOGINTEAM;

public class DialogReceiverNotification extends AppCompatDialogFragment {
    TextView name_user,phone_user,location_user;
    String name="",phone="",latitude="",longitude="",iduser="";
    private String uidCenterTeam,centerName;
    SharedPreferences sharedPreferencesAcountTeam,sRInforUser;
    public static final String MyPREFERENCESINFORUSER = "shareInforUser";
    public static final String NAME1 = "name";
    public static final String PHONE1 = "phone";
    public static final String LATITUDE1 = "lat";
    public static final String LONGITUDE1 = "long";


    MediaPlayer player;
    private DatabaseReference mDatabase;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_receiver_notification,null);
        sharedPreferencesAcountTeam = getContext().getSharedPreferences(MyPREFERENCESLOGINTEAM, Context.MODE_PRIVATE);
        sRInforUser = getContext().getSharedPreferences(MyPREFERENCESINFORUSER, Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uidCenterTeam = sharedPreferencesAcountTeam.getString(CODE,"");
        centerName = sharedPreferencesAcountTeam.getString(IDCENTER,"");
        play();
        builder.setView(view).setTitle("New Mission!").setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stop();
                dialogInterface.cancel();
            }
        })
                .setPositiveButton("See", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stop();
                        saveHistory(iduser,name,latitude,longitude);
                        putInforUser();
                        Intent intent = new Intent(getContext(),HomeCenter.class);
                        intent.setAction("OK");
                        startActivity(intent);
                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                    }
                });
        builder.setCancelable(false);
        name_user = view.findViewById(R.id.name_receive);
        phone_user = view.findViewById(R.id.phone_receive);
        location_user = view.findViewById(R.id.location_receive);
        getInforReceiver();
        return builder.create();
    }

    // Lưu thông tin của user vào shareReferences
    public void putInforUser(){
        SharedPreferences.Editor editor = sRInforUser.edit();
        //editor.putString(STATUSMISSION, "OK");
        editor.putString(NAME1, name);
        editor.putString(PHONE1, phone);
        editor.putString(LATITUDE1, latitude);
        editor.putString(LONGITUDE1, longitude);
        editor.commit();
    }


    public void play() {
        if (player == null) {
            player = MediaPlayer.create(getContext(), R.raw.thethai);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        player.start();
    }
    public void stop() {
        stopPlayer();
    }
    public void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
    public void saveHistory(String id_user,String username,String latitude, String longitude){
        DateFormat df = new SimpleDateFormat("HH'h'mm'-'dd/MM/yy");
        String date = df.format(Calendar.getInstance().getTime());
        ObjectHistoryTeam historyTeam = new ObjectHistoryTeam(date,id_user,username,latitude,longitude);
        mDatabase.child("CenterTeam").child(centerName).child("History").child(uidCenterTeam).push().setValue(historyTeam);
    }
    public void getInforReceiver(){
        try{
            Bundle bundle = getArguments();
            name = bundle.getString("NAME","");
            phone = bundle.getString("PHONE","");
            latitude = bundle.getString("LATITUDE","");
            longitude = bundle.getString("LONGITUDE","");
            iduser = bundle.getString("USERID","");
        }catch (NullPointerException ignored){}
            name_user.setText(name);
            phone_user.setText(phone);
            location_user.setText(latitude +","+ longitude);
    }

}
