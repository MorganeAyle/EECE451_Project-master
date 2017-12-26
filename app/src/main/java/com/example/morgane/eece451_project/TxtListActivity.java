package com.example.morgane.eece451_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class TxtListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private List<Upload> txtList;
    private ListView lv;
    private TxtListAdapter adapter;
    private ProgressDialog progressDialog;
    private String receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_list2);
        Intent intent = getIntent();

        receiver = intent.getStringExtra("username");

        txtList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewImage);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(MainActivityText.FB_DATABASE_PATH);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("i","test2");

                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equals("temp")) {
                        //ImageUpload class require default constructor
                        Upload img = snapshot.getValue(Upload.class);
                        if (img.getreceiver().equals(receiver))
                            txtList.add(img);
                        Log.d("i", "test3");
                    }
                }


                //Init adapter
                adapter = new TxtListAdapter(TxtListActivity.this, R.layout.txtview, txtList);
                //Set adapter for listview
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }
}
