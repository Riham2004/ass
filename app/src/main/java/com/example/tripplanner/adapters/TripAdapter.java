package com.example.tripplanner.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.R;
import com.example.tripplanner.models.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> tripList;
    private OnTripClickListener listener;

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
        void onTripLongClick(Trip trip);
    }

    public TripAdapter(List<Trip> tripList, OnTripClickListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);

        holder.tvName.setText(trip.getName());
        holder.tvDestination.setText(trip.getDestination());
        holder.tvDate.setText(trip.getStartDate() + " - " + trip.getEndDate());
        holder.tvTripType.setText(trip.getTripType());
        holder.tvBudget.setText("$" + String.format("%.0f", trip.getBudget()));
        holder.tvPriority.setText(trip.getPriority());

        // ✅ تغيير لون بسيط
        int color = getPriorityColor(trip.getPriority());
        holder.tvPriority.setBackgroundColor(color);

        // صورة
        if (trip.getImageResId() != 0) {
            holder.imgTrip.setImageResource(trip.getImageResId());
        } else {
            holder.imgTrip.setImageResource(R.drawable.ic_trip_default);
        }

        // Clicks
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTripClick(trip);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onTripLongClick(trip);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void updateList(List<Trip> newList) {
        this.tripList = newList;
        notifyDataSetChanged();
    }

    // Helper methods
    private int getPriorityColor(String priority) {
        switch (priority.toLowerCase()) {
            case "high":
                return Color.parseColor("#E53935"); // أحمر
            case "medium":
                return Color.parseColor("#F9A825"); // أصفر
            case "low":
                return Color.parseColor("#4CAF50"); // أخضر
            default:
                return Color.parseColor("#9E9E9E"); // رمادي
        }
    }

    private String getTripTypeEmoji(String tripType) {
        switch (tripType.toLowerCase()) {
            case "adventure":
                return "🏔️";
            case "leisure":
                return "🏖️";
            case "business":
                return "💼";
            default:
                return "✈️";
        }
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDestination, tvDate, tvPriority, tvTripType, tvBudget;
        ImageView imgTrip;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTripName);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            tvTripType = itemView.findViewById(R.id.tvTripType);
            tvBudget = itemView.findViewById(R.id.tvBudget);
            imgTrip = itemView.findViewById(R.id.imgTrip);
        }
    }
}