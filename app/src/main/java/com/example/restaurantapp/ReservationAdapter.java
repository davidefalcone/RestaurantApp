package com.example.restaurantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.restaurantapp.DataModel.Reservation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReservationAdapter extends BaseAdapter {

    private ArrayList<Reservation> reservations;
    private Context context;

    public ReservationAdapter(Context context){
        reservations = new ArrayList<>();
        this.context = context;
    }
    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Reservation getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reservations.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.reservation_item, null);
        TextView customer = convertView.findViewById(R.id.reservationName);
        TextView date = convertView.findViewById(R.id.reservationDate);
        TextView dishes = convertView.findViewById(R.id.reservationDishes);
        TextView notes = convertView.findViewById(R.id.reservationNotes);

        Reservation reservation = reservations.get(position);
        customer.setText(reservation.getCustomer());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a");
        date.setText("Date: " + sdf.format(reservation.getDate()));
        dishes.setText("Ordered dishes: "+ reservation.getOrderedDishesToString());
        if (reservation.getNotes() != null) notes.setText("Notes: " + reservation.getNotes());
        else notes.setVisibility(View.GONE);

        return convertView;
    }
    public void setList(ArrayList<Reservation> reservations){
        this.reservations = reservations;
    }
}
