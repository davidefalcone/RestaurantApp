package com.example.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.restaurantapp.DataModel.Database;
import com.example.restaurantapp.DataModel.Restaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class EditRestaurantActivity extends AppCompatActivity implements ChoosePictureDialogFragment.onInputListener{

    //references for views
    private TextInputLayout editName;
    private TextInputLayout editMail;
    private TextInputLayout editDescription;
    private TextInputLayout editRestaurantAddress;
    private ImageView editImage;
    private FloatingActionButton fab;

    //some tags
    private String tagDialog = "dialog";
    private final int EDIT_RESTAURANT = RestaurantDetailFragment.EDIT_RESTAURANT;
    private final int ADD_RESTAURANT = RestaurantDetailFragment.ADD_RESTAURANT;
    private final String EXTRA_RESTAURANT = RestaurantDetailFragment.EXTRA_RESTAURANT;
    private final int GALLERY = 0;
    private final int CAMERA = 1;

    private boolean imageViewEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        //linking view with relative references
        editName = findViewById(R.id.editDish);
        editMail = findViewById(R.id.editEmail);
        editDescription = findViewById(R.id.editDescription);
        editRestaurantAddress = findViewById(R.id.editRestaurantAddress);
        editImage = findViewById(R.id.editImage);
        fab = findViewById(R.id.saveButton);

        setImageview();

        setFAB();


        if(retrieveRestaurantData() != null) {
            setRestaurantData(retrieveRestaurantData());
            imageViewEmpty = false;
        }


    }

    //this method set the image editimage.png located in the drawable folder
    //and set a click listener.
    private void setImageview() {
        imageViewEmpty = true;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference path = FirebaseStorage.getInstance().getReference()
                .child("restaurants").child(user.getUid()).child("account_image.jpg");
        Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(editImage);
    }

    //this method gets restaurant details from DetailActivity and returns a Restaurant object
    private Restaurant retrieveRestaurantData() {
        Intent i = getIntent();
        Restaurant restaurant = (Restaurant) i.getSerializableExtra(EXTRA_RESTAURANT);
        return restaurant;
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

    private Restaurant readRestaurant() {
        Restaurant restaurant = new Restaurant();

        restaurant.setName(editName.getEditText().getText().toString());
        restaurant.setMail(editMail.getEditText().getText().toString());
        restaurant.setDescription(editDescription.getEditText().getText().toString());
        restaurant.setRestaurantAddress(editRestaurantAddress.getEditText().getText().toString());

        return restaurant;
    }

    private void onFinish(){
        Restaurant restaurant = readRestaurant();
        uploadImage(restaurant);
        Intent i = getIntent();
        i.putExtra(EXTRA_RESTAURANT, restaurant);
        setResult(RESULT_OK, i);
        finish();
    }

    private void uploadImage(Restaurant restaurant){
        View v = findViewById(R.id.loadingView);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference imagePath = FirebaseStorage.getInstance().getReference().
                child("restaurants").child(user.getUid()).child("account_image.jpg");
        imagePath.delete();
        restaurant.setImageURL(getResources().getString(R.string.googleBucket)+imagePath.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)editImage.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagePath.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                v.setVisibility(View.GONE);
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                v.setVisibility(View.VISIBLE);
                progressBar.setProgress((int)(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()));
            }
        });
        return;
    }
}
