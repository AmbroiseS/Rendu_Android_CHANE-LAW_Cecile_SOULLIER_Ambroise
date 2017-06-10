package com.example.sikanla.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sikanla.myapplication.Adapter.ImageDisplayAdapter;
import com.example.sikanla.myapplication.Adapter.ModelImage;
import com.example.sikanla.myapplication.FirebaseServices.NotificationClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private UploadTask uploadTask;
    private StorageReference storageRef;
    private DatabaseReference refDatabase, refTokens;
    private FirebaseDatabase mDB;

    private ArrayList<String> arrayListTokens;

    private ListView listView;
    private ImageDisplayAdapter imageDisplayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayListTokens = new ArrayList<>();
        storageRef = FirebaseStorage.getInstance().getReference();
        mDB = FirebaseDatabase.getInstance();
        refDatabase = mDB.getReference("urlPictures");
        refTokens = mDB.getReference("androidTokens");

        instantiateFAB();

        //upload android tokens to firebase database
        saveAndroidTokentoDB();

        //get other users tokens
        pullTokens();

        instantiateAdapter();

        // get pictures url from firebase database
        pullPictures();


    }

    private ChildEventListener pullTokens() {
        return refTokens.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SharedPreferences prefs = getSharedPreferences("id", MODE_PRIVATE);

                //only add tokens from other devices
                //should be able to manage multiple devices
                //comment next line for receiving notif on your own device
                if (prefs.getString("APIKEY", "1") != dataSnapshot.getValue().toString())
                    arrayListTokens.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {    }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {        }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {     }

            @Override
            public void onCancelled(DatabaseError databaseError) {       }
        });
    }



    private void pullPictures() {
        refDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //get all images urls stored in the cloud
                imageDisplayAdapter.add(new ModelImage(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


    private void saveAndroidTokentoDB() {
        SharedPreferences prefs = getSharedPreferences("id", MODE_PRIVATE);
        Log.e("ee", prefs.getString("APIKEY", "1"));
        if (prefs.getString("APIKEY", "1") != "1")
            refTokens.child(prefs.getString("APIKEY", "1")).setValue(prefs.getString("APIKEY", " "));
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            sendMethod(imageBitmap);
        }
    }


    private void sendMethod(Bitmap bitmap) {
        //give random name to storage
        final String f = getRandomString(10);
        final StorageReference imageRef = storageRef.child("images/" + f + ".jpeg");

    //exif doesnt work here either
        File path = getFilesDir();
        File file = new File(path, "temp.jpg");

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

            fOut.flush();
            fOut.close();
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            Log.e("ff", String.valueOf(file.exists()));
            Log.e("ff", String.valueOf(file.getAbsolutePath()));
            Log.e("ff", String.valueOf(file.length()));
            Log.e("tag", "e" + exifInterface.getAttribute(ExifInterface.TAG_DATETIME));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //end of exif try


        //compress and send
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();
        uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();

                //Save picture url to database
                refDatabase.child(f).setValue(downloadUrl.toString());
                //send notif to all users but us
                NotificationClass notificationClass = new NotificationClass();
                notificationClass.sendMessage(new JSONArray(arrayListTokens), "Nouvelle Image", "nouvelle image postée", "ff", "Cliquer pour voir l'image");

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();

                    }
                });
    }


    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void instantiateFAB() {
        floatingActionButton = new FloatingActionButton(this);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void instantiateAdapter() {
        listView = (ListView) findViewById(R.id.listView);
        ArrayList<ModelImage> arrayList = new ArrayList<>();
        imageDisplayAdapter = new ImageDisplayAdapter(this, arrayList);
        listView.setAdapter(imageDisplayAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                } else {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
