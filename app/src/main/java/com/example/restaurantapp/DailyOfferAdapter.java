package com.example.restaurantapp;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurantapp.DataModel.DishDatabase;
import com.example.restaurantapp.DataModel.Dish;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class DailyOfferAdapter extends RecyclerView.Adapter<DailyOfferViewHolder> {
    private DishDatabase dishDatabase;
    private ArrayList<Dish> list;
    private LayoutInflater inflater;
    private Context context;
    private final ClickListener listener;
    public interface ClickListener {

        void onEditClicked(int position);
    }

    public DailyOfferAdapter(DishDatabase dishDatabase, Context context, ClickListener listener){
        this.dishDatabase = dishDatabase;
        list = dishDatabase.getDishesList();
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public DailyOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dish_item, parent, false);
        DailyOfferViewHolder dailyOfferViewHolder = new DailyOfferViewHolder(view, listener);
        return dailyOfferViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyOfferViewHolder holder, int position) {
        holder.dishImage.setImageBitmap(Dish.decodeImage(list.get(position).getEncodedImage()));
        holder.dishName.setText(list.get(position).getName());
        holder.dishDescription.setText(list.get(position).getDescription());
        holder.dishCost.setText(Integer.toString(list.get(position).getPrice()) + "€");
        holder.dishQuantity.setText(Integer.toString(list.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setList(ArrayList<Dish> list) {
        this.list = list;
    }
    //this method receives a position in the list and return the corresponding dish
    public Dish getItem(int position){
        return list.get(position);
    }

    public Context getContext() {
        return context;
    }
}
