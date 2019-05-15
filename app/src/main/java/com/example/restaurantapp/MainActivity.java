package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.restaurantapp.DataModel.Database;
import com.example.restaurantapp.DataModel.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

import static com.example.restaurantapp.RestaurantDetailFragment.EXTRA_RESTAURANT;

public class MainActivity extends AppCompatActivity {

    //view references
    private BottomNavigationView navigation;
    private Toolbar toolbar;

    public static final int ADD_RESTAURANT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        if (getIntent().getBooleanExtra(LoginActivity.EXTRA_IS_NEW, true))
            createUser();
        else loadFragment(new DailyOfferFragment());

        //setting navigation bar
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
                    fragment = new OrderListFragment();
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
        transaction.commitAllowingStateLoss();
    }

    private void createUser() {
        Intent i = new Intent(this, EditRestaurantActivity.class);
        startActivityForResult(i, ADD_RESTAURANT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == ADD_RESTAURANT){
            Restaurant restaurant;
            switch (resultCode) {
                case RESULT_OK:
                    restaurant = (Restaurant) data.getSerializableExtra(EXTRA_RESTAURANT);
                    Database.getInstance().writeRestaurant(restaurant);
                    loadFragment(new DailyOfferFragment());
                    return;
                case RESULT_CANCELED:
                    createUser();
                    return;
            }
        }
    }

}
