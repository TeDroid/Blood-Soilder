package com.mkrlabs.bloodsoilder.ui.new_home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.ui.account.LoginActivity;
import com.mkrlabs.bloodsoilder.ui.home.HomeFragment;
import com.mkrlabs.bloodsoilder.ui.request.donate.DonateFragment;
import com.mkrlabs.bloodsoilder.ui.request.seeker.SeekerFragment;


public class PreviewFragment extends Fragment {
    private FrameLayout requestFrameContainer;
    private AppCompatButton requestDonateButton, requestSeekerButton;

    private ImageButton homeLogout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setCurrentButtonStyle(true,  requestDonateButton, requestSeekerButton,true);
        setUpFragment(new HomeFragment());
        requestDonateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentButtonStyle(true, requestDonateButton, requestSeekerButton,true);
                setUpFragment(new HomeFragment());
            }
        });
        requestSeekerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentButtonStyle(true, requestSeekerButton, requestDonateButton,false);
                setUpFragment(new DonateFragment());
            }
        });
        homeLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Logout ")
                        .setMessage("Are you sure you want to logout ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });



    }

    private void setCurrentButtonStyle(boolean isSelected, AppCompatButton selectedButton, AppCompatButton nonSelectedButton,boolean isDonate) {
        if (isSelected) {
            selectedButton.setTextColor(getContext().getResources().getColor(R.color.white));

            if (isDonate){
                selectedButton.setBackgroundDrawable(getContext().getDrawable(R.drawable.selected_donate_button_style));
            }else {
                selectedButton.setBackgroundDrawable(getContext().getDrawable(R.drawable.selected_seek_button_style));
            }

            // opposite button
            nonSelectedButton.setTextColor(getContext().getResources().getColor(R.color.primaryColor));
            nonSelectedButton.setBackgroundDrawable(getContext().getDrawable(R.drawable.non_selected_button_style));
        }
    }

    private void setUpFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.requestFrameContainer, fragment)
                .commit();
    }

    private void init(View view) {
        homeLogout = view.findViewById(R.id.homeLogout);
        requestFrameContainer = view.findViewById(R.id.requestFrameContainer);
        requestDonateButton = view.findViewById(R.id.walletMyCoinButton);
        requestSeekerButton = view.findViewById(R.id.walletCashOutButton);
    }
}