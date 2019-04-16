package com.example.restaurantapp.DataModel;

import android.content.Context;
import com.example.restaurantapp.R;

import java.util.ArrayList;

public class DishDatabase {

    private static DishDatabase instance;


    private static ArrayList<Dish> list;

    public static DishDatabase getInstance(){
        if(instance == null)
            instance = new DishDatabase();
        return instance;
    }
    public static void setDishDatabase (DishDatabase database){
        instance = database;
    }

    private DishDatabase() {
        list = new ArrayList<>();
    }

    public void addDish(Dish dish) {
        if (getDish(dish.getId()) == null) list.add(dish);
        else {
            removeDish(dish.getId());
            list.add(dish);
        }

    }

    //this method removes from the DishDatabase the dish which has the same id as the one received as parameter
    public void removeDish(int id) {
        list.remove(getDish(id));
    }

    //this method receives as a parameter the id and return a dish which has the same id
    public Dish getDish(int id) {
        Dish dish;
        for (int i = 0; i<list.size(); i++){
            dish = list.get(i);
            if(dish.getId() == id) return dish;
        }
        return null;
    }


    public static void fillDatabase(Context context) {
//        String image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.brasato_al_barolo));
//        list.add(new Dish("Brasato al Barolo", "One of the Piemontese dishes for excellence where " +
//                "the queen of meat meets the king of wines. A perfect combination to be enjoyed.", 15, 40, image));
//        image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.bagna_cauda));
//        list.add(new Dish("Bagna cauda", "A simple dish of the rural tradition that is " +
//                "consumed when the temperatures start to fall.", 10, 50, image));
        String image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.agnolotti_del_plin));
        list.add(new Dish("Agnolotti del plin", "Special Piedmontese filled pasta characteristic" +
                " of the Langhe and Monferrato area", 10, 20 , image));
        image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.vitello_tonnato));
        list.add(new Dish("Vitello tonnato", "A tasty dish made with a round " +
                "fassone marinated in dry white wine and seasoned with aromas for at least half a day.", 11, 25, image));
        image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.bonet));
        list.add(new Dish("Bonet", "An ancient Piemontese pudding " +
                "made with sugar, eggs, milk, cocoa, rum and dried macaroons.", 6, 60 , image));
        image = Dish.encodeImage(context.getResources().getDrawable(R.drawable.tajarin_al_tartufo_bianco_d_alba));
        list.add(new Dish("Tajarin al tartufo bianco d’Alba", "The white truffle of Alba, meets in " +
                "this dish the mythical tajarin, a typical egg pasta from the Langhe and Monferrato.", 20, 15, image));
    }

    public ArrayList<Dish> getDishesList() {
        return list;
    }
}
