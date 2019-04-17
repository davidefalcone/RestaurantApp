package com.example.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.restaurantapp.DataModel.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

public class EditRestaurantActivity extends AppCompatActivity implements ChoosePictureDialogFragment.onInputListener{

    //references for views
    private TextInputLayout editName;
    private TextInputLayout editMail;
    private TextInputLayout editDescription;
    private TextInputLayout editRestaurantAddress;
    private ImageView editImage;
    private FloatingActionButton fab;

    //reference for restaurant
    private Restaurant restaurant;

    //some tags
    private String tagDialog;
    private final int GALLERY = 0;
    private final int CAMERA = 1;
    private String tagImage;
    private String tagName;
    private String tagMail;
    private String tagDescription;
    private String tagRestaurantAddress;
    private String tagEmpty;
    private final int GAPS_UNFILLED = 0;
    private final int GAPS_COUNT_UNCORRECT = 1;

    //data needed for persistence
    private SharedPreferences preferences;
    private String tagPreferences;
    private String tagRestaurant;

    private boolean imageViewEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        tagDialog = "dialog";
        tagRestaurant = "Restaurant";
        tagPreferences = "preferences";
        tagImage = "image";
        tagName = "name";
        tagMail = "mail";
        tagDescription = "description";
        tagRestaurantAddress = "restaurantaddress";
        tagEmpty = "empty";
        tagRestaurant = MainActivity.tagRestaurant;
        tagPreferences = MainActivity.tagPreferences;

        imageViewEmpty = true;

        preferences = getSharedPreferences(tagPreferences, MODE_PRIVATE);

        //linking view with relative references
        editName = findViewById(R.id.editDish);
        editMail = findViewById(R.id.editEmail);
        editDescription = findViewById(R.id.editDescription);
        editRestaurantAddress = findViewById(R.id.editRestaurantAddress);
        editImage = findViewById(R.id.editImage);
        fab = findViewById(R.id.saveButton);

        setImageview();

        setFAB();

        restaurant = retrieveRestaurantData();
        if(restaurant != null) {
            setRestaurantData(restaurant);
            imageViewEmpty = false;
        }

        restoreDataAfterRotation(savedInstanceState);

    }

    private void saveRestaurant() {

        String name = editName.getEditText().getText().toString();
        String mail = editMail.getEditText().getText().toString();
        String description = editDescription.getEditText().getText().toString();
        String restaurantAddress = editRestaurantAddress.getEditText().getText().toString();

        /*
        There are two main ways to store the restaurant's image:
        1)Translate it in Base64 String and then store the resulting
        string in SharedPreferences file.
        2)Store the image on the device memory and store
        its path in the SharedPreference file.

        In this case, the first method has been choosen.

         */
        String encodedImage = Restaurant.encodeImage(editImage.getDrawable());

        Restaurant restaurant = Restaurant.getIstance();

        restaurant.setName(name);
        restaurant.setMail(mail);
        restaurant.setDescription(description);
        restaurant.setRestaurantAddress(restaurantAddress);
        restaurant.setEncodedImage(encodedImage);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurant);
        editor.putString(tagRestaurant, json);
        editor.commit();

    }

    //this method set the image editimage.png located in the drawable folder
    //and set a click listener.
    private void setImageview() {

        editImage.setImageResource(R.drawable.editimage);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //the user wants to change image
                startDialog();
            }
        });
    }

    //the aim of this method is to show the dialog in which
    // the user can choose between taking a selfie
    // or picking an image from the gallery

    private void startDialog() {

        new ChoosePictureDialogFragment().show(getSupportFragmentManager(), tagDialog);

    }

    //this method starts a special activity in which
    //the user can choose a photo from his/her gallery
    public void gallerySelected() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);

    }

    //this method starts a special activity in which
    //the user can take a photo by the camera
    public void cameraSelected() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    editImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI));
                    imageViewEmpty = false;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            editImage.setImageBitmap((Bitmap) data.getExtras().get("data"));
            imageViewEmpty = false;
        }
    }

    //this method receives a restaurant object as parameter and fill all the gaps
    //of the activity with its data
    private void setRestaurantData(Restaurant restaurant) {

        editName.getEditText().setText(restaurant.getName());
        editMail.getEditText().setText(restaurant.getMail());
        editDescription.getEditText().setText(restaurant.getDescription());
        editRestaurantAddress.getEditText().setText(restaurant.getRestaurantAddress());
        editImage.setImageBitmap(Restaurant.decodeImage(restaurant.getEncodedImage()));

    }

    //this method gets restaurant details from DetailActivity and returns a Restaurant object
    private Restaurant retrieveRestaurantData() {

        Gson gson = new Gson();
        return gson.fromJson(preferences.getString(tagRestaurant,""), Restaurant.class);

    }

    //this method returns a 1 if all gaps are filled, 0 instead.
    private boolean gapsFilled() {

        boolean result = true;

        if (TextUtils.isEmpty(editName.getEditText().getText().toString()))
            result = false;



        if (TextUtils.isEmpty(editMail.getEditText().getText().toString()))
            result = false;


        if (TextUtils.isEmpty(editDescription.getEditText().getText().toString()))
            result = false;


        if (TextUtils.isEmpty(editRestaurantAddress.getEditText().getText().toString()))
            result = false;


        if (imageViewEmpty == true)
            result = false;

        return result;

    }

    //this method simply shows a snackbar
    private void showSnackbar() {
        Snackbar.make(findViewById(R.id.mycoordinatorLayout), R.string.messageMissingMultipleData, Snackbar.LENGTH_SHORT).show();
    }
/*
    this method is called when the screen orientation
    changes. Before pass to the land version of the activity
    each gap are stored.
*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String image = Restaurant.encodeImage(editImage.getDrawable());

        String name = editName.getEditText().getText().toString();
        String mail = editMail.getEditText().getText().toString();
        String description = editDescription.getEditText().getText().toString();
        String restaurantAddress = editRestaurantAddress.getEditText().getText().toString();

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(tagImage, image);
        editor.putBoolean(tagEmpty, imageViewEmpty);
        editor.putString(tagName, name);
        editor.putString(tagMail, mail);
        editor.putString(tagDescription, description);
        editor.putString(tagRestaurantAddress, restaurantAddress);

        editor.commit();

    }

    private void restoreDataAfterRotation(Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            String image = preferences.getString(tagImage, "");

            imageViewEmpty = preferences.getBoolean(tagEmpty, false);

            String name = preferences.getString(tagName, "");
            String mail = preferences.getString(tagMail, "");
            String description = preferences.getString(tagDescription, "");
            String restaurantAddress = preferences.getString(tagRestaurantAddress, "");

            editImage.setImageBitmap(Restaurant.decodeImage(image));
            editName.getEditText().setText(name);
            editMail.getEditText().setText(mail);
            editDescription.getEditText().setText(description);
            editRestaurantAddress.getEditText().setText(restaurantAddress);

        }

    }

    //implementing onInputListener I have to override setInput.
    //In this way I can manage the clicks on the picture dialog
    @Override
    public void setInput(int input) {
        if(input == 0) gallerySelected();
        else cameraSelected();
    }

    private void setFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gapsFilled()) onFinish();
                else showSnackbar();
            }
        });
    }

    private void onFinish(){
        Intent i;
        if (getIntent().getSerializableExtra(tagRestaurant) == null){
            i = new Intent(this, MainActivity.class);
            saveRestaurant();
            startActivity(i);
            finish();
        }else{
            i = getIntent();
            setResult(RESULT_OK, i);
            saveRestaurant();
            finish();
        }
    }

}
