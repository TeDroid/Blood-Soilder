package com.mkrlabs.bloodsoilder.ui.home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.Utils.DisplayUtils;
import com.mkrlabs.bloodsoilder.Utils.LayoutType;
import com.mkrlabs.bloodsoilder.Utils.MySharedPref;
import com.mkrlabs.bloodsoilder.Utils.NodeName;
import com.mkrlabs.bloodsoilder.Utils.Utils;
import com.mkrlabs.bloodsoilder.adapter.PostAdapter;
import com.mkrlabs.bloodsoilder.api.ApiClient;
import com.mkrlabs.bloodsoilder.api.ApiServices;
import com.mkrlabs.bloodsoilder.model.BloodRequestItem;
import com.mkrlabs.bloodsoilder.model.User;
import com.mkrlabs.bloodsoilder.notification.NotificationItem;
import com.mkrlabs.bloodsoilder.notification.NotificationResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    private FirebaseFirestore firebaseFirestore;

    private RecyclerView homeRV;
    private PostAdapter adapter;
    private ArrayList<BloodRequestItem> postList;
    private ProgressBar progressBarHome;
    private MySharedPref sharedPref;
    private User user;
    private ApiServices apiServices ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();

        getUserInfo();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPref = new MySharedPref(getContext());
        apiServices = ApiClient.getInstance().create(ApiServices.class);
        init(view);
        getAllPostList();

        homeBloodSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchDialog();
            }
        });


        adapter.setPostItemClickListener(new PostAdapter.PostItemClickListener() {
            @Override
            public void OnAcceptClick() {
                Display.successToast(getContext(),"Accept Clicked");

            }

            @Override
            public void OnRemoveClick(String pId) {

            }

            @Override
            public void OnStatusChanged(String pId,boolean status) {

            }

            @Override
            public void OnCallClick(String phone) {
                if (phone.isEmpty() || phone ==null){
                    Display.infoToast(getContext(),"User doesn't update his phone number");
                }else {
                    String user_phone = "+34666777888";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", user_phone, null));
                    startActivity(intent);

                }
            }
        });

    }
    private void getUserInfo(){
        firebaseFirestore.collection(NodeName.USER_NODE).document(FirebaseAuth.getInstance().getUid().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            user = task.getResult().toObject(User.class);


                        }else {

                            Log.v("Error",task.getException().getLocalizedMessage());
                        }
                    }
                });    }
    private void init(View view) {
        postList = new ArrayList<>();
        homeBloodSearch = view.findViewById(R.id.homeBloodSearch);
        homeRV = view.findViewById(R.id.homeRV);
        progressBarHome = view.findViewById(R.id.progressBarHome);
        setUpRecycleView();

    }


    private void  setUpRecycleView(){
        adapter = new PostAdapter(LayoutType.HOME_SCREEN);
        homeRV.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRV.setHasFixedSize(true);
        homeRV.setAdapter(adapter);

    }

    private void getAllPostList(){
        progressBarHome.setVisibility(View.VISIBLE);
        postList.clear();
        firebaseFirestore.collection(NodeName.BLOOD_REQUEST_NODE)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    progressBarHome.setVisibility(View.GONE);
                    for (DocumentSnapshot snapshot :task.getResult().getDocuments()) {
                        BloodRequestItem requestItem = snapshot.toObject(BloodRequestItem.class);
                        if (requestItem.isStatus()){
                            postList.add(requestItem);
                        }
                    }
                }else {
                    progressBarHome.setVisibility(View.GONE);
                    Log.e("Error",task.getException().getLocalizedMessage());
                }
                adapter.setPostItemArrayList(postList);
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
        Button findDonorBtn = view.findViewById(R.id.findDonorBtn);
        EditText bloodSearchHospitalNameEdt = view.findViewById(R.id.bloodSearchHospitalNameEdt);
        EditText searchBloodLocationEdt = view.findViewById(R.id.searchBloodLocationEdt);
        CheckBox expensesCheckBox = view.findViewById(R.id.expensesCheckBox);
        ProgressBar bloodRequestProgressBar = view.findViewById(R.id.bloodRequestProgressBar);

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

                bloodRequestProgressBar.setVisibility(View.VISIBLE);
                 requestForBlood(dialog,bloodRequestProgressBar,BLOOD_GROUP,selectedDate,selectedTime,hospitalName,location,expensesCheckBox.isChecked());
            }
        });
        dialog.show();
    }

    private void requestForBlood(BottomSheetDialog dialog,ProgressBar bloodRequestProgressBar , String blood_group, String selectedDate, String selectedTime, String hospitalName, String location, boolean checked) {
        BloodRequestItem bloodRequestItem = new BloodRequestItem();
        //    public BloodRequestItem(String hospitalName, String hospitalAddress, Timestamp timestamp, String bloodGroup, boolean status, String requestBy,
        //    boolean transportationExpense, String contactNumber, String requestOwnerName, String requestOwnerImage) {

        Timestamp timestamp = new Timestamp(new Date());
        bloodRequestItem.setHospitalName(hospitalName);
        bloodRequestItem.setHospitalAddress(location);
        bloodRequestItem.setTimestamp(timestamp);
        bloodRequestItem.setBloodGroup(BLOOD_GROUP);
        bloodRequestItem.setStatus(true);
        bloodRequestItem.setRequestBy(sharedPref.getUID());
        bloodRequestItem.setRequestOwnerName(sharedPref.getUSER_NAME());
        bloodRequestItem.setRequestOwnerImage(sharedPref.getUSER_IMAGE());
        bloodRequestItem.setContactNumber(user!=null?user.getPhone():"");

        String pid = firebaseFirestore.collection(NodeName.BLOOD_REQUEST_NODE).document().getId();
        bloodRequestItem.setpId(pid);

        Log.v("RequestItem","Time "+mHour +" Minute " + mMinute + " Day "+ mDay + " Month "+ mMonth);

        LocalDateTime selectedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            selectedDateTime = LocalDateTime.of(mYear, mMonth+1, mDay, mHour, mMinute);
        }

        long unixTimestamp = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            unixTimestamp = selectedDateTime.toEpochSecond(ZoneOffset.UTC);
        }

        Timestamp time = new Timestamp(unixTimestamp, 0);
        bloodRequestItem.setTime(time);

        firebaseFirestore.collection(NodeName.BLOOD_REQUEST_NODE).document(pid).set(bloodRequestItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Display.successToast(getContext(),"Blood Requested Successfully");
                    triggerNotification();
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                }
            }
        });
        bloodRequestProgressBar.setVisibility(View.GONE);
        getAllPostList();





    }

    private void triggerNotification() {


        String to = Utils.BLOOD_REQUEST_TOPIC;
        String header = Utils.headerToken;
        String title = "Blood Request ";
        String description = "Urgent need "+BLOOD_GROUP +" blood group";
        NotificationItem notificationItem = new NotificationItem();

        NotificationItem.NotificationBody notificationBody = new NotificationItem.NotificationBody();
        notificationBody.title = title;
        notificationBody.body = description;

        notificationItem.notification = notificationBody;
        notificationItem.to=to;
        apiServices.postNotification(notificationItem,header).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.raw().code() == 200){
                    Display.infoToast(getContext(),"Notification Triggered");
                }else {
                    Display.infoToast(getContext(),"Something went wrong "+ response.raw().message());
                }

            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {

            }
        });
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