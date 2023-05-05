package com.mkrlabs.bloodsoilder.ui.home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;

import java.util.Calendar;


public class HomeFragment extends Fragment {

    private ImageButton homeBloodSearch;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private TextView findDonarDateTV,selectTimeTV;
    RadioGroup mFirstGroup;
    RadioGroup mSecondGroup;
    boolean isChecking = true;
    int mCheckedId =-1;
    private String BLOOD_GROUP = null;
    private String selectedDate = null;
    private String selectedTime = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        homeBloodSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchDialog();
            }
        });

    }
    private void init(View view) {
        homeBloodSearch = view.findViewById(R.id.homeBloodSearch);


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
        Button findDonorBtn = view.findViewById(R.id.findDonorBtn);
        EditText bloodSearchHospitalNameEdt = view.findViewById(R.id.bloodSearchHospitalNameEdt);
        EditText searchBloodLocationEdt = view.findViewById(R.id.searchBloodLocationEdt);

        mFirstGroup = (RadioGroup) view.findViewById(R.id.first_group);
        mSecondGroup = (RadioGroup) view.findViewById(R.id.second_group);



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



        mFirstGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    mSecondGroup.clearCheck();
                    mCheckedId = checkedId;
                    userBloodGroupSelection();
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
                    userBloodGroupSelection();
                }
                isChecking = true;
            }
        });





        findDonorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = searchBloodLocationEdt.getText().toString();
                String hospitalName = bloodSearchHospitalNameEdt.getText().toString();

                if (BLOOD_GROUP ==null){
                    Display.errorToast(view.getContext(),"select blood group");
                    return;
                }
                 if (selectedDate ==null){
                    Display.errorToast(view.getContext(),"select date");
                    return;
                }
                 if (selectedTime ==null){
                    Display.errorToast(view.getContext(),"select time");
                    return;
                }
                 if (BLOOD_GROUP ==null){
                    Display.errorToast(view.getContext(),"select blood group");
                    return;
                }


                 if (hospitalName.isEmpty()){
                     Display.errorToast(view.getContext(),"write hospital name");
                     return;
                 }
                 if (location.isEmpty()){
                     Display.errorToast(view.getContext(),"write location");
                     return;
                 }

                 Display.successToast(view.getContext(),"All Okay");

            }
        });






        dialog.show();
    }
    public void userBloodGroupSelection() {
        if (mCheckedId == R.id.type1) {
            BLOOD_GROUP = "A+";
        } else if (mCheckedId == R.id.type2) {
            BLOOD_GROUP = "A-";
        } else if (mCheckedId == R.id.type3) {
            BLOOD_GROUP = "B+";
        } else if (mCheckedId == R.id.type4) {
            BLOOD_GROUP = "B-";
        } else if (mCheckedId == R.id.type5) {
            BLOOD_GROUP = "AB+";
        } else if (mCheckedId == R.id.type6) {
            BLOOD_GROUP = "AB-";
        } else if (mCheckedId == R.id.type7) {
            BLOOD_GROUP = "O+";
        } else if (mCheckedId == R.id.type8) {
            BLOOD_GROUP = "O-";
        }else {
            BLOOD_GROUP =null;
        }
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

                        selectedTime = hourOfDay + ":" + minute;
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

                selectedDate=dayOfMonth + "/" + (month+1) + "/" + year;
                findDonarDateTV.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}