package com.example.restaurantapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.restaurantapp.DataModel.Customer;
import com.example.restaurantapp.DataModel.Database;
import com.example.restaurantapp.DataModel.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.restaurantapp.DataModel.Order.ACCEPTED;
import static com.example.restaurantapp.DataModel.Order.CANCELLED;
import static com.example.restaurantapp.DataModel.Order.DELIVERED;
import static com.example.restaurantapp.DataModel.Order.IN_DELIVERY;
import static com.example.restaurantapp.DataModel.Order.RECEIVED;

public class OrdersAdapter extends BaseAdapter {

    private ArrayList<Order> ordersList;
    private Context context;

    public OrdersAdapter(Context context){
        ordersList = new ArrayList<>();
        this.context = context;
    }
    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Order getItem(int position) {
        return ordersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.reservation_item, null);
        TextView status = convertView.findViewById(R.id.orderStatus);

        Order order = ordersList.get(position);

        setName(convertView, order);

        if (order.getNotes() != null) {
            TextView notes = convertView.findViewById(R.id.orderNotes);
            notes.setVisibility(View.VISIBLE);
            notes.setText(order.getNotes());
        }

        switch (order.getStatus()) {
            case RECEIVED:
                status.setText("Status: Received. Tap for accept the order.");
                break;
            case ACCEPTED:
                status.setText("Status: Accepted");
                break;
            case IN_DELIVERY:
                status.setText("Status: In delivery");
                break;
            case DELIVERED:
                status.setText("Status: Delivered");
                break;
            case CANCELLED:
                status.setText("Status: Cancelled");
                break;
        }

        TextView orderedDishes = convertView.findViewById(R.id.orderDishes);
        orderedDishes.setText(order.getOrderedDishesString());

        return convertView;
    }
    public void setList(ArrayList<Order> ordersList){
        this.ordersList = ordersList;
    }

    private void setName(View convertView, Order order) {
        DatabaseReference path = FirebaseDatabase.getInstance().getReference();
        path.child("customers").child(order.getCustomerID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Customer customer  = data.getValue(Customer.class);
                    if (TextUtils.equals(customer.getID(), order.getCustomerID())) {
                        ((TextView) convertView.findViewById(R.id.orderName)).setText(customer.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
