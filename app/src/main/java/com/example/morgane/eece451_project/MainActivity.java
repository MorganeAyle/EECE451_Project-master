package com.example.morgane.eece451_project;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class MainActivity extends AppCompatActivity {
    EditText username;
    String user = "";
    Context context = this;
    IBDevice device;
    DatabaseReference mDatabaseRef1, mDatabaseRef2, mDatabaseRef3;
    Master master;

    private int size1= -1;
    private int size2= -1;
    private int size3= -1;
    private Boolean isAvailable1 = false;
    private Boolean isAvailable2 = false;
    private Boolean isAvailable3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);

        master = new Master();

        // get instance of database

        mDatabaseRef1 = getInstance().getReference("Channel1");
        mDatabaseRef2 = getInstance().getReference("Channel2");
        mDatabaseRef3 = getInstance().getReference("Channel3");

        mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("temp").exists()) {
                    dataSnapshot.getRef().getParent().child("uploads").child("Users1").child(user).setValue(user);
                }

                if (dataSnapshot.child("temp").exists() && dataSnapshot.child("temp").getValue().equals(user) ) {

                    size1 = (int) dataSnapshot.getChildrenCount();
                    mDatabaseRef1.child("temp").removeValue();
                    isAvailable1 = (Boolean) dataSnapshot.child("Available").getValue();

                    size1-=2;

                    if ((size1 == 0) && isAvailable1) {
                        device = new IBDevice(user, true, 1, size1);

                        mDatabaseRef1.child(String.valueOf(size1)).setValue(user);

                        Log.d("result", "is the master of channel " + device.GetChannel());

                        Intent piconetPage = new Intent(MainActivity.this, Piconet.class);
                        piconetPage.putExtra("device", (Serializable) device);
                        piconetPage.putExtra("channel", 1);
                        startActivity(piconetPage);
                    }
                    else if ((size1 != 0) && isAvailable1) {
                        device = new IBDevice(user, false, 1, size1);
                        mDatabaseRef1.child(String.valueOf(size1)).setValue(user);

                        Log.d("result", "connected to channel 1");

                        Intent piconetPage = new Intent(MainActivity.this, Piconet.class);
                        piconetPage.putExtra("device", (Serializable) device);
                        piconetPage.putExtra("channel", 1);
                        startActivity(piconetPage);
                    }
                    else if (!isAvailable1) {
                        Log.d("result", "channel1 not available");

                        Intent exception = new Intent(MainActivity.this, Unavailable.class);
                        exception.putExtra("channel", 1);
                        startActivity(exception);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mDatabaseRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("temp").exists()) {
                    dataSnapshot.getRef().getParent().child("uploads").child("Users2").child(user).setValue(user);
                }

                if (dataSnapshot.child("temp").exists()&& dataSnapshot.child("temp").getValue().equals(user) ) {

                    size2 = (int) dataSnapshot.getChildrenCount();
                    mDatabaseRef2.child("temp").removeValue();
                    isAvailable2 = (Boolean) dataSnapshot.child("Available").getValue();

                    size2 -= 2;

                    if ((size2 == 0) && isAvailable2) {
                        device = new IBDevice(user, true, 2, size2);

                        mDatabaseRef2.child(String.valueOf(size2)).setValue(user);

                        Log.d("result", "is the master of channel " + device.GetChannel());

                        Intent piconetPage = new Intent(MainActivity.this, Piconet.class);
                        piconetPage.putExtra("device", (Serializable) device);
                        piconetPage.putExtra("channel", 2);
                        startActivity(piconetPage);
                    }
                    else if ((size2 != 0) && isAvailable2) {
                        device = new IBDevice(user, false, 2, size2);
                        mDatabaseRef2.child(String.valueOf(size2)).setValue(user);

                        Log.d("result", "connected to channel 2");

                        Intent piconetPage = new Intent(MainActivity.this, Piconet.class);
                        piconetPage.putExtra("device", (Serializable) device);
                        piconetPage.putExtra("channel", 2);
                        startActivity(piconetPage);
                    }
                    else if (!isAvailable2) {
                        Log.d("result", "channel2 not available");

                        Intent exception = new Intent(MainActivity.this, Unavailable.class);
                        exception.putExtra("channel", 2);
                        startActivity(exception);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mDatabaseRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("temp").exists()) {
                    dataSnapshot.getRef().getParent().child("uploads").child("Users3").child(user).setValue(user);
                }

                if (dataSnapshot.child("temp").exists()&& dataSnapshot.child("temp").getValue().equals(user) ) {

                    size3 = (int) dataSnapshot.getChildrenCount();
                    mDatabaseRef3.child("temp").removeValue();
                    isAvailable3 = (Boolean) dataSnapshot.child("Available").getValue();

                    size3-=2;

                    if ((size3 == 0) && isAvailable3) {
                        device = new IBDevice(user, true, 3, size3);

                        mDatabaseRef3.child(String.valueOf(size3)).setValue(user);

                        Log.d("result", "is the master of channel " + device.GetChannel());

                        Intent piconetPage = new Intent(MainActivity.this, Piconet.class);
                        piconetPage.putExtra("device", (Serializable) device);
                        piconetPage.putExtra("channel", 3);
                        startActivity(piconetPage);
                    }
                    else if ((size3 != 0) && isAvailable3) {
                        device = new IBDevice(user, false, 3, size3);
                        mDatabaseRef3.child(String.valueOf(size3)).setValue(user);

                        Log.d("result", "connected to channel 3");

                        Intent piconetPage = new Intent(MainActivity.this, Piconet.class);
                        piconetPage.putExtra("device", (Serializable) device);
                        piconetPage.putExtra("channel", 3);
                        startActivity(piconetPage);
                    }
                    else if (!isAvailable3) {
                        Log.d("result", "channel3 not available");

                        Intent exception = new Intent(MainActivity.this, Unavailable.class);
                        exception.putExtra("channel", 3);
                        startActivity(exception);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    public void ConfirmUsername(View view) {
        user = username.getText().toString();

        if (user.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please enter your username.");
            builder.setCancelable(true);
            builder.show();
        }

        else {
            Intent welcomeWindow = new Intent(MainActivity.this, WelcomePage.class);
            welcomeWindow.putExtra("username", user);
            startActivity(welcomeWindow);
        }
    }
}