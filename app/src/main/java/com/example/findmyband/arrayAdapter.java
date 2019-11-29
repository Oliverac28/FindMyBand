package com.example.findmyband;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {

    Context context;

    public arrayAdapter (Context context, int resourceId, List<cards> items){
        super(context, resourceId,items);

    }

    public View getView (int position, View convertView, ViewGroup parent){
        cards cards_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent,false);

        }

        TextView mUserID = (TextView) convertView.findViewById(R.id.userId) ;
        TextView mName = (TextView) convertView.findViewById(R.id.name);
        TextView mLName = (TextView) convertView.findViewById(R.id.lName);
        TextView mLocation = (TextView) convertView.findViewById(R.id.location);
        TextView mPrimaryInstrument = (TextView) convertView.findViewById(R.id.primary_instrument);
        TextView mSecondaryInstrument = (TextView) convertView.findViewById(R.id.secondary_instrument);
        TextView mGenre1 = (TextView) convertView.findViewById(R.id.genre_one);
        TextView mGenre2 = (TextView) convertView.findViewById(R.id.genre_two);
        TextView mGenre3 = (TextView) convertView.findViewById(R.id.genre_three);
        TextView mBio = (TextView) convertView.findViewById(R.id.bio);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        mName.setText(cards_item.getName());
        mLName.setText(cards_item.getLName());
        mLocation.setText(cards_item.getLocation());
        mPrimaryInstrument.setText(cards_item.getPrimaryInstrument());
        mSecondaryInstrument.setText(cards_item.getSecondaryInstrument());
        mGenre1.setText(cards_item.getGenre1());
        mGenre2.setText(cards_item.getGenre2());
        mGenre3.setText(cards_item.getGenre3());
        mBio.setText(cards_item.getBio());
        image.setImageResource (R.mipmap.ic_launcher);

        return convertView;
    }
}