package com.pethome;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>{

    List<RequestModel> requestList;
    boolean isOwnerView;
    OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onApprove(RequestModel request);
        void onReject(RequestModel request);
    }

    public RequestAdapter(List<RequestModel> requestList, boolean isOwnerView, OnRequestActionListener listener){
        this.requestList = requestList;
        this.isOwnerView = isOwnerView;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestModel req = requestList.get(position);

        holder.txtName.setText(req.getName());
        holder.txtBreed.setText(req.getBreed());
        holder.txtStatus.setText(req.getStatus());
        holder.txtRequestUser.setText("From: " + req.getRequestUser());

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(req.getImage()))
                    .placeholder(R.drawable.ic_add)
                    .error(R.drawable.ic_add)
                    .into(holder.imgPet);
        } else {
            holder.imgPet.setImageResource(R.drawable.ic_add);
        }

        if (isOwnerView && "PENDING".equals(req.getStatus())) {
            holder.layoutActions.setVisibility(View.VISIBLE);
            holder.btnApprove.setOnClickListener(v -> {
                if (listener != null) listener.onApprove(req);
            });
            holder.btnReject.setOnClickListener(v -> {
                if (listener != null) listener.onReject(req);
            });
        } else {
            holder.layoutActions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPet;
        TextView txtName, txtBreed, txtStatus, txtRequestUser;
        LinearLayout layoutActions;
        Button btnApprove, btnReject;

        public ViewHolder(View itemView){
            super(itemView);
            imgPet = itemView.findViewById(R.id.imgPet);
            txtName = itemView.findViewById(R.id.txtName);
            txtBreed = itemView.findViewById(R.id.txtBreed);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtRequestUser = itemView.findViewById(R.id.txtRequestUser);
            layoutActions = itemView.findViewById(R.id.layoutActions);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
