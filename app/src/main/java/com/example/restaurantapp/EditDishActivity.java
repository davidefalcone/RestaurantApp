package com.example.restaurantapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.restaurantapp.DataModel.Dish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class EditDishActivity extends AppCompatActivity implements ChoosePictureDialogFragment.onInputListener{

    //view references
    private ImageView editImage;
    private TextInputLayout editName;
    private TextInputLayout editDescription;
    private TextInputLayout editPrice;
    private TextInputLayout editQuantity;
    private FloatingActionButton fab;
    private Button deleteButton;

    public static final String tagDialog = "dialog";
    public static final String tagDishes = "dishes";
    public static final int GALLERY = 0;
    public static final int CAMERA  = 1;

    private boolean imageViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        //getting view references
        editImage = findViewById(R.id.dishImage);
        editName = findViewById(R.id.editDish);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        editQuantity = findViewById(R.id.editQuantity);
        fab = findViewById(R.id.floating_action_button);
        deleteButton = findViewById(R.id.deleteButton);

        setImageView();

        setFAB();

        setButton();

        if( getDish() != null) setGaps(getDish());
    }

    //this method sets an image in the imageview and
    //adds a click listener
    private void setImageView() {

        imageViewEmpty = true;
        editImage.setImageResource(R.drawable.editimage);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDialog();

            }
        });

    }

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

    private void setButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                setResult(DailyOfferFragment.RESULT_DELETED, i);
                i.putExtra(tagDishes, readDish());
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

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

    private void setFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gapsFilled()){
                    returntoDailyOfferActivity(readDish());
                }else
                    showSnackbar();
            }
        });
    }



    private boolean gapsFilled() {

        boolean result = true;

        if (TextUtils.isEmpty(editName.getEditText().getText().toString()))
            result = false;


        if (TextUtils.isEmpty(editDescription.getEditText().getText().toString()))
            result = false;


        if (TextUtils.isEmpty(editPrice.getEditText().getText().toString()))
            result = false;


        if (TextUtils.isEmpty(editQuantity.getEditText().getText().toString()))
            result = false;


        if (imageViewEmpty == true)
            result = false;

        return result;

    }

    private void showSnackbar() {

        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageMissingMultipleData, Snackbar.LENGTH_SHORT).show();

    }

    private Dish readDish() {
        String name = editName.getEditText().getText().toString();
        String description = editDescription.getEditText().getText().toString();
        int price = Integer.parseInt(editPrice.getEditText().getText().toString());
        int quantity = Integer.parseInt(editQuantity.getEditText().getText().toString());
        String encodedImage = Dish.encodeImage(editImage.getDrawable());

        Dish dish;
        if(getIntent().getSerializableExtra(tagDishes) == null) dish  = new Dish();
        else dish = (Dish) getIntent().getSerializableExtra(tagDishes);

        dish.setName(name);
        dish.setDescription(description);
        dish.setPrice(price);
        dish.setQuantity(quantity);
        dish.setEncodedImage(encodedImage);

        return dish;
    }

    private void returntoDailyOfferActivity(Dish dish) {
        Intent i = getIntent();
        setResult(RESULT_OK, i);
        i.putExtra(tagDishes, dish);
        finish();
    }

    @Override
    public void setInput(int input) {
        if(input == 0) gallerySelected();
        else cameraSelected();
    }

    private Dish getDish() {
        if(getIntent().getSerializableExtra(tagDishes) != null )
            return ((Dish) getIntent().getSerializableExtra(tagDishes));
        else return null;
    }

    private void setGaps(Dish dish) {
        editImage.setImageBitmap(Dish.decodeImage(dish.getEncodedImage()));
        imageViewEmpty = false;
        editName.getEditText().setText(dish.getName());
        editDescription.getEditText().setText(dish.getDescription());
        editPrice.getEditText().setText(Integer.toString(dish.getPrice()));
        editQuantity.getEditText().setText(Integer.toString(dish.getQuantity()));
        deleteButton.setVisibility(View.VISIBLE);
    }
}
