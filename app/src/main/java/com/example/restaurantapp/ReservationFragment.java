package com.example.restaurantapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.restaurantapp.DataModel.ReservationDatabase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReservationFragment extends androidx.fragment.app.Fragment {

    private ListView listView;
    private ReservationAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservations, container, false);

        listView = v.findViewById(R.id.reservationList);

        setListView();

        return v;
    }

    private void setListView() {
        adapter = new ReservationAdapter(getContext());
        adapter.setList(ReservationDatabase.getInstance().getReservations());
        listView.setAdapter(adapter);
    }

}

