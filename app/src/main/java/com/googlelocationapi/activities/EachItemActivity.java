package com.googlelocationapi.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.googlelocationapi.R;


public class EachItemActivity extends AppCompatActivity implements LocationListener {

    double longitude;
    double latitude;
    double currentLatitude;
    double currentLongitude;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_item);

        ImageView imageView = (ImageView) findViewById(R.id.imageButton);
        TextView name = (TextView) findViewById(R.id.name);
        TextView address = (TextView) findViewById(R.id.address);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingActivity);
        TextView latitudeTextView = (TextView) findViewById(R.id.latitude);
        TextView longitudeTextView = (TextView) findViewById(R.id.longitude);
        Button navigationButton = (Button) findViewById(R.id.navigateButton);

        final Intent extras = getIntent();
        name.setText(extras.getStringExtra("name"));
        address.setText(extras.getStringExtra("address"));

        ratingBar.setRating(extras.getFloatExtra("rating", 0));
        Picasso.with(this).load(extras.getStringExtra("icon")).into(imageView);

        latitude = extras.getDoubleExtra("latitude", 0.0);
        latitudeTextView.setText(String.valueOf(latitude));

        longitude = extras.getDoubleExtra("longitude", 0.0);
        longitudeTextView.setText(String.valueOf(longitude));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(EachItemActivity.this, "%%%%%%%%%%%%%%", Toast.LENGTH_LONG).show();
                if (ActivityCompat.checkSelfPermission(EachItemActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EachItemActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EachItemActivity.this, "if condition", Toast.LENGTH_LONG).show();
                    //TO DO: permissions
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(EachItemActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            onLocationChanged(location);
                            //Toast.makeText(EachItemActivity.this, "location change", Toast.LENGTH_LONG).show();
                        }
                        else {
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();
                        }
                        //Toast.makeText(EachItemActivity.this, "current location", Toast.LENGTH_LONG).show();
                        Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"&daddr="+latitude+","+longitude));
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        //Toast.makeText(this, ""+currentLongitude, Toast.LENGTH_LONG).show();
    }
}
