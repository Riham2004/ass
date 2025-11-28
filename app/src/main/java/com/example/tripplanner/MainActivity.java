package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.adapters.TripAdapter;
import com.example.tripplanner.data.TripManager;
import com.example.tripplanner.models.Trip;
import com.example.tripplanner.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private TripManager tripManager;
    private List<Trip> tripList;
    private SearchView searchView;
    private FloatingActionButton fabAdd;
    private RadioGroup rgFilter;
    private TextView tvTripCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupListeners();
        updateTripCount();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerTrips);
        searchView = findViewById(R.id.searchView);
        fabAdd = findViewById(R.id.fabAdd);
        rgFilter = findViewById(R.id.rgFilter);
        tvTripCount = findViewById(R.id.tvTripCount);

        tripManager = new TripManager(this);
        tripList = tripManager.loadTrips();
    }

    private void setupRecyclerView() {
        adapter = new TripAdapter(tripList, new TripAdapter.OnTripClickListener() {
            @Override
            public void onTripClick(Trip trip) {
                Intent intent = new Intent(MainActivity.this, TripDetailsActivity.class);
                intent.putExtra(Constants.EXTRA_TRIP_ID, trip.getId());
                startActivity(intent);
            }

            @Override
            public void onTripLongClick(Trip trip) {
                showDeleteDialog(trip);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterTrips(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTrips(newText);
                return true;
            }
        });

        rgFilter.setOnCheckedChangeListener((group, checkedId) -> {
            String filter = getFilterType(checkedId);
            adapter.updateList(tripManager.filterByType(filter));
            updateTripCount();
        });
    }

    private void filterTrips(String query) {
        if (query == null || query.isEmpty()) {
            adapter.updateList(tripManager.loadTrips());
        } else {
            adapter.updateList(tripManager.searchTrips(query));
        }
    }

    private String getFilterType(int checkedId) {
        if (checkedId == R.id.rbAdventure) return Constants.TRIP_TYPE_ADVENTURE;
        else if (checkedId == R.id.rbLeisure) return Constants.TRIP_TYPE_LEISURE;
        else if (checkedId == R.id.rbBusiness) return Constants.TRIP_TYPE_BUSINESS;
        else return Constants.TRIP_TYPE_ALL;
    }

    private void showDeleteDialog(Trip trip) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Trip")
                .setMessage("Delete '" + trip.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    tripManager.deleteTrip(trip.getId());
                    tripList.remove(trip);
                    adapter.notifyDataSetChanged();
                    updateTripCount();
                    Toast.makeText(this, Constants.MSG_TRIP_DELETED, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateTripCount() {
        int count = tripManager.getTripsCount();
        String text = count + (count == 1 ? " trip planned" : " trips planned");
        tvTripCount.setText(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tripList.clear();
        tripList.addAll(tripManager.loadTrips());
        adapter.notifyDataSetChanged();
        updateTripCount();
    }
}
