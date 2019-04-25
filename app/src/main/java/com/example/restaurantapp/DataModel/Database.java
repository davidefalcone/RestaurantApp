package com.example.restaurantapp.DataModel;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.restaurantapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Database {
    private static Database instance;
    DatabaseReference path;
    FirebaseUser user;
    private ArrayList<Reservation> reservationList;
    private ArrayList<Dish> dishList;

    private Database() {
        reservationList = new ArrayList<>();
        dishList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        path = FirebaseDatabase.getInstance().getReference().child("restaurants").child(user.getUid());
    }

    public static Database getInstance() {
        if(instance == null) instance = new Database();
        return instance;
    }

    public ArrayList<Dish> getDishList() {
        return dishList;
    }

    public ArrayList<Reservation> getReservationList() {
        return reservationList;
    }

    public void addReservation(Reservation reservation){
        if(getReservation(reservation.getId()) == null) reservationList.add(reservation);
        else {
            removeReservation(reservation.getId());
            reservationList.add(reservation);
        }
    }

    public Reservation getReservation(int id){
        for (int i = 0; i>reservationList.size(); i++){
            if(id == reservationList.get(i).getId()) return reservationList.get(i);
        }
        return null;
    }

    public void removeReservation(int id){
        reservationList.remove(getReservation(id));
    }

    public void fillReservationDatabase(){
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
        orderedDishes.add(getDish(2));
        orderedDishes.add(getDish(0));
        reservationList.add(new Reservation("John Doe", date, orderedDishes, "Call me when you reach the building"));

        //date creation
        dateInString = "April 29, 2019 at 9:30 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(getDish(1));
        orderedDishes.add(getDish(0));
        reservationList.add(new Reservation("Clara Rainolds", date, orderedDishes, "Bring me some sauces"));

        //date creation
        dateInString = "April 28, 2019 at 8:00 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(getDish(0));
        orderedDishes.add(getDish(2));
        reservationList.add(new Reservation("Mohamed Fahdil", date, orderedDishes));

        //date creation
        dateInString = "April 28, 2019 at 9:30 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(getDish(1));
        reservationList.add(new Reservation("Domenico Monaco", date, orderedDishes));

        //date creation
        dateInString = "April 27, 2019 at 10:30 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(getDish(2));
        reservationList.add(new Reservation("Daniele Gallo", date, orderedDishes));

        //date creation
        dateInString = "April 28, 2019 at 7:30 pm";
        try{
            date = sdf.parse(dateInString);
        }catch(ParseException e){
            //ignore
        }
        //setting orderedDishes
        orderedDishes = new ArrayList<>();
        orderedDishes.add(getDish(2));
        orderedDishes.add(getDish(1));
        reservationList.add(new Reservation("Freddie Mercury", date, orderedDishes, "No hot sauce"));


    }

    public void addDish(Dish dish) {
        if (getDish(dish.getId()) == null) dishList.add(dish);
        else {
            removeDish(dish.getId());
            dishList.add(dish);
        }

    }

    //this method removes from the DishDatabase the dish which has the same id as the one received as parameter
    public void removeDish(int id) {
        dishList.remove(getDish(id));
    }

    //this method receives as a parameter the id and return a dish which has the same id
    public Dish getDish(int id) {
        Dish dish;
        for (int i = 0; i<dishList.size(); i++){
            dish = dishList.get(i);
            if(dish.getId() == id) return dish;
        }
        return null;
    }

    public void fillDishDatabase(Context context) {
        String image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.agnolotti_del_plin));
        dishList.add(new Dish("Agnolotti del plin", "Special Piedmontese filled pasta characteristic" +
                " of the Langhe and Monferrato area", 10, 20 , image));
        image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.vitello_tonnato));
        dishList.add(new Dish("Vitello tonnato", "A tasty dish made with a round " +
                "fassone marinated in dry white wine and seasoned with aromas for at least half a day.", 11, 25, image));
        image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.bonet));
        dishList.add(new Dish("Bonet", "An ancient Piemontese pudding " +
                "made with sugar, eggs, milk, cocoa, rum and dried macaroons.", 6, 60 , image));
        image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.tajarin_al_tartufo_bianco_d_alba));
        dishList.add(new Dish("Tajarin al tartufo bianco d’Alba", "The white truffle of Alba, meets in " +
                "this dish the mythical tajarin, a typical egg pasta from the Langhe and Monferrato.", 20, 15, image));
    }

    public void writeRestaurant(Restaurant restaurant) {
        path.child("account").setValue(restaurant);
    }
}
