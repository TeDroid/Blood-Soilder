package com.mkrlabs.bloodsoilder.ui.homemap;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;
import com.mkrlabs.bloodsoilder.ui.account.CreateAccountActivity;

import java.security.Permission;
import java.util.Calendar;


public class HomeMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Map";
    private String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermission;
    private GoogleMap googleMap;
    private int REQUEST_CODE = 1001;
    private float DEFAULT_ZOOM = 16f;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private ImageButton homeMapBloodSearch;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private TextView findDonarDateTV,selectTimeTV;

    // blood radio button
    RadioGroup mFirstGroup;
    RadioGroup mSecondGroup;
    boolean isChecking = true;
    int mCheckedId = R.id.type1;

    public HomeMapFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_map, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        homeMapBloodSearch = view.findViewById(R.id.homeMapBloodSearch);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocationPermission();


        homeMapBloodSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchDialog();
            }
        });




    }

    private void openSearchDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_find_donor_search_top, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        findDonarDateTV = view.findViewById(R.id.findDonarDateTV);
        selectTimeTV = view.findViewById(R.id.selectTimeTV);

        MaterialCardView selectDateCV = view.findViewById(R.id.pickDateCV);
        MaterialCardView selectTimeCV = view.findViewById(R.id.pickTimeCV);
        MaterialCardView selectLocationCV = view.findViewById(R.id.setLocationCV);


        mFirstGroup = view.findViewById(R.id.first_group);
        mSecondGroup = view.findViewById(R.id.second_group);

        selectDateCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDateWindow();
            }
        });
        selectTimeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTimeWindow();
            }
        });

        dialog.show();

        mFirstGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    mSecondGroup.clearCheck();
                    mCheckedId = checkedId;
                }
                isChecking = true;
            }
        });

        mSecondGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    mFirstGroup.clearCheck();
                    mCheckedId = checkedId;
                }
                isChecking = true;
            }
        });
    }

    private void setUpTimeWindow() {
        Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        selectTimeTV.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    private void setUpDateWindow() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                findDonarDateTV.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
            Task<Location> task = fusedLocationProviderClient.getLastLocation();

            task.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location userLocation = task.getResult();

                        if (userLocation != null) {
                            LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                            moveCameraToLocation(latLng, DEFAULT_ZOOM);

                        } else {
                            Display.infoToast(getContext(), "Unable to find current location");
                        }
                    } else {
                        Display.errorToast(getContext(), "Unable to find current location");
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