package com.example.restaurantapp.DataModel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import androidx.annotation.NonNull;

public class Dish implements Serializable {

    //attributes
    private String name;
    private String description;
    private int price;
    private int quantity;
    private String encodedImage;
    private int id;
    private static int nInstance = 0;

    //constructor
    public Dish(String name, String description, int price, int quantity, String encodedImage) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.encodedImage = encodedImage;
        this.id = nInstance;
        nInstance++;
    }

    public Dish() {
        this.id = nInstance;
        nInstance++;
    }

    //getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public int getId() {
        return id;
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
