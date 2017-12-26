package com.example.morgane.eece451_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class Piconet extends AppCompatActivity {
    IBDevice device;
    CountDownTimer timer;

    int channel;
    Button sendFile, receiveFile, sendMessage, receiveMessage;
    DatabaseReference mDatabaseRef1, mDatabaseRef2, mDatabaseRef3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piconet);

        sendMessage = (Button) findViewById(R.id.sendMessage);
        receiveMessage = (Button) findViewById(R.id.receiveMessage);
        sendFile = (Button) findViewById(R.id.sendFile);
        receiveFile = (Button) findViewById(R.id.receiveFile);

        sendMessage.setBackgroundColor(Color.RED);
        receiveMessage.setBackgroundColor(Color.RED);
        receiveFile.setBackgroundColor(Color.RED);
        receiveFile.setBackgroundColor(Color.RED);

        mDatabaseRef1 = getInstance().getReference("Channel1");
        mDatabaseRef2 = getInstance().getReference("Channel2");
        mDatabaseRef3 = getInstance().getReference("Channel3");

        Intent intent = getIntent();
        device = (IBDevice) intent.getSerializableExtra("device");
        channel = intent.getIntExtra("channel", -1);


        if (channel == 1) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(Piconet.this);
            builder.setCancelable(true);

            if (device.GetIfMaster() && (device.GetChannel() == 1)) {
                builder.setMessage("Connected to channel 1 and is master.");

                new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        mDatabaseRef1.child("Available").setValue(false);
                    }
                }.start();
            } else if (!device.GetIfMaster() && (device.GetChannel() == 1))
                builder.setMessage("Connected to channel 1.");

            builder.show();
        }

        else if (channel == 2) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(Piconet.this);
                builder.setCancelable(true);

                if (device.GetIfMaster() && (device.GetChannel() == 2)) {
                    builder.setMessage("Connected to channel 2 and is master.");

                    new CountDownTimer(30000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            mDatabaseRef2.child("Available").setValue(false);
                        }
                    }.start();
                }
                else if (!device.GetIfMaster() && (device.GetChannel() == 2))
                    builder.setMessage("Connected to channel 2.");

                builder.show();
        }

        else if (channel == 3) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(Piconet.this);
            builder.setCancelable(true);

            if (device.GetIfMaster() && (device.GetChannel() == 3)) {
                builder.setMessage("Connected to channel 3 and is master.");

                new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        mDatabaseRef3.child("Available").setValue(false);
                    }
                }.start();
            } else if (!device.GetIfMaster() && (device.GetChannel() == 3))
                builder.setMessage("Connected to channel 3.");

            builder.show();
        }

        // counter to change button colors
//        final Boolean[] yes = {true};
//        new CountDownTimer(300000, 2000) {
//            @Override
//            public void onTick(long l) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                yes[0] =false;
//
//            }
//        }.start();

        //Buttons initialized to red and inactive;
//        while(yes[0])
//        {
        final Boolean[] foo = {true};
        timer =
            new CountDownTimer(300000, 3000) {
                @Override
                public void onTick(long l) {
                    if (foo[0])
                        Activate();
                    else
                        Deactivate();
                    foo[0] = !foo[0];
                }

                @Override
                public void onFinish() {

                }
            };
        timer.start();

//        }

        mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.getChildrenCount() == 1) && !(device.isMaster)) {
                    if (!dataSnapshot.child("0").exists()) {
                        timer.cancel();
                        Deactivate();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mDatabaseRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.getChildrenCount() == 1) && !(device.isMaster)) {
                    if (!dataSnapshot.child("0").exists()) {
                        timer.cancel();
                        Deactivate();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mDatabaseRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.getChildrenCount() == 1) && !(device.isMaster)) {
                    if (!dataSnapshot.child("0").exists()) {
                        timer.cancel();
                        Deactivate();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void Disconnect(View view) {
        if (device.isMaster)
            tearPiconet();

        else {
            if (channel == 1) {
                mDatabaseRef1.child(device.Key).removeValue();
                mDatabaseRef1.getParent().child("uploads").child("Users1").child(device.Username).removeValue();
            }
            else if (channel == 2) {
                mDatabaseRef2.child(device.Key).removeValue();
                mDatabaseRef1.getParent().child("uploads").child("Users2").child(device.Username).removeValue();
            }
            else if (channel == 3) {
                mDatabaseRef3.child(device.Key).removeValue();
                mDatabaseRef1.getParent().child("uploads").child("Users3").child(device.Username).removeValue();
            }
        }
        timer.cancel();
        Deactivate();
    }

    void tearPiconet() {
        if (channel == 1) {
            mDatabaseRef1.setValue("temp");
            mDatabaseRef1.getParent().child("uploads").child("Users1").setValue("temp");
            mDatabaseRef1.child("Available").setValue(true);
        }

        else if (channel == 2) {
            mDatabaseRef2.setValue("temp");
            mDatabaseRef1.getParent().child("uploads").child("Users2").setValue("temp");
            mDatabaseRef2.child("Available").setValue(true);
        }

        else if (channel == 3) {
            mDatabaseRef3.setValue("temp");
            mDatabaseRef1.getParent().child("uploads").child("Users3").setValue("temp");
            mDatabaseRef3.child("Available").setValue(true);
        }
        timer.cancel();
        Deactivate();
    }

    public void ReceiveMessage(View view) {
        Intent TextActivity = new Intent(Piconet.this, TxtListActivity.class);
        TextActivity.putExtra("username", device.Username);
        startActivity(TextActivity);
    }

    public void ReceiveFile(View view) {
        Intent downloadActivity = new Intent(Piconet.this, ViewDownloadActivity.class);
        downloadActivity.putExtra("username", device.Username);
        startActivity(downloadActivity);
    }

    public void SendFile(View view) {
        Intent mainFileActivity = new Intent(Piconet.this, MainFileActivity.class);
        mainFileActivity.putExtra("username", device.Username);
        mainFileActivity.putExtra("channel", device.GetChannel());
        startActivity(mainFileActivity);
    }

    public void SendMessage(View view) {
        Intent mainActivityText = new Intent(Piconet.this, MainActivityText.class);
        mainActivityText.putExtra("username", device.Username);
        mainActivityText.putExtra("channel", device.GetChannel());
        startActivity(mainActivityText);
    }

    public void Deactivate() {
      //Set buttons to red + can't be pressed

        sendMessage.setClickable(false);
        receiveMessage.setClickable(false);
        sendFile.setClickable(false);
        receiveFile.setClickable(false);

        sendMessage.setBackgroundColor(Color.RED);
        receiveMessage.setBackgroundColor(Color.RED);
        sendFile.setBackgroundColor(Color.RED);
        receiveFile.setBackgroundColor(Color.RED);
    }

    public void Activate() {
        //Set buttons to green + can be pressed

        sendMessage.setClickable(true);
        receiveMessage.setClickable(true);
        sendFile.setClickable(true);
        receiveFile.setClickable(true);

        sendMessage.setBackgroundColor(Color.GREEN);
        receiveMessage.setBackgroundColor(Color.GREEN);
        sendFile.setBackgroundColor(Color.GREEN);
        receiveFile.setBackgroundColor(Color.GREEN);

    }
}
