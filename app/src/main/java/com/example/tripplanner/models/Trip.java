package com.example.tripplanner.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Trip implements Serializable {
    private String id;
    private String name;
    private String destination;
    private String startDate;
    private String endDate;
    private double budget;
    private String tripType; // "Adventure", "Leisure", "Business"
    private boolean isCompleted;
    private ArrayList<PackingItem> packingList;
    private String notes;

    // ✅ جديد:
    private String priority;     // "High", "Medium", "Low"
    private int imageResId;      // R.drawable.ic_trip_default أو أي صورة ثابتة

    // Constructor افتراضي (مطلوب لـ Gson)
    public Trip() {
        this.id = UUID.randomUUID().toString();
        this.packingList = new ArrayList<>();
        this.isCompleted = false;
        this.priority = "Medium";
        this.imageResId = 0; // لاحقًا نضبطها في AddTripActivity
    }

    // Constructor مخصص
    public Trip(String name, String destination, String startDate, String endDate,
                double budget, String tripType, String priority, int imageResId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.tripType = tripType;
        this.isCompleted = false;
        this.packingList = new ArrayList<>();
        this.notes = "";
        this.priority = priority;
        this.imageResId = imageResId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public ArrayList<PackingItem> getPackingList() {
        return packingList;
    }

    public void setPackingList(ArrayList<PackingItem> packingList) {
        this.packingList = packingList;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ✅ جديد:
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    // Helper method
    public void addPackingItem(String itemName) {
        packingList.add(new PackingItem(itemName));
    }

    @Override
    public String toString() {
        return name + " - " + destination;
    }
}
