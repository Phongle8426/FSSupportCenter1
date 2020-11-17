package com.example.fssupportcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.fssupportcenter.Object.ObjectLocationCenter;
import com.example.fssupportcenter.Object.ObjectProfileCenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class ProfileCenter extends AppCompatActivity implements OnMapReadyCallback {

    String[] COUNTRIES = new String[] {"Police", "Hospital", "Fire"};
    GoogleMap map;
    SupportMapFragment mapFragment;
    TextInputEditText centerName,centerMail,centerPhone,centerAddress;
    Button confirm,btn_confirm_address;
    AutoCompleteTextView centerType;
    private String uidCenter,centerName1,centerPhone1,centerMail1,centerType1,centerAddress1,longitude,latitude,namecity;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public static final String MyPREFERENCES = "MyPrefs"; //biến lưu của cặp user/password
    public static final String TYPECENTER = "typeKey";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_center);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Anhxa();
        mapFragment.getMapAsync(this);
        getUIDCenter();
        setMenuTypeCenter();
        setEvent();
        getInformationCenter();

    }

    // hàm lấy ID của center
    public void getUIDCenter(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uidCenter = user.getUid();
    }

    // hàm lấy các thông tin mà người dùng nhập vào text field
    public void getInforField(){
        centerName1 = centerName.getText().toString();
        centerPhone1 = centerPhone.getText().toString();
        centerMail1 = centerMail.getText().toString();
        centerType1 = centerType.getText().toString();
        centerAddress1 = centerAddress.getText().toString();
    }
    // hàm lưu trữ Type center
    public void saveTypeKey(String type){
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(TYPECENTER,type);
        editor.commit();
    }
    //Hàm đẩy dữ liệu InformationCenter và CenterLocation lên Db và lưu Type center lại
    public void pushInformationCenter(){
        getInforField();
        ObjectProfileCenter objectProfileCenter =
                new ObjectProfileCenter(centerName1,centerMail1,centerPhone1,centerType1,centerAddress1);
        ObjectLocationCenter objectLocationCenter = new ObjectLocationCenter(latitude,longitude,namecity,centerName1,uidCenter);
        mDatabase.child("InformationCenter").child(centerType1).child(uidCenter).setValue(objectProfileCenter);
        mDatabase.child("CenterLocation").child(centerType1).child(namecity).child(uidCenter).setValue(objectLocationCenter);
        saveTypeKey(centerType1);
    }

    // Hàm lấy thông tin của center từ DB về hiển thị trên các field
    public void getInformationCenter(){
    mDatabase.child("InformationCenter").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists())
            showInformationCenter(snapshot);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }

    // Hàm lấy thông tin của center từ DB về hiển thị trên các field
    public void showInformationCenter(DataSnapshot snapshot){
        ObjectProfileCenter inforCenter = new ObjectProfileCenter();
        String center_Type =sharedPreferences.getString(TYPECENTER,"");
        inforCenter.setCenter_name(snapshot.child(center_Type).child(uidCenter).child("center_name").getValue(String.class));
        inforCenter.setCenter_email(snapshot.child(center_Type).child(uidCenter).child("center_email").getValue(String.class));
        inforCenter.setCenter_phone(snapshot.child(center_Type).child(uidCenter).child("center_phone").getValue(String.class));
        inforCenter.setCenter_type(snapshot.child(center_Type).child(uidCenter).child("center_type").getValue(String.class));
        inforCenter.setCenter_address(snapshot.child(center_Type).child(uidCenter).child("center_address").getValue(String.class));

        centerName.setText(inforCenter.getCenter_name());
        centerMail.setText(inforCenter.getCenter_email());
        centerPhone.setText(inforCenter.getCenter_phone());
        centerType.setText(inforCenter.getCenter_type());
        centerAddress.setText(inforCenter.getCenter_address());
    }

    // Hàm bắt các lỗi nhập
    public boolean catchError(){
        final String emailPattern =  "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        getInforField();
        if(centerName1.isEmpty() || centerMail1.isEmpty()||centerPhone1.isEmpty()||centerType1.isEmpty()||centerAddress1.isEmpty()){
            Toast.makeText(ProfileCenter.this, "Something is empty !", Toast.LENGTH_LONG).show();
            return true;
        }
        if (centerPhone1.length() != 10){
            Toast.makeText(ProfileCenter.this, "Invalid phone number!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (!centerMail1.matches(emailPattern)){
            Toast.makeText(ProfileCenter.this, "Invalid email", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (latitude == null){
            Toast.makeText(this, "Press button Search again, Please !", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    // hàm đặt các type center vào menu
    public void setMenuTypeCenter(){
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,R.id.item_menu,
                        COUNTRIES);
        centerType.setAdapter(adapter);
    }
    //Hàm thực hiện chuyển các TAB
    public void activeItemBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigate);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                         startActivity(new Intent(getApplicationContext(),HomeCenter.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        //startActivity(new Intent(getApplicationContext(),Contact.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }
    public void setEvent(){
        activeItemBottomNavigation();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (catchError()==false){
                    pushInformationCenter();
                    Toast.makeText(ProfileCenter.this, "Save information successful !", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void Anhxa(){
        centerName = findViewById(R.id.edtxt_centerName_profile);
        centerAddress = findViewById(R.id.edtxt_centerAddress_profile);
        centerMail = findViewById(R.id.edtxt_centerEmail_profile);
        centerPhone = findViewById(R.id.edtxt_centerPhone_profile);
        centerType = findViewById(R.id.filled_exposed_dropdown);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        confirm = findViewById(R.id.btn_centerRegis);
        btn_confirm_address = findViewById(R.id.btn_confirm_address);
    }

    // Hàm lấy thông tin location của center
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        btn_confirm_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final String location = centerAddress.getText().toString();
                    if (location == null || location.equals("")){
                        Toast.makeText(ProfileCenter.this, "Input your address, Please !", Toast.LENGTH_SHORT).show();
                    }else{
                        List<Address> addressList = null;

                        if (location != null || !location.equals("")) {
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                addressList = geocoder.getFromLocationName(location, 1);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Address address = addressList.get(0);
                            longitude = ""+address.getLongitude();
                            latitude = ""+address.getLatitude();
                            namecity = ""+address.getAdminArea();
                            final LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                                    map.addMarker(new MarkerOptions().title(location).position(latLng));
                                }
                            });
                        }
                    }
            }
        });
    }
}