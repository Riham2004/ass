package com.example.tripplanner.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.tripplanner.models.Trip;
import com.example.tripplanner.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TripManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public TripManager(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = prefs.edit();
        this.gson = new Gson();
    }

    public void saveTrips(List<Trip> trips) {
        String json = gson.toJson(trips);
        editor.putString(Constants.PREFS_TRIPS_KEY, json);
        editor.commit();
    }

    public List<Trip> loadTrips() {
        String json = prefs.getString(Constants.PREFS_TRIPS_KEY, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Trip>>(){}.getType();
        List<Trip> trips = gson.fromJson(json, type);
        return trips != null ? trips : new ArrayList<>();
    }

    public void addTrip(Trip trip) {
        List<Trip> trips = loadTrips();
        trips.add(trip);
        saveTrips(trips);
    }

    public void updateTrip(Trip updatedTrip) {
        List<Trip> trips = loadTrips();
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getId().equals(updatedTrip.getId())) {
                trips.set(i, updatedTrip);
                break;
            }
        }
        saveTrips(trips);
    }

    public void deleteTrip(String tripId) {
        List<Trip> trips = loadTrips();
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getId().equals(tripId)) {
                trips.remove(i);
                break;
            }
        }
        saveTrips(trips);
    }

    public Trip getTripById(String tripId) {
        List<Trip> trips = loadTrips();
        for (Trip trip : trips) {
            if (trip.getId().equals(tripId)) {
                return trip;
            }
        }
        return null;
    }

    public List<Trip> searchTrips(String query) {
        List<Trip> allTrips = loadTrips();
        List<Trip> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Trip trip : allTrips) {
            if (trip.getName().toLowerCase().contains(lowerQuery) ||
                    trip.getDestination().toLowerCase().contains(lowerQuery)) {
                results.add(trip);
            }
        }
        return results;
    }

    public List<Trip> filterByType(String tripType) {
        List<Trip> allTrips = loadTrips();
        if (tripType.equals(Constants.TRIP_TYPE_ALL)) {
            return allTrips;
        }
        List<Trip> results = new ArrayList<>();
        for (Trip trip : allTrips) {
            if (trip.getTripType().equals(tripType)) {
                results.add(trip);
            }
        }
        return results;
    }

    public int getTripsCount() {
        return loadTrips().size();
    }
}