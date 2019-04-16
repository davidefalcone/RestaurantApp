package com.example.restaurantapp;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DailyOfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView dishImage;
    public TextView dishName;
    public TextView dishDescription;
    public TextView dishCost;
    public TextView dishQuantity;
    public Button editButton;
    private WeakReference<DailyOfferAdapter.ClickListener> listenerRef;

    public DailyOfferViewHolder(@NonNull View itemView, DailyOfferAdapter.ClickListener listener) {
        super(itemView);
        listenerRef = new WeakReference<>(listener);
        dishImage = itemView.findViewById(R.id.dishimage);
        dishName = itemView.findViewById(R.id.dishname);
        dishDescription = itemView.findViewById(R.id.dishdescription);
        dishCost = itemView.findViewById(R.id.dishcost);
        dishQuantity = itemView.findViewById(R.id.dishquantity);
        editButton = itemView.findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
    }
    // onClick Listener for view
    @Override
    public void onClick(View v) {

        if (v.getId() == editButton.getId()) {
           // Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }

        listenerRef.get().onEditClicked(getAdapterPosition());
    }

}
