package com.example.tripplanner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripplanner.utils.Constants;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.data.TripManager;
import com.example.tripplanner.models.PackingItem;
import com.example.tripplanner.models.Trip;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView tvTripName, tvSource, tvDestination, tvDates, tvTripType, tvBudget, tvStatus, tvNotes;
    private EditText etPackingItem;
    private Button btnAddItem, btnEdit, btnDelete, btnEditNotes;
    private LinearLayout packingListContainer;

    private Trip currentTrip;
    private TripManager tripManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        // Initialize views
        tvTripName = findViewById(R.id.tvTripName);
        tvSource = findViewById(R.id.tvSource);
        tvDestination = findViewById(R.id.tvDestination);
        tvDates = findViewById(R.id.tvDates);
        tvTripType = findViewById(R.id.tvTripType);
        tvBudget = findViewById(R.id.tvBudget);
        tvStatus = findViewById(R.id.tvStatus);
        tvNotes = findViewById(R.id.tvNotes);

        etPackingItem = findViewById(R.id.etPackingItem);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnEditNotes = findViewById(R.id.btnEditNotes);

        packingListContainer = findViewById(R.id.packingListContainer);

        tripManager = new TripManager(this);

        Intent intent = getIntent();
        String tripId = intent.getStringExtra(Constants.EXTRA_TRIP_ID);

        if (tripId != null) {
            currentTrip = tripManager.getTripById(tripId);
            if (currentTrip != null) {
                displayTripDetails();
                displayPackingList();
            }
        }

        btnAddItem.setOnClickListener(v -> addPackingItem());

        btnEdit.setOnClickListener(v -> editTrip());

        btnDelete.setOnClickListener(v -> confirmDelete());

        btnEditNotes.setOnClickListener(v -> editNotes());
    }

    private void displayTripDetails() {
        tvTripName.setText(currentTrip.getName());
        tvSource.setText(currentTrip.getSource());
        tvDestination.setText(currentTrip.getDestination());
        tvDates.setText(currentTrip.getStartDate() + " → " + currentTrip.getEndDate());
        tvTripType.setText(currentTrip.getTripType());
        tvBudget.setText("$" + currentTrip.getBudget());
        tvStatus.setText(currentTrip.isCompleted() ? "Confirmed ✓" : "Not Confirmed");

        String notes = currentTrip.getNotes();
        tvNotes.setText(notes != null && !notes.isEmpty() ? notes : Constants.MSG_NO_NOTES);
    }

    private void displayPackingList() {
        packingListContainer.removeAllViews();

        if (currentTrip.getPackingList() != null) {
            for (int i = 0; i < currentTrip.getPackingList().size(); i++) {
                PackingItem item = currentTrip.getPackingList().get(i);
                addCheckBoxForItem(item, i);
            }
        }
    }

    private void addCheckBoxForItem(PackingItem item, int position) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(item.getItemName());
        checkBox.setChecked(item.isPacked());
        checkBox.setTextSize(16);
        checkBox.setPadding(8, 8, 8, 8);

        // Update item status when checkbox changes
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setPacked(isChecked);
            tripManager.updateTrip(currentTrip);
        });

        checkBox.setOnLongClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Item")
                    .setMessage("Remove '" + item.getItemName() + "' from packing list?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        currentTrip.getPackingList().remove(position);
                        tripManager.updateTrip(currentTrip);
                        displayPackingList();
                        Toast.makeText(this, Constants.MSG_ITEM_REMOVED, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        packingListContainer.addView(checkBox);
    }

    private void addPackingItem() {
        String itemName = etPackingItem.getText().toString().trim();

        if (itemName.isEmpty()) {
            Toast.makeText(this, Constants.MSG_ENTER_ITEM_NAME, Toast.LENGTH_SHORT).show();
            return;
        }

        currentTrip.addPackingItem(itemName);
        tripManager.updateTrip(currentTrip);

        etPackingItem.setText("");
        displayPackingList();

        Toast.makeText(this, Constants.MSG_ITEM_ADDED, Toast.LENGTH_SHORT).show();
    }

    private void editTrip() {
        Intent intent = new Intent(this, AddTripActivity.class);
        intent.putExtra(Constants.EXTRA_TRIP_ID, currentTrip.getId());
        intent.putExtra(Constants.EXTRA_EDIT_MODE, true);
        startActivity(intent);
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Trip")
                .setMessage("Are you sure you want to delete '" + currentTrip.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    tripManager.deleteTrip(currentTrip.getId());
                    Toast.makeText(this, Constants.MSG_TRIP_DELETED, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void editNotes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Notes");

        final EditText input = new EditText(this);
        input.setText(currentTrip.getNotes());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            currentTrip.setNotes(input.getText().toString());
            tripManager.updateTrip(currentTrip);
            tvNotes.setText(currentTrip.getNotes());
            Toast.makeText(this, Constants.MSG_NOTES_UPDATED, Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String tripId = savedInstanceState.getString(Constants.EXTRA_TRIP_ID);
            if (tripId != null) {
                currentTrip = tripManager.getTripById(tripId);
                if (currentTrip != null) {
                    displayTripDetails();
                    displayPackingList();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentTrip != null) {
            currentTrip = tripManager.getTripById(currentTrip.getId());
            if (currentTrip != null) {
                displayTripDetails();
                displayPackingList();
            }
        }
    }
}
