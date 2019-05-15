package com.example.restaurantapp.DataModel;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.restaurantapp.DailyOfferAdapter;
import com.example.restaurantapp.OrdersAdapter;
import com.example.restaurantapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class Database {
    private static Database instance;
    private DatabaseReference path;
    private FirebaseUser user;
    private ArrayList<Order> ordersList;
    private ArrayList<Dish> dishList;

    private Database() {
        ordersList = new ArrayList<>();
        dishList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        path = FirebaseDatabase.getInstance().getReference();
    }

    public static Database getInstance() {
        if(instance == null) instance = new Database();
        return instance;
    }

    public ArrayList<Dish> getDishList() {
        return dishList;
    }

    public void addDish(Dish dish) {
        dish.setRestaurantID(user.getUid());
        dish.setID(path.child("restaurants").child(user.getUid()).child("dishes").push().getKey());
        path.child("restaurants").child(user.getUid()).child("dishes").child(dish.getID()).setValue(dish);
    }

    public void removeDish(Dish dish) {
        path.child("restaurants").child(user.getUid()).child("dishes").child(dish.getID()).removeValue();
    }

    public void writeRestaurant(Restaurant restaurant) {
        restaurant.setID(user.getUid());
        restaurant.setMail(user.getEmail());
        path.child("restaurants").child(user.getUid()).child("account").setValue(restaurant);
    }

    public void fillDishDatabase(DailyOfferAdapter adapter) {
        path.child("restaurants").child(user.getUid()).child("dishes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dishList.clear();
                for (DataSnapshot dish : dataSnapshot.getChildren()){
                    dishList.add(dish.getValue(Dish.class));
                }
                adapter.setList(dishList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fillOrdersDatabase(OrdersAdapter adapter) {
        path.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for (DataSnapshot o : dataSnapshot.getChildren()) {
                    Order order = o.getValue(Order.class);
                    if (TextUtils.equals(order.getRestaurant().getID(), user.getUid()))
                        ordersList.add(order);
                }
                adapter.setList(ordersList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateOrder(Order order) {
        path.child("deliverymen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (order.getStatus() != Order.CANCELLED) {
                    long childrenCount = dataSnapshot.getChildrenCount();
                    int count = (int) childrenCount;
                    int randomNumber = new Random().nextInt(count);
                    int i = 0;
                    for (DataSnapshot deliveryMan : dataSnapshot.getChildren()) {
                        if (i == randomNumber){
                            order.setDeliveryMan(deliveryMan.child("account").getValue(DeliveryMan.class));
                            break;
                        }
                        i++;
                    }
                }
                path.child("orders").child(order.getID()).setValue(order);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
