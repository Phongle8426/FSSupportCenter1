package com.example.fssupportcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fssupportcenter.Object.ObjectProfileCenter;
import com.example.fssupportcenter.Object.ObjectSupportCenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class WelconeTeamCenter extends AppCompatActivity {

    private DatabaseReference mDatabase;
    List<ObjectSupportCenter> centerList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    String[] nameList1 = {};
    String[] idList1 = {};
    TextInputEditText centerType,centerCity,centerName,inputCode;
    Button login;
    String centerTypeSelected=null,centerCitySelected=null,centerNameSelected=null;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCESLOGINTEAM = "sharedLoginTeam";
    public static final String IDCENTER = "center";
    public static final String CODE = "code";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcone_team_center);
        sharedPreferences = getSharedPreferences(MyPREFERENCESLOGINTEAM, Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        setEvent();
    }
    public void setEvent(){
        centerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSelectCenterType();
            }
        });

        centerCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSelectCenterCity();
            }
        });
        centerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCenter();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputCode();
            }
        });
    }

    public void saveData(String idcenter, String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IDCENTER, idcenter);
        editor.putString(CODE, code);
        editor.commit();
    }
    public void inputCode() {
        final String code = inputCode.getText().toString();
        if (!code.isEmpty() && centerCitySelected!=null && centerTypeSelected!=null && centerNameSelected!=null) {
            mDatabase.child("CenterTeam").child(centerNameSelected).child("InforTeam").orderByChild("code").equalTo(code)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        saveData(centerNameSelected,code);
                        Intent intent = new Intent(getApplicationContext(), HomeCenter.class);
                        startActivity(intent);
                    }else Toast.makeText(WelconeTeamCenter.this, "Code is not exists !", Toast.LENGTH_LONG).show();

                    mDatabase.child("CenterTeam").child(centerNameSelected).child("InforTeam").removeEventListener(this);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }

            });
        }else Toast.makeText(this, "Incorrect info !", Toast.LENGTH_LONG).show();
    }
    public void loadCenter(){
        if (centerTypeSelected==null || centerCitySelected == null){
            Toast.makeText(this, "Select center type and city,please!", Toast.LENGTH_SHORT).show();
        }else{
            final int[] i = {0};
            mDatabase.child("SupportCenter").child(centerTypeSelected).child(centerCitySelected).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    centerList.clear();
                    nameList.clear();
                    idList.clear();
                    Toast.makeText(WelconeTeamCenter.this, ""+snapshot.getKey(), Toast.LENGTH_SHORT).show();
                    if (snapshot.exists()){
                        for (DataSnapshot data : snapshot.getChildren()){
                            ObjectSupportCenter profile = data.getValue(ObjectSupportCenter.class);
                            centerList.add(profile);
                            nameList.add(centerList.get(i[0]).getCenter_name());
                            idList.add(centerList.get(i[0]).getCenter_id());
                            i[0]++;
                            //Toast.makeText(WelconeTeamCenter.this, ""+centerList.get(i[0]).getCenter_id(), Toast.LENGTH_SHORT).show();
                        }
                        nameList1 = nameList.toArray(nameList1);
                        idList1 = idList.toArray(idList1);
                        openDialogSelectCenterName();
                    }else
                        Toast.makeText(WelconeTeamCenter.this, "No have any center!", Toast.LENGTH_SHORT).show();
                    mDatabase.child("InformationCenter").child(centerTypeSelected).child(centerCitySelected).removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void openDialogSelectCenterType(){
        final String[] datas = {"Hospital","Police","Fire"};
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Center Type!");
        dialog.setSingleChoiceItems(datas, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                centerType.setText(datas[i]);
                centerTypeSelected = datas[i];
                dialogInterface.dismiss();
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }
    public void openDialogSelectCenterCity(){
        final String[] datas = {"Quang Binh","Hue","Đà Nẵng","Quang Nam"};
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Center City!");
        dialog.setSingleChoiceItems(datas, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                centerCity.setText(datas[i]);
                centerCitySelected = datas[i];
                dialogInterface.dismiss();
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }

    public void openDialogSelectCenterName(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Center City!");
        dialog.setSingleChoiceItems(nameList1, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                centerName.setText(nameList1[i]);
                centerNameSelected = idList1[i];
                dialogInterface.dismiss();
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }

    public void AnhXa(){
        centerType = findViewById(R.id.select_centerType);
        centerCity = findViewById(R.id.select_city);
        centerName = findViewById(R.id.select_nameCenter);
        inputCode = findViewById(R.id.input_pass);
        login = findViewById(R.id.confirm_log);
    }
}