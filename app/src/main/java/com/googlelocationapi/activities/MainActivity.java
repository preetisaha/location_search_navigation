package com.googlelocationapi.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.googlelocationapi.R;
import com.googlelocationapi.fragments.GoogleMapFragment;

public class MainActivity extends AppCompatActivity {

    Fragment mapFragment;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    FloatingActionButton fab1;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.setting);

        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setImageResource(R.drawable.window);

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setImageResource(R.drawable.google);

        fab.setOnClickListener(new View.OnClickListener() {
            boolean status = true;

            @Override
            public void onClick(View view) {
                if (status) {
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    status = false;
                } else {
                    fab1.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                    status = true;
                }
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        FragmentManager fm = getFragmentManager();
        mapFragment = fm.findFragmentById(R.id.mapContainer);
        if(mapFragment == null){
            mapFragment = new GoogleMapFragment();
        }
        fm.beginTransaction().add(R.id.mapContainer, mapFragment).commit();
    }
}
