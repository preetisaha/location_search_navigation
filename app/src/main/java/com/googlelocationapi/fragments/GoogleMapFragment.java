package com.googlelocationapi.fragments;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;

import com.google.android.gms.maps.CameraUpdateFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.SearchView;

import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.googlelocationapi.Data.Geometry;
import com.googlelocationapi.Data.LocationLatLng;
import com.googlelocationapi.Data.ParcelablesData;
import com.googlelocationapi.Data.ResponseData;
import com.googlelocationapi.Data.Results;
import com.googlelocationapi.R;
import com.googlelocationapi.adapter.NamesAdapter;
import com.googlelocationapi.interfaces.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapFragment extends android.app.Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    FloatingActionButton fab1;
    CuboidButton cb;
    SearchView searchView;
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    double latitude;
    double longitude;
    double currentLatitude;
    double currentLongitude;
    GoogleMap googleMap;
    String userInput;
    ResponseData responseData;
    RecyclerView recyclerView;
    MapView mv;
    View view;
    Results results;
    FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    NamesAdapter adapter;
    ArrayList<ParcelablesData> parcelablesDataArrayList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> addressList = new ArrayList<>();
    ArrayList<LocationLatLng> locationList = new ArrayList<>();
    FusedLocationProviderClient  fusedLocationProviderClient;


    public static GoogleMapFragment newInstance(String param1, String param2) {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    onLocationChanged(location);
                }else{
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                }
            }
        });


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        searchView = (SearchView) getActivity().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                userInput = s;
                googleMap.clear();
                new FetchItems().execute();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        fab1 = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        fab1.setImageResource(R.drawable.window);

        cb = (CuboidButton) view.findViewById(R.id.atmNames);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) parcelablesDataArrayList);
                ListViewFragment listViewFragment = new ListViewFragment();
                listViewFragment.setArguments(bundle);

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mapContainer, listViewFragment);
                fragmentTransaction.commit();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        return view;
    }

    public void onButtonPressed(Uri uri) {}
    @Override
    public void onAttach(Context context) {super.onAttach(context);}
    @Override
    public void onDetach() {super.onDetach();}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TO DO: permissions
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        currentLongitude = location.getLongitude();
        currentLatitude = location.getLatitude();
    }

    // ---------------------------------------------- Inner Class Starts ---------------------------------------------
    class FetchItems extends AsyncTask<Void,Void,ResponseData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResponseData doInBackground(Void... voids) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create()).build();
            Service service = retrofit.create(Service.class);
            responseData = new ResponseData();
            try {
                Response<ResponseData> response = service.getComment("AIzaSyDnaTE-olVObMqhzNMJgqrp5rY7ZYpUNjU", userInput).execute();
                responseData = response.body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseData;
        }

        ParcelablesData pd;
        @Override
        protected void onPostExecute(ResponseData responseData) {
            ArrayList<Results> resultsList = responseData.getResults();
            for(int i = 0; i<resultsList.size(); i++){
                results = resultsList.get(i);
                addressList.add(results.getFormatted_address());
                nameList.add(results.getName());
                Geometry geometry = results.getGeometry();
                locationList.add(geometry.getLocation());
                pd = new ParcelablesData(resultsList);
            }
            parcelablesDataArrayList.add(pd);
            getLocationOnMarker(locationList, addressList, nameList);
            adapter = new NamesAdapter(nameList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
    // ---------------------------------------------- Inner Class Ends ---------------------------------------------//

    public void getLocationOnMarker(List<LocationLatLng> list, List<String> atmAddressList, List<String> atmNameList){

        for(int count=0; count<list.size(); count++){
            LocationLatLng location = list.get(count);
            latitude = location.getLat();
            longitude = location.getLng();
            String address = atmAddressList.get(count);
            String name = atmNameList.get(count);

            LatLng center = new LatLng(currentLatitude, currentLongitude);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(center).zoom(3).bearing(10).tilt(0).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .snippet("Address- "+ address).title("Name- "+ name)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dollar));

        }
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"&daddr="+latitude+","+longitude));
                startActivity(intent);
            }
        });
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mv = (MapView)view.findViewById(R.id.mapView);
        mv.onCreate(null);
        mv.onResume();
        mv.getMapAsync(this);
    }

}
