package com.example.restaurantapp.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private String ID;
    private Restaurant restaurant;
    private String customerID;
    private ArrayList<Dish> orderedDishes;
    private String notes;
    private DeliveryMan deliveryMan;
    private int total;
    private boolean read;
    private int status;

    //constants for status
    public static final int RECEIVED = 0;
    public static final int ACCEPTED = 1;
    public static final int IN_DELIVERY = 2;
    public static final int DELIVERED = 3;
    public static final int CANCELLED = -1;

    public Order(Restaurant restaurant) {
        this.restaurant = restaurant;
        total = 0;
        orderedDishes = new ArrayList<>();
        status = RECEIVED;
        read = false;
    }

    public Order () {}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public ArrayList<Dish> getOrderedDishes() {
        return orderedDishes;
    }

    public void setOrderedDishes(ArrayList<Dish> orderedDishes) {
        this.orderedDishes = orderedDishes;
    }

    public String getOrderedDishesString() {
        String result = "";
        for (int i = 0 ; i < orderedDishes.size(); i++) {
            if (i != orderedDishes.size() - 1)
            {
                result = result + orderedDishes.get(i).getName() + ", ";
            }else result = result + orderedDishes.get(i).getName() + ".";
        }
        return result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void addDish(Dish dish) {
        orderedDishes.add(dish);
    }

    public void removeDish(Dish dish) {
        orderedDishes.remove(dish);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }
}
