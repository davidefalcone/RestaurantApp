package com.example.restaurantapp.DataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class ReservationDatabase {
    private ArrayList<Reservation> reservations;
    private static ReservationDatabase instance;

    public static ReservationDatabase getInstance(){
        if(instance == null) instance = new ReservationDatabase();
        return instance;
    }

    private ReservationDatabase(){
        reservations = new ArrayList<>();
        fillDatabase();
    }

    public void addReservation(Reservation reservation){
        if(getReservation(reservation.getId()) == null) reservations.add(reservation);
        else {
            removeReservation(reservation.getId());
            reservations.add(reservation);
        }
    }

    public Reservation getReservation(int id){
        for (int i = 0; i>reservations.size(); i++){
            if(id == reservations.get(i).getId()) return reservations.get(i);
        }
        return null;
    }

    public void removeReservation(int id){
        reservations.remove(getReservation(id));
    }

    public ArrayList<Reservation> getReservations(){
        return reservations;
    }

    private void fillDatabase(){
        //date creation
        String dateInString = "April 29, 2019 at 8:30 pm";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy 'at' hh:mm a");
        Date date = null;
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        ArrayList<Dish> orderedDishes = new ArrayList<>();
        orderedDishes.add(DishDatabase.getInstance().getDish(2));
        orderedDishes.add(DishDatabase.getInstance().getDish(0));
        reservations.add(new Reservation("John Doe", date, orderedDishes, "Call me when you reach the building"));

        //date creation
        dateInString = "April 29, 2019 at 9:30 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(DishDatabase.getInstance().getDish(1));
        orderedDishes.add(DishDatabase.getInstance().getDish(0));
        reservations.add(new Reservation("Clara Rainolds", date, orderedDishes, "Bring me some sauces"));

        //date creation
        dateInString = "April 28, 2019 at 8:00 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(DishDatabase.getInstance().getDish(0));
        orderedDishes.add(DishDatabase.getInstance().getDish(2));
        reservations.add(new Reservation("Mohamed Fahdil", date, orderedDishes));

        //date creation
        dateInString = "April 28, 2019 at 9:30 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(DishDatabase.getInstance().getDish(1));
        reservations.add(new Reservation("Domenico Monaco", date, orderedDishes));

        //date creation
        dateInString = "April 27, 2019 at 10:30 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(DishDatabase.getInstance().getDish(2));
        reservations.add(new Reservation("Daniele Gallo", date, orderedDishes));

        //date creation
        dateInString = "April 28, 2019 at 7:30 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(DishDatabase.getInstance().getDish(2));
        orderedDishes.add(DishDatabase.getInstance().getDish(1));
        reservations.add(new Reservation("Freddie Mercury", date, orderedDishes, "No hot sauce"));


    }

}
