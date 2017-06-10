package com.example.sikanla.myapplication.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sikanla.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.media.ExifInterface.TAG_DATETIME;
import static android.media.ExifInterface.TAG_GPS_LATITUDE;
import static android.media.ExifInterface.TAG_GPS_LONGITUDE;

/**
 * Created by Sikanla on 22/05/2017.
 */


public class ImageDisplayAdapter extends ArrayAdapter<ModelImage> {

    private Activity context;


    private static class ViewHolder {
        LinearLayout linearLayout;
        ImageView pictureDisplay;
    }

    public ImageDisplayAdapter(Activity context, ArrayList<ModelImage> modelImages) {
        super(context, R.layout.item_picture, modelImages);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ModelImage modelImage = getItem(position);

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_picture, parent, false);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear);
            viewHolder.pictureDisplay = (ImageView) convertView.findViewById(R.id.pictureContact);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(modelImage.urlPic).into(viewHolder.pictureDisplay);

    //click on item will display dialog fragment with infos(or no infos rather)
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.pictureDisplay.buildDrawingCache();
                Log.e("zoo",String.valueOf(viewHolder.pictureDisplay.getDrawingCache()==null));
                if (viewHolder.pictureDisplay.getDrawingCache() != null) {
                    Bitmap bitmap = viewHolder.pictureDisplay.getDrawingCache();

                    try {
    //doesnt work, exif is alwais null
    // could have used a gps class, but wasnt the right way to get the Gps coordinates
                        File path = context.getFilesDir();
                        File file = new File(path,"temp.jpg");

                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());

                        displayDialogFragment(file, exifInterface);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        return convertView;
    }

    private void displayDialogFragment(File file, ExifInterface exifInterface) {
        DetailImageDialogFragment detailImageDialogFragment = new DetailImageDialogFragment();

        Bundle args = new Bundle();
        args.putString("location", exifInterface.getAttribute(TAG_GPS_LATITUDE) + ", "
                + exifInterface.getAttribute(TAG_GPS_LONGITUDE));
        args.putString("date", exifInterface.getAttribute(TAG_DATETIME));
        args.putString("size",String.valueOf((file.length()/1024)));
        detailImageDialogFragment.setArguments(args);
        detailImageDialogFragment.show(context.getFragmentManager(), "pom");
    }
}
