package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.restaurantapp.DataModel.Database;
import com.example.restaurantapp.DataModel.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class RestaurantDetailFragment extends androidx.fragment.app.Fragment {

    //view references
    private ImageView restaurantImage;
    private TextView restaurantName;
    private TextView restaurantMail;
    private TextView restaurantDescription;
    private TextView restaurantAddress;
    private FloatingActionButton fab;

    private Restaurant restaurant;

    public static final String EXTRA_RESTAURANT = "EXTRA_RESTAURANT";

    public static final int EDIT_RESTAURANT = 0;
    public static final int ADD_RESTAURANT = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        //linking view with relative references
        restaurantImage = v.findViewById(R.id.restaurantImage);
        restaurantName = v.findViewById(R.id.textName);
        restaurantMail = v.findViewById(R.id.textEmail);
        restaurantDescription = v.findViewById(R.id.textDescription);
        restaurantAddress = v.findViewById(R.id.textRestaurantAddress);
        fab = v.findViewById(R.id.floating_action_button);

        getRestaurantData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditRestaurantActivity(EDIT_RESTAURANT);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference path = FirebaseStorage.getInstance().getReference()
                .child("restaurants").child(user.getUid()).child("account_image.jpg");
        Glide.with(getContext()).load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(restaurantImage);
    }


    private void goToEditRestaurantActivity(int requestcode) {
        Intent intent = new Intent(getContext(), EditRestaurantActivity.class);
        intent.putExtra(EXTRA_RESTAURANT, restaurant);
        /*
         By using startActivityForResult instead of startActivity
         the application will return to Profile Fragment when the user edits the profile.
         In fact, if we used startActivity, EditRestaurantActivity would start MainActivity another time.
        */
        startActivityForResult(intent, requestcode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Restaurant restaurant = (Restaurant) data.getSerializableExtra(EXTRA_RESTAURANT);
        switch (requestCode){
            case ADD_RESTAURANT:
                switch (resultCode){
                    case RESULT_OK:
                        Database.getInstance().writeRestaurant(restaurant);
                        setRestaurantData(restaurant);
                        return;
                    case RESULT_CANCELED:
                        //TODO: check here!
                        goToEditRestaurantActivity(ADD_RESTAURANT);
                        return;
                }
            case EDIT_RESTAURANT:
                switch (resultCode){
                    case RESULT_OK:
                        Database.getInstance().writeRestaurant(restaurant);
                        setRestaurantData(restaurant);
                        return;
                    case RESULT_CANCELED:
                        return;
                }
        }
    }

    private void getRestaurantData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference path = FirebaseDatabase.getInstance().getReference()
                .child("restaurants").child(user.getUid()).child("account");

        path.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO: add a progress bar
                if (dataSnapshot.getValue() == null) goToEditRestaurantActivity(ADD_RESTAURANT);
                else {
                    restaurant = dataSnapshot.getValue(Restaurant.class);
                    setRestaurantData(restaurant);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}