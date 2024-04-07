package com.example.fitfurlife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AccGyroAdapter extends ArrayAdapter<accGyro> {
    public AccGyroAdapter(Context context, List<accGyro> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        accGyro accGyro = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accgyro_item, parent, false);
        }
        // Lookup view for data population
        TextView tvAcc = (TextView) convertView.findViewById(R.id.tvAcc);
        TextView tvGyro = (TextView) convertView.findViewById(R.id.tvGyro);
        // Populate the data into the template view using the data object
        tvAcc.setText(String.valueOf(accGyro.getAcceleration()));
        tvGyro.setText(String.valueOf(accGyro.getGyroscope()));
        // Return the completed view to render on screen
        return convertView;
    }
}
