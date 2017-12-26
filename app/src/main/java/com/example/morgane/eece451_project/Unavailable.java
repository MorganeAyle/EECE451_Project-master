package com.example.morgane.eece451_project;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Unavailable extends AppCompatActivity {
    int channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unavailable);

        Intent intent = getIntent();
        channel = intent.getIntExtra("channel", -1);

        final AlertDialog.Builder builder = new AlertDialog.Builder(Unavailable.this);
        builder.setCancelable(true);

        if (channel == 1) {
            builder.setMessage("Channel 1 not available. Please go back and try another channel.");
        }
        else if (channel == 2) {
            builder.setMessage("Channel 2 not available. Please go back and try another channel.");
        }
        else if (channel == 3) {
            builder.setMessage("Channel 3 not available. Please go back and try another channel.");
        }
        builder.show();
    }
}
