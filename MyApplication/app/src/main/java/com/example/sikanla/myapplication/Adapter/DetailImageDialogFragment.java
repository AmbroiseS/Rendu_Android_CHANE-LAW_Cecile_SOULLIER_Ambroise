package com.example.sikanla.myapplication.Adapter;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sikanla.myapplication.R;

/**
 * Created by Sikanla on 23/05/2017.
 */

public class DetailImageDialogFragment extends DialogFragment {
    private View rootView;
    private TextView name, date, localisation, size;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        rootView = inflater.inflate(R.layout.detail_layout, null);

        name = (TextView) rootView.findViewById(R.id.df_name);
        date = (TextView) rootView.findViewById(R.id.df_date);
        localisation = (TextView) rootView.findViewById(R.id.df_position);
        size = (TextView) rootView.findViewById(R.id.df_taille);

        date.setText(getArguments().getString("date"));
        localisation.setText(getArguments().getString("location"));
        size.setText(getArguments().getString("size"));


        final AlertDialog.Builder builder1 = builder.setView(rootView);
        return builder1.create();
    }
}