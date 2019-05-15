package com.example.restaurantapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.restaurantapp.DataModel.Database;
import com.example.restaurantapp.DataModel.Dish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DailyOfferAdapter extends RecyclerView.Adapter<DailyOfferViewHolder> {
    private ArrayList<Dish> list;
    private LayoutInflater inflater;
    private Context context;
    private FirebaseUser user;
    private StorageReference path;
    private final ClickListener listener;
    public interface ClickListener {

        void onEditClicked(int position);
    }

    public DailyOfferAdapter(Context context, ClickListener listener){
        list = Database.getInstance().getDishList();
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        user = FirebaseAuth.getInstance().getCurrentUser();
        path = FirebaseStorage.getInstance().getReference().child("restaurants")
                .child(user.getUid());
        notifyDataSetChanged();
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
        holder.dishName.setText(list.get(position).getName());
        holder.dishDescription.setText(list.get(position).getDescription());
        holder.dishCost.setText(Integer.toString(list.get(position).getPrice()) + "€");
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(path.child(list.get(position).getName())).
                addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(holder.dishImage);
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
class DailyOfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView dishImage;
    public TextView dishName;
    public TextView dishDescription;
    public TextView dishCost;
    public Button editButton;
    public ProgressBar progressBar;
    private WeakReference<DailyOfferAdapter.ClickListener> listenerRef;

    public DailyOfferViewHolder(@NonNull View itemView, DailyOfferAdapter.ClickListener listener) {
        super(itemView);
        listenerRef = new WeakReference<>(listener);
        dishImage = itemView.findViewById(R.id.dishimage);
        dishName = itemView.findViewById(R.id.dishname);
        dishDescription = itemView.findViewById(R.id.dishdescription);
        dishCost = itemView.findViewById(R.id.dishcost);
        editButton = itemView.findViewById(R.id.editButton);
        progressBar = itemView.findViewById(R.id.progress_bar);
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
