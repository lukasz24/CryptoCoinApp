package com.example.radek.finalproduct;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class AdapterListy extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> web;
    private final ArrayList<String> kost;
    private final ArrayList<Integer> imageId;
    public AdapterListy(Activity context, ArrayList<String> web, ArrayList<String> kost, ArrayList<Integer> imageId) {
        super(context, R.layout.list_element, web);
        this.context = context;
        this.web = web;
        this.kost = kost;
        this.imageId = imageId;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View wiersz= inflater.inflate(R.layout.list_element, null, true);
        TextView txtTitle = (TextView) wiersz.findViewById(R.id.nazwa);
        txtTitle.setText(web.get(position));
        TextView priceText = (TextView) wiersz.findViewById(R.id.cena);

        ImageView imageView = (ImageView) wiersz.findViewById(R.id.icon);
        priceText.setTextColor(Color.parseColor("#FF0000"));
        priceText.setText(kost.get(position));
        imageView.setImageResource(imageId.get(position));
        return wiersz;
    }
}