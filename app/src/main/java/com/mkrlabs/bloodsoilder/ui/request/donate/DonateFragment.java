package com.mkrlabs.bloodsoilder.ui.request.donate;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.Utils.MySharedPref;
import com.mkrlabs.bloodsoilder.Utils.NodeName;
import com.mkrlabs.bloodsoilder.adapter.DonorAdapter;
import com.mkrlabs.bloodsoilder.model.User;

import java.util.ArrayList;
import java.util.List;


public class DonateFragment extends Fragment {


    private RecyclerView donorRV;
    private DonorAdapter adapter;

    private List<User> donorList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private MySharedPref sharedPref;
    public DonateFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPref = new MySharedPref(getContext());

        getDonorList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpRecycleView();

        adapter.setOnDonorCallClickListener(new DonorAdapter.OnDonorCallListener() {
            @Override
            public void onClick(User user) {
                String phone = user.getPhone();
                if (phone.isEmpty() || phone ==null){
                    Display.infoToast(getContext(),"User doesn't update his phone number");
                }else {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);

                }
            }
        });
    }
    private void initView(View view){
        donorRV = view.findViewById(R.id.donorRV);
    }
    private void setUpRecycleView(){
        donorRV.setLayoutManager(new LinearLayoutManager(getContext()));
        donorRV.setHasFixedSize(true);
        adapter = new DonorAdapter(donorList);
        donorRV.setAdapter(adapter);

    }

    private void getDonorList(){
        firebaseFirestore.collection(NodeName.USER_NODE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot s :task.getResult().getDocuments()) {
                        User user = s.toObject(User.class);
                        if (user.isDonation_status() && user.isStatus() && !user.getUid().equals(sharedPref.getUID())){
                            donorList.add(user);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    task.getException().printStackTrace();
                    Log.v("Error",task.getException().getMessage());
                    Display.errorToast(getContext(),"Something went wrong");
                }
            }
        });

    }
}