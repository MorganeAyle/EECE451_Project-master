package com.example.morgane.eece451_project;

import android.content.Context;
        import android.content.Intent;
        import android.os.CountDownTimer;
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
        import com.google.firebase.database.FirebaseDatabase;
        import static com.google.firebase.database.FirebaseDatabase.*;
        import java.util.ArrayList;
        import java.util.List;

public class Files extends AppCompatActivity {
    String username;
    Context context = this;
    DatabaseReference mDatabaseRef1;
    DatabaseReference mDatabaseRef2;
    DatabaseReference mDatabaseRef3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        // get the device object
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        mDatabaseRef1 = getInstance().getReference("Channel1");
        mDatabaseRef2 = getInstance().getReference("Channel2");
        mDatabaseRef3 = getInstance().getReference("Channel3");
    }


    public void TryConnectChannel1(View view) {

        mDatabaseRef1.child("temp").setValue(username);

    }

    public void TryConnectChannel2(View view) {

        mDatabaseRef2.child("temp").setValue(username);
    }

    public void TryConnectChannel3(View view) {
        mDatabaseRef3.child("temp").setValue(username);

    }
}