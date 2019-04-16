package com.example.restaurantapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.restaurantapp.DataModel.DishDatabase;
import com.example.restaurantapp.DataModel.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    //view references
    private BottomNavigationView navigation;
    private Toolbar toolbar;

    //reference for sharedPreferences
    private SharedPreferences preferences;

    //this strings will be used for managing sharedPreferences
    public static String tagRestaurant;
    public static String tagPreferences;
    private String tagDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagRestaurant = "Restaurant";
        tagPreferences = "preferences";
        tagDishes = "dishes";

        //setting toolbar
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        //setting navigation bar
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        preferences = getSharedPreferences(tagPreferences, MODE_PRIVATE);

        if(!restaurantExist())
            goToEditRestaurantActivity();

        getPreferences();

        loadFragment(new DailyOfferFragment());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {

                case R.id.profile:

                    fragment = new RestaurantDetailFragment();
                    loadFragment(fragment);
                    invalidateOptionsMenu();
                    return true;

                case R.id.dishes:

                    fragment = new DailyOfferFragment();
                    loadFragment(fragment);
                    invalidateOptionsMenu();
                    return true;

                case R.id.reservations:
                    fragment = new ReservationFragment();
                    loadFragment(fragment);
                    invalidateOptionsMenu();
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public boolean restaurantExist() {

        Restaurant restaurant;
        Gson gson = new Gson();

        restaurant = gson.fromJson(preferences.getString(tagRestaurant, ""), Restaurant.class);

        return !(restaurant == null);

    }

    private void goToEditRestaurantActivity() {

        Intent i = new Intent(this, EditRestaurantActivity.class);
        startActivity(i);

    }

    //this method retrieve the DishDatabase thanks to sharedpref.
    //if no sharedpref were found, the method allow the DishDatabase class to create a new instance
    private void getPreferences(){
        DishDatabase dishDatabase;
        SharedPreferences preferences = getSharedPreferences(tagDishes, MODE_PRIVATE);

        Gson gson = new Gson();
        if(preferences.getString(tagDishes,"")!= "") {
            dishDatabase = gson.fromJson(preferences.getString(tagDishes, ""), DishDatabase.class);
            DishDatabase.setDishDatabase(dishDatabase);
        }
        else dishDatabase = DishDatabase.getInstance();
        DishDatabase.fillDatabase(this);
    }

}
