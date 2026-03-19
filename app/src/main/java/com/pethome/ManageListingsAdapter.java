package com.pethome;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ManageListingsAdapter extends RecyclerView.Adapter<ManageListingsAdapter.ViewHolder> {

    private List<PetModel> petList;
    private OnPetActionListener listener;

    public interface OnPetActionListener {
        void onDelete(PetModel pet);
        void onEdit(PetModel pet);
    }

    public ManageListingsAdapter(List<PetModel> petList, OnPetActionListener listener) {
        this.petList = petList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_pet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PetModel pet = petList.get(position);
        holder.txtName.setText(pet.getName());
        holder.txtBreed.setText("Breed: " + pet.getBreed());

        String imageStr = pet.getImage();
        if (imageStr != null && !imageStr.isEmpty()) {
            int resId = holder.itemView.getContext().getResources().getIdentifier(
                    imageStr, "drawable", holder.itemView.getContext().getPackageName());

            if (resId != 0) {
                Glide.with(holder.itemView.getContext())
                        .load(resId)
                        .placeholder(R.drawable.ic_pets)
                        .into(holder.imgPet);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(Uri.parse(imageStr))
                        .placeholder(R.drawable.ic_pets)
                        .into(holder.imgPet);
            }
        }

        holder.btnDelete.setOnClickListener(v -> listener.onDelete(pet));
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(pet));
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPet;
        TextView txtName, txtBreed, btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPet = itemView.findViewById(R.id.imgPet);
            txtName = itemView.findViewById(R.id.txtPetName);
            txtBreed = itemView.findViewById(R.id.txtPetBreed);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
