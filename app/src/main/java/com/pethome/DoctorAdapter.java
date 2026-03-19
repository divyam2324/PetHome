package com.pethome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<DoctorModel> doctorList;
    private boolean isOwnerView;
    private OnDoctorActionListener listener;

    public interface OnDoctorActionListener {
        void onDelete(DoctorModel doctor);
    }

    public DoctorAdapter(List<DoctorModel> doctorList, boolean isOwnerView, OnDoctorActionListener listener) {
        this.doctorList = doctorList;
        this.isOwnerView = isOwnerView;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorModel doctor = doctorList.get(position);
        holder.txtName.setText(doctor.getName());
        holder.txtSpec.setText(doctor.getSpecialization());
        holder.txtExp.setText(doctor.getExperience() + " Experience");
        holder.txtContact.setText("Contact: " + doctor.getContact());

        if (doctor.getShelterName() != null && !doctor.getShelterName().isEmpty()) {
            holder.txtShelterName.setVisibility(View.VISIBLE);
            holder.txtShelterName.setText("Shelter: " + doctor.getShelterName());
        } else {
            holder.txtShelterName.setVisibility(View.GONE);
        }

        if (isOwnerView) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(doctor);
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtSpec, txtExp, txtContact, txtShelterName;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtDoctorName);
            txtSpec = itemView.findViewById(R.id.txtSpecialization);
            txtExp = itemView.findViewById(R.id.txtExperience);
            txtContact = itemView.findViewById(R.id.txtContact);
            txtShelterName = itemView.findViewById(R.id.txtShelterName);
            btnDelete = itemView.findViewById(R.id.btnDeleteDoctor);
        }
    }
}
