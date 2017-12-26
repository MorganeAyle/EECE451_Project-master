package com.example.morgane.eece451_project;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TxtListAdapter extends ArrayAdapter {

    private Activity context;
    private int resource;
    private List<Upload> listtxt;

    public TxtListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Upload> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listtxt = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("i","test");
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView txt = (TextView) v.findViewById(R.id.txt);
        TextView sender = (TextView) v.findViewById(R.id.sender);

        txt.setText(listtxt.get(position).geturl());
        sender.setText("From " + listtxt.get(position).getsender() + ":");
        Log.d("i","test1");
        return v;

    }
}
