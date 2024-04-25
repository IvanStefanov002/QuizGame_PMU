package com.example.loginregister.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.loginregister.R;

public class DropDownAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

    private String[] items;
    private Context context;

    public DropDownAdapter(Context context, String[] items) {
        super(context, R.layout.custom_spinner_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(items[position]);

        return convertView;
    }
}
