package com.example.fssupportcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.fssupportcenter.Modules.HistoryAdapterRecyclerView;
import com.example.fssupportcenter.Modules.RecyclerViewClickInterface;
import com.example.fssupportcenter.Object.ObjectHistoryTeam;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.fssupportcenter.WelconeTeamCenter.CODE;
import static com.example.fssupportcenter.WelconeTeamCenter.IDCENTER;
import static com.example.fssupportcenter.WelconeTeamCenter.MyPREFERENCESLOGINTEAM;

public class History extends AppCompatActivity implements RecyclerViewClickInterface {

    List<ObjectHistoryTeam> historyList;
    RecyclerView recyclerView;
    ImageView empty;
    HistoryAdapterRecyclerView adapterRecyclerView;
    private DatabaseReference mDatabase;
    SharedPreferences sRInforAccountTeam;
    public String uidCenterTeam,uidcenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sRInforAccountTeam = getSharedPreferences(MyPREFERENCESLOGINTEAM, Context.MODE_PRIVATE);
        uidCenterTeam = sRInforAccountTeam.getString(CODE,"");
        uidcenter = sRInforAccountTeam.getString(IDCENTER,"");
        AnhXa();
        setEvent();
        getListHistory();
    }

    public void newContactAdapterRecyclerView(){
        adapterRecyclerView = new HistoryAdapterRecyclerView(historyList,this);
    }

    public void getListHistory(){
        mDatabase.child("CenterTeam").child(uidcenter).child("History").child(uidCenterTeam)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    empty.setVisibility(View.INVISIBLE);
                    historyList.clear();
                    for(DataSnapshot data : snapshot.getChildren()){
                        ObjectHistoryTeam ds = data.getValue(ObjectHistoryTeam.class);
                        historyList.add(ds);
                    }
                    newContactAdapterRecyclerView();
                    recyclerView.setAdapter(adapterRecyclerView);
                    mDatabase.child("CenterTeam").child(uidcenter).child("History").child(uidCenterTeam).removeEventListener(this);
                }else{
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setEvent(){
        activeItemBottomNavigation();
    }
    public void AnhXa(){
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        empty = findViewById(R.id.empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyList = new ArrayList<>();
    }
    public void activeItemBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigate);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
//                    case R.id.profile:
//                        startActivity(new Intent(getApplicationContext(), ProfileCenter.class));
//                        overridePendingTransition(0, 0);
//                        return true;
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(),HomeCenter.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        String idUser = historyList.get(position).getUser_id();
        String latitudeUser = historyList.get(position).getUser_latitude();
        String longitudeUser = historyList.get(position).getUser_longitude();
        String time = historyList.get(position).getTime();
        Intent intent = new Intent(getApplicationContext(),DetailHistory.class);
        intent.putExtra("IDUSER",idUser);
        intent.putExtra("LATITUDEUSER",latitudeUser);
        intent.putExtra("LONGITUDEUSER",longitudeUser);
        intent.putExtra("TIMEUSER",time);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){

    }
}