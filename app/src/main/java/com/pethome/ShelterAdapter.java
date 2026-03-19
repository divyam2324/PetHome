package com.pethome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShelterAdapter extends RecyclerView.Adapter<ShelterAdapter.ViewHolder> {

    private List<ShelterModel> shelterList;

    public ShelterAdapter(List<ShelterModel> shelterList) {
        this.shelterList = shelterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shelter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShelterModel shelter = shelterList.get(position);
        holder.txtName.setText(shelter.getName());
        holder.txtCity.setText("City: " + shelter.getCity());
        holder.txtContact.setText("Contact: " + shelter.getContact());
        
        if (shelter.getMedicalInfo() != null && !shelter.getMedicalInfo().isEmpty()) {
            holder.txtMedicalInfo.setText(shelter.getMedicalInfo());
        } else {
            holder.txtMedicalInfo.setText("No medical information provided.");
        }
    }

    @Override
    public int getItemCount() {
        return shelterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCity, txtContact, txtMedicalInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtShelterName);
            txtCity = itemView.findViewById(R.id.txtShelterCity);
            txtContact = itemView.findViewById(R.id.txtShelterContact);
            txtMedicalInfo = itemView.findViewById(R.id.txtMedicalInfo);
        }
    }
}
