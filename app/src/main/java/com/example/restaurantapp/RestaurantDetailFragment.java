package com.example.restaurantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.restaurantapp.DataModel.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class RestaurantDetailFragment extends androidx.fragment.app.Fragment {

    //view references
    private ImageView restaurantImage;
    private TextView restaurantName;
    private TextView restaurantMail;
    private TextView restaurantDescription;
    private TextView restaurantAddress;
    private FloatingActionButton fab;

    private Restaurant restaurant;

    //reference for SharedPreferences
    private SharedPreferences preferences;

    private String tagPreferences;
    private String tagRestaurant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        tagRestaurant = MainActivity.tagRestaurant;
        tagPreferences = MainActivity.tagPreferences;

        //linking view with relative references
        restaurantImage = v.findViewById(R.id.restaurantImage);
        restaurantName = v.findViewById(R.id.textName);
        restaurantMail = v.findViewById(R.id.textEmail);
        restaurantDescription = v.findViewById(R.id.textDescription);
        restaurantAddress = v.findViewById(R.id.textRestaurantAddress);
        fab = v.findViewById(R.id.floating_action_button);

        restaurant = retrieveRestaurantData();
        setRestaurantData(restaurant);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditRestaurantActivity();
            }
        });

        return v;
    }

    //this method fulls both imageview and textviews with Restaurant's data
    private void setRestaurantData(Restaurant restaurant){

        restaurantName.setText(restaurant.getName());
        restaurantMail.setText(restaurant.getMail());
        restaurantDescription.setText(restaurant.getDescription());
        restaurantAddress.setText(restaurant.getRestaurantAddress());
        restaurantImage.setImageBitmap(Restaurant.decodeImage(restaurant.getEncodedImage()));

    }

    //this method uses sharedPreference to retrieve the restaurant object
    //the method will return a null restaurant object if no
    //restaurant is found in sharedprefreferences file
    private Restaurant retrieveRestaurantData() {

        preferences = getActivity().getSharedPreferences(tagPreferences, MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString(tagRestaurant, "");
        if(json == null)
            return null;
        else return gson.fromJson(json, Restaurant.class);

    }


    private void goToEditRestaurantActivity() {

        Intent intent = new Intent(getContext(), EditRestaurantActivity.class);
        intent.putExtra(tagRestaurant, restaurant);
        /*
         By using startActivityForResult instead of startActivity
         the application will return to Profile Fragment when the user edits the profile.
         In fact, if we used startActivity, EditRestaurantActivity would start MainActivity another time.
        */
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                //at the moment is useless..I save the Restaurant in EditRestaurantActivity
                //maybe in the next versions the restaurant will not be a singleton anymore and
                //this case will be handled.
                setRestaurantData(Restaurant.getIstance());
                return;
            case RESULT_CANCELED:
                //ignore
                return;
        }
    }
}
