package com.example.morgane.eece451_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WelcomePage extends AppCompatActivity {
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }

    public void idButtonClick(View view) {
        Intent idWindow = new Intent(WelcomePage.this, Identification.class);
        startActivity(idWindow);
    }

    public void checkButtonClick(View view) {
        Intent checkWindow = new Intent(WelcomePage.this, Connections.class);
        startActivity(checkWindow);
    }

    public void sendButtonClick(View view) {
        Intent sendWindow = new Intent(WelcomePage.this, Files.class);
        sendWindow.putExtra("username", username); // passing the IBDevice object
        startActivity(sendWindow);
    }
}
