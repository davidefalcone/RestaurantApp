package com.example.restaurantapp.DataModel;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Dish implements Serializable {

    //attributes
    private String name;
    private String description;
    private int price;
    private int quantity;
    private String ID;
    private String restaurantID;

    //getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
