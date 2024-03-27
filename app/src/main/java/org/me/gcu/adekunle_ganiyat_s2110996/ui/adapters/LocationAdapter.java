package org.me.gcu.adekunle_ganiyat_s2110996.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.adekunle_ganiyat_s2110996.R;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private List<String> locationList;
    private final OnLocationClickListener onLocationClickListener;

    public interface OnLocationClickListener {
        void onLocationClick(String locationId);
    }

    public LocationAdapter(List<String> locationList, OnLocationClickListener onLocationClickListener) {
        this.locationList = locationList;
        this.onLocationClickListener = onLocationClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String location = locationList.get(position);
        holder.locationTextView.setText(location);
        holder.itemView.setOnClickListener(v -> onLocationClickListener.onLocationClick(location));
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void filterList(List<String> filteredList) {
        locationList = filteredList;
        notifyDataSetChanged();
    }

    public interface OnLocationSelectedListener {
        void onLocationSelected(String location);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView;

        ViewHolder(View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.location_text_view);
        }
    }
}