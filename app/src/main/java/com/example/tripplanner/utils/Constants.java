package com.example.tripplanner.utils;

/**
 * Centralized Constants Class
 * Contains all constant values used across the application
 */
public class Constants {

    // Intent Keys
    public static final String EXTRA_TRIP_ID = "TRIP_ID";
    public static final String EXTRA_EDIT_MODE = "EDIT_MODE";

    // SharedPreferences Keys
    public static final String PREFS_TRIPS_KEY = "TRIPS_DATA";

    // Trip Types
    public static final String TRIP_TYPE_ADVENTURE = "Adventure";
    public static final String TRIP_TYPE_LEISURE = "Leisure";
    public static final String TRIP_TYPE_BUSINESS = "Business";
    public static final String TRIP_TYPE_ALL = "All";

    // Priority Levels
    public static final String PRIORITY_HIGH = "High";
    public static final String PRIORITY_MEDIUM = "Medium";
    public static final String PRIORITY_LOW = "Low";

    // Default Values
    public static final int DEFAULT_IMAGE = 0;
    public static final String DEFAULT_PRIORITY = PRIORITY_MEDIUM;
    public static final String DEFAULT_TRIP_TYPE = TRIP_TYPE_LEISURE;

    // Validation Messages
    public static final String MSG_FILL_ALL_FIELDS = "Please fill all fields";
    public static final String MSG_TRIP_ADDED = "Trip added successfully";
    public static final String MSG_TRIP_UPDATED = "Trip updated successfully";
    public static final String MSG_TRIP_DELETED = "Trip deleted";
    public static final String MSG_ITEM_ADDED = "Item added";
    public static final String MSG_ITEM_REMOVED = "Item removed";
    public static final String MSG_ENTER_ITEM_NAME = "Please enter item name";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate Constants class");
    }


}