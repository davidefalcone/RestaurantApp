package com.example.restaurantapp.DataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Reservation implements Serializable {
    private int id;
    private String customer;
    private Date date;
    private ArrayList<Dish> ordedDishes;
    private String notes;
    private static int nIstances = 0;

    public Reservation(String customer, Date date, ArrayList<Dish> ordedDishes, String notes) {
        this.customer = customer;
        this.date = date;
        this.ordedDishes = ordedDishes;
        this.notes = notes;
        this.id = nIstances;
        nIstances++;
    }

    public Reservation(String customer, Date date, ArrayList<Dish> ordedDishes) {
        this.customer = customer;
        this.date = date;
        this.ordedDishes = ordedDishes;
        this.id = nIstances;
        nIstances++;
    }

    public int getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Dish> getOrdedDishes() {
        return ordedDishes;
    }

    public void setOrdedDishes(ArrayList<Dish> ordedDishes) {
        this.ordedDishes = ordedDishes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static int getnIstances() {
        return nIstances;
    }

    public static void setnIstances(int nIstances) {
        Reservation.nIstances = nIstances;
    }

    public String getOrderedDishesToString(){
        String dishes = "";
        for (int i = 0; i<ordedDishes.size(); i++){
            dishes = dishes + ordedDishes.get(i).toString();
            if(i != (ordedDishes.size()-1)) dishes = dishes + ", ";
        }
        return dishes;
    }

}
