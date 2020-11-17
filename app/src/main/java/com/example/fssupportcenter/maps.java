package com.example.fssupportcenter;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class maps extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    EditText searchView;
    Button search;
    int PLACE_PICKER_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        searchView = findViewById(R.id.location);
        search = findViewById(R.id.goSearch);
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);


    }

    public void searchLocation() {
            String location = searchView.getText().toString();
            List<Address> addressList = null;

            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                //map.addMarker(new MarkerOptions().position(latLng).title(location));
               // map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_SHORT).show();

            }
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String location = searchView.getText().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    final  LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                            map.addMarker(new MarkerOptions().title(location).snippet("ddm tin trong quang trong").position(latLng));
                        }
                    });
                }
            }
        });
        }
    }