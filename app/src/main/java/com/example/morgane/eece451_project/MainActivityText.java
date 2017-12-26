package com.example.morgane.eece451_project;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivityText extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;

    private EditText txtImageName1x;
    private EditText txtImageName2x;
    private TextView resultText;

    public static final String FB_DATABASE_PATH = "text";

    private String sender;
    private String receiverName;
    private int channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text);

        txtImageName1x = (EditText) findViewById(R.id.txtImageName1);
        txtImageName2x = (EditText) findViewById(R.id.txtImageName2);
        resultText = (TextView) findViewById(R.id.ResultText);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        sender = intent.getStringExtra("username");
        channel = intent.getIntExtra("channel", 0);

    }

    public void btnUpload_Click(View view) {
        receiverName = txtImageName1x.getText().toString();

        String usersPath = "";
        if (channel == 1)
            usersPath = "Channel1";
        else if (channel == 2)
            usersPath = "Channel2";
        else if (channel == 3)
            usersPath = "Channel3";

        Log.d("channel ", String.valueOf(channel));
        final String finalUsersPath = usersPath;
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.child(finalUsersPath).getChildren();
                Boolean found = false;
                for (DataSnapshot s: data) {
                    Log.d("to user", s.getValue().toString());
                    if (s.getValue().equals(receiverName)) {
                        Upload imageUpload = new Upload(sender, txtImageName2x.getText().toString(), receiverName);
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child("text").child(uploadId).setValue(imageUpload);
                        found = true;
                        break;
                    }
                    else {

                    }
                }

                if (found) {
                    resultText.setText("Message sent successfully.");
                }
                else {
                    resultText.setText("This user is not connected to your channel.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mDatabaseRef.child("temp").setValue("temp");

    }
}

