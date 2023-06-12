package com.mkrlabs.bloodsoilder.ui.request;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.Utils.LayoutType;
import com.mkrlabs.bloodsoilder.Utils.MySharedPref;
import com.mkrlabs.bloodsoilder.Utils.NodeName;
import com.mkrlabs.bloodsoilder.adapter.PostAdapter;
import com.mkrlabs.bloodsoilder.model.BloodRequestItem;
import com.mkrlabs.bloodsoilder.ui.request.donate.DonateFragment;
import com.mkrlabs.bloodsoilder.ui.request.seeker.SeekerFragment;

import java.util.ArrayList;


public class RequestFragment extends Fragment {


    private RecyclerView requestedRV;
    private PostAdapter adapter;
    private ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<BloodRequestItem> postList;
    private MySharedPref sharedPref;
    public RequestFragment() {
        // Required empty public constructor

        // new changes applied
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PostAdapter(LayoutType.REQUESTED_SCREEN);
        init(view);
        getAllPostList();

        adapter.setPostItemClickListener(new PostAdapter.PostItemClickListener() {
            @Override
            public void OnAcceptClick() {

            }

            @Override
            public void OnRemoveClick(String pId) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseFirestore.collection(NodeName.BLOOD_REQUEST_NODE)
                                        .document(pId)
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Display.successToast(getContext(),"Deleted Successfully");
                                                    getAllPostList();
                                                }else {
                                                    Display.errorToast(getContext(),"Something went wrong . Please try again later");
                                                }
                                            }
                                        });
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void OnStatusChanged(String pId,boolean status) {

                firebaseFirestore.collection(NodeName.BLOOD_REQUEST_NODE).document(pId)
                        .update("status",status)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Display.successToast(getContext(),"Status Updated Successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error",e.getLocalizedMessage());
                                Display.errorToast(getContext(),"Something went wrong . Please try again later");
                            }
                        })
                ;

            }

            @Override
            public void OnCallClick(String phone) {

            }
        });

    }



    private void init(View view) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        sharedPref= new MySharedPref(getContext());
        requestedRV = view.findViewById(R.id.requestedRV);
        setUpRecycleView();
        progressBar = view.findViewById(R.id.requestedProgressBar);
    }
    private  void  setUpRecycleView(){
        requestedRV.setLayoutManager(new LinearLayoutManager(getContext()));
        requestedRV.setHasFixedSize(true);
        requestedRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getAllPostList(){
        progressBar.setVisibility(View.VISIBLE);
        postList.clear();
        firebaseFirestore.collection(NodeName.BLOOD_REQUEST_NODE)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    for (DocumentSnapshot snapshot :task.getResult().getDocuments()) {
                        BloodRequestItem requestItem = snapshot.toObject(BloodRequestItem.class);
                        if (requestItem.getRequestBy().equals(sharedPref.getUID())){
                            postList.add(requestItem);
                        }
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error",task.getException().getLocalizedMessage());
                }
                if(postList.isEmpty()){
                    Display.infoToast(getContext(),"No data found !!!");
                }
                adapter.setPostItemArrayList(postList);
            }
        });

    }
}