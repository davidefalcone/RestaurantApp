package com.example.restaurantapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.restaurantapp.DataModel.Database;
import com.example.restaurantapp.DataModel.Dish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class DailyOfferFragment extends androidx.fragment.app.Fragment {

    private DailyOfferRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DailyOfferAdapter adapter;
    private FloatingActionButton fab;
    private Database dishDatabase;

    private String tagDishes;
    public static final int EDIT_DISH = 0;
    public static final int ADD_DISH = 1;
    public static final int RESULT_DELETED = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_daily_offer, container, false);

        tagDishes = EditDishActivity.tagDishes;

        recyclerView = v.findViewById(R.id.recyclerView);
        fab = v.findViewById(R.id.floating_action_button);

        dishDatabase = getDB();

        setRecyclerView(v);

        setFAB();

        return v;
    }
    //this method creates a new LinearLayoutManager and sets it in the recyclerView.
    //Then, an adapter is assigned to the recyclerView.
    private void setRecyclerView(View v) {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //changing in content do not change the layout size of the RecyclerView (from developer.android)
        recyclerView.setHasFixedSize(true);
        recyclerView.setEmptyView(v.findViewById(R.id.no_dishes_view));
        adapter = new DailyOfferAdapter(dishDatabase, getContext(), new DailyOfferAdapter.ClickListener() {
            @Override
            public void onEditClicked(int position) {
                Intent i = new Intent(getContext(), EditDishActivity.class);
                i.putExtra(tagDishes, adapter.getItem(position));
                startActivityForResult(i, EDIT_DISH);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    private void setFAB(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getContext(), EditDishActivity.class);
                startActivityForResult(i,ADD_DISH);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Dish dish;
        switch (requestCode){
            case EDIT_DISH:
                switch (resultCode){
                    case RESULT_OK:
                        dish = (Dish)data.getSerializableExtra(tagDishes);
                        dishDatabase.addDish(dish);
                        adapter.setList(dishDatabase.getDishList());
                        adapter.notifyDataSetChanged();
                        commitDB();
                        return;
                    case RESULT_DELETED:
                        dish = (Dish)data.getSerializableExtra(tagDishes);
                        dishDatabase.removeDish(dish.getId());
                        adapter.setList(dishDatabase.getDishList());
                        adapter.notifyDataSetChanged();
                        commitDB();
                        return;
                    case RESULT_CANCELED:
                        return;
                }
            case ADD_DISH:
                switch (resultCode){
                    case RESULT_OK:
                        dish = (Dish)data.getSerializableExtra(tagDishes);
                        dishDatabase.addDish(dish);
                        adapter.setList(dishDatabase.getDishList());
                        adapter.notifyDataSetChanged();
                        commitDB();
                    case RESULT_CANCELED:
                        return;
                }
        }
    }

    private void commitDB() {
        SharedPreferences preferences = getActivity().getSharedPreferences(tagDishes, MODE_PRIVATE);

        String encodedDB;
        Gson gson = new Gson();
        encodedDB = gson.toJson(dishDatabase);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(tagDishes,encodedDB);
        editor.commit();
    }

    private Database getDB(){
        Database dishDatabase;
        SharedPreferences preferences = getContext().getSharedPreferences(tagDishes, MODE_PRIVATE);

        Gson gson = new Gson();
        if(preferences.getString(tagDishes,"first time")!= "first time") {
            dishDatabase = gson.fromJson(preferences.getString(tagDishes, ""), Database.class);
        }
        else dishDatabase = Database.getInstance();
        return dishDatabase;
    }

}
