package com.example.morgane.eece451_project;

import android.content.Context;
import android.os.BadParcelableException;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class IBDevice implements Serializable {
    // Member variables of device
     boolean isMaster;
     int isConnectedToChannel;
     String Username = "";
     String Key;
     DatabaseReference mDatabaseRef;
    public String MAC_Address = "";
    public List<IBDevice> slaves = new ArrayList<IBDevice>(); //////////////initialize ??????????

    // constructor
    public IBDevice(String user, Boolean isMaster, int isConnectedToChannel, int Key) {
        Username = user;
        this.isMaster = isMaster;
        this.isConnectedToChannel = isConnectedToChannel;
        this.Key = String.valueOf(Key);
    }

    public Boolean GetIfMaster() {
        return isMaster;
    }

    public int GetChannel() {
        return isConnectedToChannel;
    }


//    public void Disconnect() {
//        if (isMaster)
//            tearDown();
//
//        else {
//            mDatabaseRef.child(String.valueOf(Key)).removeValue();
//            isConnectedToChannel = 0;
//            Key = -1;
//        }
//    }
//
//    public void tearDown() {
//        mDatabaseRef.removeValue();
//        mDatabaseRef.child("Available").setValue(true);
//    }

}