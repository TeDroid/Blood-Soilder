package com.mkrlabs.bloodsoilder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorAdapter extends  RecyclerView.Adapter<DonorAdapter.DonorViewHolder>{
    private OnDonorCallListener onDonorCallClickListener;
    private List<User> donorList;
    public DonorAdapter(List<User> donorList){
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        User user = donorList.get(position);

        holder.userItemName.setText(user.getName());
        holder.userItemBloodGroup.setText(user.getBlood_group());
        Glide.with(holder.itemView)
                .load(user.getProfileImage())
                .error(R.drawable.bubble)
                .placeholder(R.drawable.bubble)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.userItemImage);

        holder.userItemPhnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDonorCallClickListener.onClick(user);
            }
        });


    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public void setOnDonorCallClickListener(OnDonorCallListener onDonorCallClickListener){
        this.onDonorCallClickListener = onDonorCallClickListener;
    }
    public interface OnDonorCallListener{
        void onClick(User user);
    }

    class DonorViewHolder extends RecyclerView.ViewHolder{
        public TextView userItemName,userItemBloodGroup;
        public ImageView userItemPhnDial;
        public CircleImageView userItemImage;
        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);

            userItemName = itemView.findViewById(R.id.userItemNameTV);
            userItemBloodGroup = itemView.findViewById(R.id.userItemBloodGroup);
            userItemImage = itemView.findViewById(R.id.userItemImage);
            userItemPhnDial = itemView.findViewById(R.id.userItemPhnDial);
        }
    }
}
