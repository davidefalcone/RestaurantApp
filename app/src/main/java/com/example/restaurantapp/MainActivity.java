package com.example.restaurantapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.restaurantapp.DataModel.Database;
import com.example.restaurantapp.DataModel.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;

    //view references
    private BottomNavigationView navigation;
    private Toolbar toolbar;

    //this strings will be used for managing sharedPreferences
    public static String tagRestaurant;
//    public static String tagPreferences;
//    private String tagDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        tagRestaurant = "Restaurant";
//        tagDishes = EditDishActivity.tagDishes;

        //setting toolbar
        //tagPreferences = "preferences";
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        //setting navigation bar
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new RestaurantDetailFragment());
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
}
