package com.example.restaurantapp.DataModel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Restaurant implements Serializable {
    private String name;
    private String mail;
    private String description;
    private String restaurantAddress;
    private String encodedImage;

    //singleton class
    private static Restaurant istance;
    private Restaurant() {

    }

    public static Restaurant getIstance() {
        if(istance == null){
            istance = new Restaurant();
        }
        return istance;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public static Bitmap decodeImage(String encodedImage) {

        byte[] decodedByte = Base64.decode(encodedImage, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);

    }

    public static String encodeImage(Drawable drawable) {

        Bitmap image = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;

    }
}
