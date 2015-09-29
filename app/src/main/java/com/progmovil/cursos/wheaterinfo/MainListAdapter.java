package com.progmovil.cursos.wheaterinfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cinthia.martinez on 22/09/2015.
 */


public class MainListAdapter extends ArrayAdapter {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    public MainListAdapter(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_list_item, null);
        }

        Log.e(LOG_TAG, "cmc INICIANDOOOOOLISTADAPTER" + convertView.toString());

        TextView countryName = (TextView) convertView.findViewById(R.id.name);
        TextView countryTemp = (TextView) convertView.findViewById(R.id.temp);
        TextView countryHumidity = (TextView) convertView.findViewById(R.id.humidity);
        TextView countryDescription = (TextView) convertView.findViewById(R.id.description);
       // TextView icon = (TextView) convertView.findViewById(R.id.icono);
        Country country = (Country) getItem(position);


        countryName.setText(country.getCountryName());
        countryTemp.setText(country.getTemp());
        countryHumidity.setText(country.getHumidity());
        countryDescription.setText(country.getDescription());
       // icon.setText(country.getDescription());




        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return convertView;
    }


}