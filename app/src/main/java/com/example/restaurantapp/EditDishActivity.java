package com.example.restaurantapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.restaurantapp.DataModel.Dish;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditDishActivity extends AppCompatActivity implements ChoosePictureDialogFragment.onInputListener{
    private FirebaseUser user;
    private StorageReference path;
    //view references
    private ImageView editImage;
    private TextInputLayout editName;
    private TextInputLayout editDescription;
    private TextInputLayout editPrice;
    private FloatingActionButton fab;
    private Button deleteButton;

    public static final String tagDialog = "dialog";
    public static final String tagDishes = "dishes";
    public static final int GALLERY = 0;
    public static final int CAMERA  = 1;
    private final int GAPS_UNFILLED = 0;
    private final int GAPS_COUNT_UNCORRECT = 1;

    private boolean imageViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        user = FirebaseAuth.getInstance().getCurrentUser();
        path = FirebaseStorage.getInstance().getReference()
                .child("restaurants").child(user.getUid());

        //getting view references
        editImage = findViewById(R.id.dishImage);
        editName = findViewById(R.id.editDish);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        fab = findViewById(R.id.floating_action_button);
        deleteButton = findViewById(R.id.deleteButton);

        setImageView();

        setFAB();

        setButton();

        if(getDish() != null) setGaps(getDish());
    }

    //this method sets an image in the imageview and
    //adds a click listener
    private void setImageView() {
        imageViewEmpty = true;
        editImage.setImageResource(R.drawable.editimage);
        editImage.setOnClickListener(new View.OnClickListener() {
            //if the user clicks on the image, an alert dialog appears.
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

    //setting delete button
    private void setButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                setResult(DailyOfferFragment.RESULT_DELETED, i);
                deleteImage(readDish());
                i.putExtra(tagDishes, readDish());

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
                if(gapsCorrect()) returnToDailyOfferActivity(readDish());
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

        if (imageViewEmpty == true)
            result = false;

        return result;

    }

    private void showSnackbar(int requestCode) {
        switch (requestCode){
            case GAPS_UNFILLED:
                Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageMissingMultipleData, Snackbar.LENGTH_SHORT).show();
                return;
            case GAPS_COUNT_UNCORRECT:
                Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageGapsCountIncorrect, Snackbar.LENGTH_SHORT).show();
                return;
        }
    }

    private Dish readDish() {
        String name = editName.getEditText().getText().toString();
        String description = editDescription.getEditText().getText().toString();
        int price = Integer.parseInt(editPrice.getEditText().getText().toString());

        Dish dish;
        if(getIntent().getSerializableExtra(tagDishes) == null) dish  = new Dish();
        else dish = (Dish) getIntent().getSerializableExtra(tagDishes);

        dish.setName(name);
        dish.setDescription(description);
        dish.setPrice(price);

        return dish;
    }

    private void returnToDailyOfferActivity(Dish dish) {
        Intent i = getIntent();
        setResult(RESULT_OK, i);
        i.putExtra(tagDishes, dish);
        uploadImage(dish);
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
        imageViewEmpty = false;
        editName.getEditText().setText(dish.getName());
        editDescription.getEditText().setText(dish.getDescription());
        editPrice.getEditText().setText(Integer.toString(dish.getPrice()));
//        editQuantity.getEditText().setText(Integer.toString(dish.getQuantity()));
        deleteButton.setVisibility(View.VISIBLE);
        Glide.with(this).load(path.child(dish.getName())).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(editImage);
    }

    private boolean gapsCorrect(){
        if(gapsFilled()){
            if (gapsCorrectCount()) return true;
            else showSnackbar(GAPS_COUNT_UNCORRECT);
        }else showSnackbar(GAPS_UNFILLED);
        return false;
    }

    private boolean gapsCorrectCount(){
        if (editName.getEditText().getText().toString().length() > 30||
                editDescription.getEditText().getText().toString().length() > 120) return false;
        return true;
    }

    private void uploadImage(Dish dish){
        View v = findViewById(R.id.loadingView);
        path.child(dish.getName()).delete();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)editImage.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = path.child(dish.getName()).putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                v.setVisibility(View.GONE);
                finish();
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                v.setVisibility(View.VISIBLE);
            }
        });
        return;
    }

    private void deleteImage(Dish dish) {
        path.child(dish.getName()).delete();
        finish();
    }
}
