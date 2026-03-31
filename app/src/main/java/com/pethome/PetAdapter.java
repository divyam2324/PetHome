package com.pethome;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    List<PetModel> petList;
    OnPetClickListener listener;
    DatabaseHelper dbHelper;
    String userEmail;

    public interface OnPetClickListener {
        void onPetClick(PetModel pet);
    }

    public PetAdapter(List<PetModel> petList, OnPetClickListener listener) {
        this.petList = petList;
        this.listener = listener;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet, parent, false);
        
        dbHelper = new DatabaseHelper(parent.getContext());
        SharedPreferences prefs = parent.getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userEmail = prefs.getString("email", "");
        
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        PetModel pet = petList.get(position);

        holder.txtName.setText(pet.getName());
        holder.txtBreed.setText("Breed: " + pet.getBreed());
        holder.txtAge.setText("Age: " + pet.getAge());
        holder.txtGender.setText("Gender: " + pet.getGender());

        String imageStr = pet.getImage();
        if (imageStr != null && !imageStr.isEmpty()) {
            int resId = holder.itemView.getContext().getResources().getIdentifier(
                    imageStr, "drawable", holder.itemView.getContext().getPackageName());

            if (resId != 0) {
                Glide.with(holder.itemView.getContext())
                        .load(resId)
                        .placeholder(R.drawable.ic_add)
                        .error(R.drawable.ic_add)
                        .into(holder.imgPet);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(Uri.parse(imageStr))
                        .placeholder(R.drawable.ic_add)
                        .error(R.drawable.ic_add)
                        .into(holder.imgPet);
            }
        } else {
            holder.imgPet.setImageResource(R.drawable.ic_add);
        }

        // Handle Favorite Status
        updateFavoriteIcon(holder.imgFavorite, pet.getId());

        holder.imgFavorite.setOnClickListener(v -> {
            if (userEmail == null || userEmail.isEmpty()) {
                Toast.makeText(v.getContext(), "Please login to add favorites", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (dbHelper.isFavorite(userEmail, pet.getId())) {
                dbHelper.removeFavorite(userEmail, pet.getId());
                Toast.makeText(v.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addFavorite(userEmail, pet.getId());
                Toast.makeText(v.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon(holder.imgFavorite, pet.getId());
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onPetClick(pet);
        });
    }

    private void updateFavoriteIcon(ImageView imageView, int petId) {
        if (dbHelper.isFavorite(userEmail, petId)) {
            imageView.setImageResource(R.drawable.ic_heart_f);
        } else {
            imageView.setImageResource(R.drawable.ic_heart_h);
        }
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    static class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPet, imgFavorite;
        TextView txtName, txtBreed, txtAge, txtGender;

        public PetViewHolder(View itemView) {
            super(itemView);
            imgPet = itemView.findViewById(R.id.imgPet);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            txtName = itemView.findViewById(R.id.txtPetName);
            txtBreed = itemView.findViewById(R.id.txtPetBreed);
            txtAge = itemView.findViewById(R.id.txtPetAge);
            txtGender = itemView.findViewById(R.id.txtPetGender);
        }
    }
}
