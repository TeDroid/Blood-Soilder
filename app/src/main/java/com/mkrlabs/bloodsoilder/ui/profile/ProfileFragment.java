package com.mkrlabs.bloodsoilder.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.Utils.MySharedPref;
import com.mkrlabs.bloodsoilder.Utils.NodeName;
import com.mkrlabs.bloodsoilder.model.User;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {



    private TextView profileUserName,profileUserPhone,profileUserEmail,profileUserBloodGroup;
    private CircleImageView profileCircleImageView;
    private ProgressBar profileProgressBar;
    private MySharedPref myShreadPref;
    private ImageButton profileEditIcon;
    private TextInputEditText updateNameEdt,updatePhoneEdt;
    private TextInputEditText updateEmailEdt;
    private MaterialButton profileUpdateButton,profileCancelButton;
    private ProgressBar profileUpdateProgressBar;
    private BottomSheetDialog bottomSheetDialog ;
    private User user;
    private FirebaseFirestore firebaseFirestore;
    private SwitchCompat profileUserDonorStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        myShreadPref = new MySharedPref(view.getContext());
        initUser();
        profileEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileEditBottomSheet();
            }
        });

        profileUserDonorStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                    firebaseFirestore.collection(NodeName.USER_NODE).document(myShreadPref.getUID())
                            .update("donation_status",b)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Display.successToast(getContext(),"Status Updated Successfully");
                                    }else {
                                        task.getException().printStackTrace();
                                        Display.successToast(getContext(),"Something went wrong");
                                    }
                                }
                            });


            }
        });

    }

    private void initUser() {
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection(NodeName.USER_NODE).document(myShreadPref.getUID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            user = task.getResult().toObject(User.class);

                            profileUserName.setText(user.getName());
                            profileUserPhone.setText(user.getPhone().isEmpty()?"01X XX XX XX XX":user.getPhone());
                            profileUserEmail.setText(user.getEmail());
                            profileUserBloodGroup.setText(user.getBlood_group());
                            Glide.with(getContext())
                                    .load(user.getProfileImage())
                                    .placeholder(R.drawable.bubble)
                                    .error(R.drawable.bubble)
                                    .into(profileCircleImageView);
                            profileUserDonorStatus.setChecked(user.isDonation_status());

                        }else {

                            Log.v("Error",task.getException().getLocalizedMessage());
                        }
                    }
                });

    }

    private void showProfileEditBottomSheet() {


        bottomSheetDialog = new BottomSheetDialog(getContext());


        View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_layout,null);

        bottomSheetDialog.setContentView(view);

        updateNameEdt = view.findViewById(R.id.userNameUpdateEDT);
        updatePhoneEdt = view.findViewById(R.id.userPhoneUpdateEDT);
        updateEmailEdt = view.findViewById(R.id.userEmailUpdateEDT);
        profileUpdateButton = view.findViewById(R.id.profileUpdateButton);
        profileCancelButton = view.findViewById(R.id.profileUpdateCancelButton);
        profileUpdateProgressBar = view.findViewById(R.id.profileUpdateProgressBar);

        if (user!=null) {
            updateNameEdt.setText(user.getName());
            updateEmailEdt.setText(user.getEmail());
            updatePhoneEdt.setText(user.getPhone().isEmpty()?"":user.getPhone());
        }


        profileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = updateNameEdt.getText().toString().trim();
                String email = updateEmailEdt.getText().toString().trim();
                String phone = updatePhoneEdt.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    updateNameEdt.setError("required");
                    return;
                }

                if (TextUtils.isEmpty(phone)){
                    updatePhoneEdt.setError("required");
                    return;
                }
                Map<String,Object>map = new HashMap<>();
                map.put("name",name);
                map.put("phone",phone);


                firebaseFirestore.collection(NodeName.USER_NODE).document(myShreadPref.getUID())
                        .update(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    if (bottomSheetDialog!=null) bottomSheetDialog.dismiss();
                                    Display.successToast(getContext(),"Profile updated successfully");
                                }else {
                                    Log.v("Error",task.getException().getLocalizedMessage());
                                    Display.errorToast(getContext(),"Something went wrong");
                                }
                            }
                        });





            }
        });


        profileCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetDialog!=null) bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();

    }
    private void initView(View view){
        profileUserName = view.findViewById(R.id.profileUserName);
        profileUserPhone = view.findViewById(R.id.profileUserPhone);
        profileUserEmail = view.findViewById(R.id.profileUserEmail);
        profileUserBloodGroup = view.findViewById(R.id.profileUserBloodGroup);
        profileUserDonorStatus = view.findViewById(R.id.profileUserDonorStatus);
        profileEditIcon = view.findViewById(R.id.profileEditIcon);
        profileProgressBar = view.findViewById(R.id.profileProgressBar);
        profileCircleImageView = view.findViewById(R.id.profileCircleImageView);
    }
}