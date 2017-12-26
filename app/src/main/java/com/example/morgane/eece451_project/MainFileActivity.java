package com.example.morgane.eece451_project;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.Serializable;

public class MainFileActivity extends AppCompatActivity implements View.OnClickListener {

        //this is the pic pdf code used in file chooser
        final static int PICK_PDF_CODE = 2342;

        //these are the views
        TextView textViewStatus;
        EditText receiverName;
        ProgressBar progressBar;

        //the firebase objects for storage and database
        StorageReference mStorageReference;
        DatabaseReference mDatabaseReference;

        private String sender;
        private int channel;
        int i;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_file);

            Intent intent = getIntent();
            sender = intent.getStringExtra("username");
            channel = intent.getIntExtra("channel", 0);

            i = 0;

            //getting firebase objects
            mStorageReference = FirebaseStorage.getInstance().getReference();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

            //getting the views
            textViewStatus = (TextView) findViewById(R.id.textViewStatus);
            progressBar = (ProgressBar) findViewById(R.id.progressbar);
            receiverName = (EditText) findViewById(R.id.receiverName);

            //attaching listeners to views
            findViewById(R.id.buttonSendFile).setOnClickListener(this);
        }

        //this function will get the pdf from the storage
        private void getFile() {
            //for greater than lolipop versions we need the permissions asked on runtime
            //so if the permission is not available user will go to the screen to allow storage permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                return;
            }

            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            //when the user choses the file
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                //if a file is selected
                if (data.getData() != null) {
                    sendFile(data.getData());
                } else{
                    Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
                }
            }
        }
        public static String getMimeType(Context context, Uri uri) {
            String extension;

            //Check uri format to avoid null
            if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                //If scheme is a content
                final MimeTypeMap mime = MimeTypeMap.getSingleton();
                extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
            } else {
                //If scheme is a File
                //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
                extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

            }

            return extension;
        }

        //this method is sending the file
        //the code is same as the previous tutorial
        //so we are not explaining it
        private void sendFile(Uri data) {
            progressBar.setVisibility(View.VISIBLE);

            final int[] sameChannel = {0};

            StorageReference sRef = mStorageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + '.'+ getMimeType(this,data));
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (sameChannel[0] == 1) {
                                progressBar.setVisibility(View.GONE);
                                textViewStatus.setText("File Sent Successfully");

                                Upload upload = new Upload(sender, taskSnapshot.getDownloadUrl().toString(), receiverName.getText().toString());
                                mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);

                                i++;
                            }
                            else if (sameChannel[0] == 2) {
                                progressBar.setVisibility(View.GONE);
                                textViewStatus.setText("This user is not in your channel.");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            String usersPath = "";

                            if (channel == 1)
                                usersPath = "Users1";
                            else if (channel == 2)
                                usersPath = "Users2";
                            else if (channel == 3)
                                usersPath = "Users3";

                            Log.d("channel ", String.valueOf(channel));

                            if (i == 0) {
                                // receiverName.getText().toString()
                                final String finalUsersPath = usersPath;
                                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Iterable<DataSnapshot> data = dataSnapshot.child(finalUsersPath).getChildren();
                                        for (DataSnapshot s: data) {
                                            if (s.getValue().equals(receiverName.getText().toString())) {
                                                sameChannel[0] = 1;
                                                i = 1;
                                                break;
                                            }
                                            else {
                                                sameChannel[0] = 2;
                                                i = 0;
                                            }
                                        }
                                        Log.d("bla ", String.valueOf(sameChannel[0]));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });

                                mDatabaseReference.child("temp").setValue("temp");

                            }

//                            Log.d("bla outside listener", String.valueOf(sameChannel[0]));

//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                            textViewStatus.setText((int) progress + "% Sending...");
                        }
                    });

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonSendFile:
                    textViewStatus.setText("No file selected");
                    getFile();
                    break;
            }
        }
    }