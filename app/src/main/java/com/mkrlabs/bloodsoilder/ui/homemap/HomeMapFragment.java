package com.mkrlabs.bloodsoilder.ui.homemap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;

import java.security.Permission;


public class HomeMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Map";
    private String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermission;
    private GoogleMap googleMap;
    private int REQUEST_CODE = 1001;
    private float DEFAULT_ZOOM = 16f;

    private FusedLocationProviderClient fusedLocationProviderClient;

    public HomeMapFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocationPermission();
    }


    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.homeMapFragment);
        supportMapFragment.getMapAsync(this);
    }


    private void getLocationPermission() {


        String permission[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLocationPermission = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permission, REQUEST_CODE);
            }

        } else {
            ActivityCompat.requestPermissions(getActivity(), permission, REQUEST_CODE);
        }


    }

    private void getDeviceCurrentLocation() {

        Log.d(TAG, "getDeviceCurrentLocation ");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try {
            Task<Location> task =  fusedLocationProviderClient.getLastLocation();

            task.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()){
                        Location userLocation = task.getResult();

                        if (userLocation!=null){
                            LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                            moveCameraToLocation(latLng, DEFAULT_ZOOM);

                        }else {
                            Display.infoToast(getContext(),"Unable to find current location");
                        }
                    }else {
                        Display.errorToast(getContext(),"Unable to find current location");
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException " + e.getMessage());
        }
    }

    private void moveCameraToLocation(LatLng latLng, float zoom) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googleMap.animateCamera(cameraUpdate);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Sweet Home")
                .snippet("current location");
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermission = false;

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermission = false;
                    }
                }
                mLocationPermission = true;
                initMap();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        Display.successToast(getContext(), "Map Ready");

        if (mLocationPermission) {
            getDeviceCurrentLocation();
        }
        /*LatLng latLng = new LatLng(27.1751,78.0421);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Taj Mahl")
                .snippet("Situated in India");

        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,16);
        googleMap.animateCamera(cameraUpdate);*/
    }
}