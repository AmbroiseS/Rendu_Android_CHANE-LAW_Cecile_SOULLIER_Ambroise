package com.example.sikanla.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sikanla on 22/05/2017.
 */


public class ImageDisplayAdapter extends ArrayAdapter<ModelImage> {
    // View lookup cache
    private Context context;

    private static class ViewHolder {

        ImageView pictureDisplay;
    }

    public ImageDisplayAdapter(Context context, ArrayList<ModelImage> modelImages) {
        super(context, R.layout.item_picture, modelImages);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ModelImage modelImage = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_picture, parent, false);
            viewHolder.pictureDisplay = (ImageView) convertView.findViewById(R.id.pictureContact);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        Picasso.with(context).load(modelImage.urlPic).into(viewHolder.pictureDisplay);


        // Return the completed view to render on screen
        return convertView;
    }
}