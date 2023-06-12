package com.mkrlabs.bloodsoilder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.LayoutType;
import com.mkrlabs.bloodsoilder.Utils.Utils;
import com.mkrlabs.bloodsoilder.model.BloodRequestItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter  extends  RecyclerView.Adapter<PostAdapter.PostViewHolder>{


    private PostItemClickListener postItemClickListener;
    private ArrayList<BloodRequestItem> postItemArrayList ;

    LayoutType layoutType;
    public PostAdapter(LayoutType layoutType){
    this.layoutType = layoutType;
    }

    public void setPostItemArrayList(ArrayList<BloodRequestItem> postItemArrayList){
        this.postItemArrayList = postItemArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        BloodRequestItem item = postItemArrayList.get(position);
        bind(holder,item);
    }

    private void bind(PostViewHolder holder , BloodRequestItem item){
        holder.addressSeekerTxt.setText(item.getHospitalAddress());
        holder.hospitalLocationTxt.setText(item.getHospitalName());
        holder.seekerNameTxt.setText(item.getRequestOwnerName());
        holder.phnSeekerTxt.setText(item.getContactNumber());
        if (item.isTransportationExpense())holder.transportIncldTxt.setVisibility(View.VISIBLE);
        holder.monthTxt.setText(Utils.getMonth(item.getTime()));
        holder.donationTimeTxt.setText(Utils.geTime(item.getTime()));
        holder.donationDateTxt.setText(Utils.getDate(item.getTime()));
        holder.bloodGroupBoldTxt.setText(item.getBloodGroup());
        holder.agoTxt.setText(Utils.getTimeAgo(item.getTimestamp().toDate().getTime()));

        Glide.with(holder.itemView)
                .load(item.getRequestOwnerImage())
                .error(R.drawable.bubble)
                .placeholder(R.drawable.bubble)
                .into(holder.profile_image);
        if (layoutType == LayoutType.REQUESTED_SCREEN){
            holder.postRemoveButton.setVisibility(View.VISIBLE);
            holder.postStatus.setVisibility(View.VISIBLE);
            holder.acceptReqBtn.setVisibility(View.GONE);
            holder.postCallButton.setVisibility(View.GONE);
        }else {
            holder.postRemoveButton.setVisibility(View.GONE);
            holder.postStatus.setVisibility(View.GONE);
            holder.acceptReqBtn.setVisibility(View.GONE);
            holder.postCallButton.setVisibility(View.VISIBLE);

        }
        holder.acceptReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItemClickListener.OnAcceptClick();
            }
        });


        holder.postRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItemClickListener.OnRemoveClick(item.getpId());
            }
        });


        holder.postStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                postItemClickListener.OnStatusChanged(item.getpId(),b);

            }
        });
        holder.postCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItemClickListener.OnCallClick(item.getContactNumber()==null?"":item.getContactNumber());
            }
        });



    }
    @Override
    public int getItemCount() {
        return postItemArrayList==null?0:postItemArrayList.size();
    }

    class  PostViewHolder extends RecyclerView.ViewHolder {


        public TextView phnSeekerTxt,addressSeekerTxt,hospitalLocationTxt,seekerNameTxt,transportIncldTxt,monthTxt;
        public TextView donationTimeTxt,donationDateTxt,bloodGroupBoldTxt,agoTxt;
        public Button acceptReqBtn,postRemoveButton;
        public SwitchCompat postStatus;
        public CircleImageView profile_image;
        public ImageButton postCallButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            phnSeekerTxt = itemView.findViewById(R.id.phnSeekerTxt);
            addressSeekerTxt = itemView.findViewById(R.id.addressSeekerTxt);
            hospitalLocationTxt = itemView.findViewById(R.id.hospitalLocationTxt);
            seekerNameTxt = itemView.findViewById(R.id.seekerNameTxt);
            transportIncldTxt = itemView.findViewById(R.id.transportIncldTxt);
            monthTxt = itemView.findViewById(R.id.monthTxt);
            donationTimeTxt = itemView.findViewById(R.id.donationTimeTxt);
            donationDateTxt = itemView.findViewById(R.id.donationDateTxt);
            bloodGroupBoldTxt = itemView.findViewById(R.id.bloodGroupBoldTxt);
            agoTxt = itemView.findViewById(R.id.agoTxt);
            postStatus = itemView.findViewById(R.id.postStatus);
            acceptReqBtn = itemView.findViewById(R.id.acceptReqBtn);
            postRemoveButton = itemView.findViewById(R.id.postRemoveButton);
            profile_image = itemView.findViewById(R.id.profile_image);
            postCallButton = itemView.findViewById(R.id.postCallButton);

        }
    }

    public void setPostItemClickListener(PostItemClickListener postItemClickListener) {
        this.postItemClickListener = postItemClickListener;
    }


    public interface  PostItemClickListener{
        void OnAcceptClick();
        void OnRemoveClick(String pId);
        void OnStatusChanged(String pId,boolean status);
        void OnCallClick(String phone);
    }
}
