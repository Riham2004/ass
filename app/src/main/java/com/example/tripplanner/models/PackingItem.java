package com.example.tripplanner.models;

import java.io.Serializable;

public class PackingItem implements Serializable {
    private String itemName;
    private boolean isPacked;

    public PackingItem() {
    }

    public PackingItem(String itemName) {
        this.itemName = itemName;
        this.isPacked = false;
    }

    public PackingItem(String itemName, boolean isPacked) {
        this.itemName = itemName;
        this.isPacked = isPacked;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPacked() {
        return isPacked;
    }

    public void setPacked(boolean packed) {
        isPacked = packed;
    }

    @Override
    public String toString() {
        return itemName + (isPacked ? " ✓" : "");
    }
}