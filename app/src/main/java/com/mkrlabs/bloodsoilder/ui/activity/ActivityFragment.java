package com.mkrlabs.bloodsoilder.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.ui.request.donate.DonateFragment;
import com.mkrlabs.bloodsoilder.ui.request.seeker.SeekerFragment;


public class ActivityFragment extends Fragment {

    private FrameLayout activityFrameContainer;
    private AppCompatButton activityCampaignButton, activityHistoryButton;

    public ActivityFragment() {
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
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setCurrentButtonStyle(true,  activityCampaignButton, activityHistoryButton,true);
        setUpWalletFragment(new DonateFragment());
        activityCampaignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentButtonStyle(true, activityCampaignButton, activityHistoryButton,true);
                setUpWalletFragment(new DonateFragment());
            }
        });
        activityHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentButtonStyle(true, activityHistoryButton, activityCampaignButton,false);
                setUpWalletFragment(new SeekerFragment());
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

    private void setUpWalletFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.activityFrameContainer, fragment)
                .commit();
    }

    private void init(View view) {
        activityFrameContainer = view.findViewById(R.id.requestFrameContainer);
        activityCampaignButton = view.findViewById(R.id.campaignTabButton);
        activityHistoryButton = view.findViewById(R.id.historyTabButton);
    }
}