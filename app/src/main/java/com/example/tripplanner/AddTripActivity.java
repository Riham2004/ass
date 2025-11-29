package com.example.tripplanner;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import com.example.tripplanner.utils.Constants;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.data.TripManager;
import com.example.tripplanner.models.Trip;

import java.util.Calendar;
import android.content.Intent;

public class AddTripActivity extends AppCompatActivity {

    private EditText etTripName, etSource, etDestination, etBudget, etNotes;
    private Button btnStartDate, btnEndDate, btnSaveTrip;
    private RadioGroup rgTripType, rgPriority;
    private Switch switchConfirmed;

    private String startDate = "";
    private String endDate = "";

    private TripManager tripManager;

    private Trip editingTrip = null;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        etTripName = findViewById(R.id.etTripName);
        etSource = findViewById(R.id.etSource);
        etDestination = findViewById(R.id.etDestination);
        etBudget = findViewById(R.id.etBudget);
        etNotes = findViewById(R.id.etNotes);

        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);

        rgTripType = findViewById(R.id.rgTripType);
        rgPriority = findViewById(R.id.rgPriority);
        switchConfirmed = findViewById(R.id.switchConfirmed);

        tripManager = new TripManager(this);

        // Check if editing existing trip
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("EDIT_MODE", false);

        if (isEditMode) {
            String tripId = intent.getStringExtra("TRIP_ID");
            editingTrip = tripManager.getTripById(tripId);
            if (editingTrip != null) {
                fillFieldsForEdit();
                btnSaveTrip.setText("Update Trip");
            }
        }

        // DatePickers
        btnStartDate.setOnClickListener(v -> showDatePicker(true));
        btnEndDate.setOnClickListener(v -> showDatePicker(false));

        // Save button
        btnSaveTrip.setOnClickListener(v -> saveTrip());
    }

    private void fillFieldsForEdit() {
        etTripName.setText(editingTrip.getName());
        etSource.setText(editingTrip.getSource());
        etDestination.setText(editingTrip.getDestination());
        etBudget.setText(String.valueOf(editingTrip.getBudget()));
        etNotes.setText(editingTrip.getNotes());

        startDate = editingTrip.getStartDate();
        endDate = editingTrip.getEndDate();
        btnStartDate.setText("Start: " + startDate);
        btnEndDate.setText("End: " + endDate);

        switchConfirmed.setChecked(editingTrip.isCompleted());

        String type = editingTrip.getTripType();
        if (type.equals("Adventure")) {
            rgTripType.check(R.id.rbAdventure);
        } else if (type.equals("Leisure")) {
            rgTripType.check(R.id.rbLeisure);
        } else {
            rgTripType.check(R.id.rbBusiness);
        }

        String priority = editingTrip.getPriority();
        if (priority.equals("High")) {
            rgPriority.check(R.id.rbHigh);
        } else if (priority.equals("Low")) {
            rgPriority.check(R.id.rbLow);
        } else {
            rgPriority.check(R.id.rbMedium);
        }
    }

    private void showDatePicker(boolean isStartDate) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    if (isStartDate) {
                        startDate = date;
                        btnStartDate.setText("Start: " + date);
                    } else {
                        endDate = date;
                        btnEndDate.setText("End: " + date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveTrip() {
        String name = etTripName.getText().toString().trim();
        String source = etSource.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        String budgetStr = etBudget.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (name.isEmpty() || source.isEmpty() || destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || budgetStr.isEmpty()) {
            Toast.makeText(this, Constants.MSG_FILL_ALL_FIELDS, Toast.LENGTH_SHORT).show();
            return;
        }

        double budget = Double.parseDouble(budgetStr);

        int selectedTypeId = rgTripType.getCheckedRadioButtonId();
        RadioButton selectedRadio = findViewById(selectedTypeId);
        String tripType = selectedRadio != null ? selectedRadio.getText().toString() : "Leisure";

        int selectedPriorityId = rgPriority.getCheckedRadioButtonId();
        RadioButton selectedPriorityRadio = findViewById(selectedPriorityId);
        String priority = selectedPriorityRadio != null ? selectedPriorityRadio.getText().toString() : "Medium";

        boolean confirmed = switchConfirmed.isChecked();

        int imageResId = R.drawable.ic_trip_default;

        if (isEditMode && editingTrip != null) {
            // Update existing trip
            editingTrip.setName(name);
            editingTrip.setSource(source);
            editingTrip.setDestination(destination);
            editingTrip.setStartDate(startDate);
            editingTrip.setEndDate(endDate);
            editingTrip.setBudget(budget);
            editingTrip.setTripType(tripType);
            editingTrip.setCompleted(confirmed);
            editingTrip.setNotes(notes);
            editingTrip.setPriority(priority);
            editingTrip.setImageResId(imageResId);

            tripManager.updateTrip(editingTrip);
            Toast.makeText(this, "Trip updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Add new trip
            Trip trip = new Trip(name, source, destination, startDate, endDate, budget, tripType, priority, imageResId);
            trip.setCompleted(confirmed);
            trip.setNotes(notes);

            tripManager.addTrip(trip);
            Toast.makeText(this, "Trip added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TRIP_NAME", etTripName.getText().toString());
        outState.putString("SOURCE", etSource.getText().toString());
        outState.putString("DESTINATION", etDestination.getText().toString());
        outState.putString("START_DATE", startDate);
        outState.putString("END_DATE", endDate);
        outState.putString("BUDGET", etBudget.getText().toString());
        outState.putString("NOTES", etNotes.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            etTripName.setText(savedInstanceState.getString("TRIP_NAME"));
            etSource.setText(savedInstanceState.getString("SOURCE"));
            etDestination.setText(savedInstanceState.getString("DESTINATION"));
            startDate = savedInstanceState.getString("START_DATE", "");
            endDate = savedInstanceState.getString("END_DATE", "");
            if (!startDate.isEmpty()) {
                btnStartDate.setText("Start: " + startDate);
            }
            if (!endDate.isEmpty()) {
                btnEndDate.setText("End: " + endDate);
            }
            etBudget.setText(savedInstanceState.getString("BUDGET"));
            etNotes.setText(savedInstanceState.getString("NOTES"));
        }
    }
}
