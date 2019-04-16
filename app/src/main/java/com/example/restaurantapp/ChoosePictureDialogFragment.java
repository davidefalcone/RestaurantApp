package com.example.restaurantapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ChoosePictureDialogFragment extends DialogFragment {

    public interface onInputListener{
        void setInput(int input);
    }

    public onInputListener onInputListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.myDialog);
        builder.setTitle(R.string.dialogTitle);
        builder.setView(R.layout.picture_dialog);
        builder.setAdapter(new GalleriaCameraAdapter(this.getContext()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) onInputListener.setInput(0);
                else onInputListener.setInput(1);
            }
        });

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onInputListener = (onInputListener) getActivity();
        }
        catch (ClassCastException e){
            Log.e("","onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
