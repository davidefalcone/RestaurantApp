package com.example.restaurantapp;


import android.app.Dialog;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.restaurantapp.DataModel.Database;
import com.example.restaurantapp.DataModel.Order;

public class OrderListFragment extends androidx.fragment.app.Fragment {
    private ListView listView;
    private OrdersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservations, container, false);

        listView = v.findViewById(R.id.reservationList);

        setListView();

        return v;
    }

    private void setListView() {
        adapter = new OrdersAdapter(getContext());
        Database.getInstance().fillOrdersDatabase(adapter);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = adapter.getItem(position);
                if (order.getStatus() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setPositiveButton(R.string.acceptorder, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            order.setStatus(order.getStatus() + 1);
                            Database.getInstance().updateOrder(order);
                            adapter.notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton(R.string.declineorder, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            order.setStatus(Order.CANCELLED);
                            Database.getInstance().updateOrder(order);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setTitle(R.string.orderdialogtitle);
                    builder.setMessage(R.string.orderdialogtext);

                    builder.create().show();

                }
            }
        });
    }

}

