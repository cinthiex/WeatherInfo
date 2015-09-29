package com.progmovil.cursos.wheaterinfo;

/**
 * Created by cinthia.martinez on 22/09/2015.
 */

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();
    public static String getJsonStringFromNetwork( String name) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // http://api.openweathermap.org/data/2.5/find?q=Bogota&units=metric&lang=sp
        try {
            final String FINDWHEATER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
            final String FIND_PATH = "find";
            final String TIME_FRAME_PARAMETER_COUNTRY = "q";
            final String TIME_FRAME_PARAMETER_UNIT = "units";
            final String TIME_FRAME_PARAMETER_LANG = "lang";

            Uri builtUri = Uri.parse(FINDWHEATER_BASE_URL).buildUpon()
                   // .appendPath(team)
                    .appendPath(FIND_PATH)
                    .appendQueryParameter(TIME_FRAME_PARAMETER_COUNTRY, name)
                    .appendQueryParameter(TIME_FRAME_PARAMETER_UNIT, "metric")
                    .appendQueryParameter(TIME_FRAME_PARAMETER_LANG, "sp")
                    .build();
            URL url = new URL(builtUri.toString());

            Log.d("API Url", builtUri.toString());
            //http://api.openweathermap.org/data/2.5/find?q=Bogota
            //http://api.openweathermap.org/data/2.5/find?q=Bogota&units=metric&lang=sp


            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null)
                return "";
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }


            if (buffer.length() == 0)
                return "";

            return buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                    e.printStackTrace();
                }
            }
        }

        return "";
    }


    //Parse string JSON to List
    public static ArrayList<Country> parseFixtureJson(String fixtureJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(fixtureJson);
        ArrayList<Country> result = new ArrayList<Country>();

        JSONArray countriesArray = jsonObject.getJSONArray("list");
        String count = jsonObject.getString("count");

        Log.d("Count results", count);

        for (int i = 0; i < countriesArray.length(); i++) {
            String name;
            String temp;

            String temp_min;
            String temp_max;
            String humidity;
            String description="";
            String icon="";


            Date date = new Date();
            JSONObject countryObject = countriesArray.getJSONObject(i);
            JSONObject mainObject = countryObject.getJSONObject("main");
            JSONArray weatherarray = countryObject.getJSONArray("weather");

                name = countryObject.getString("name");
            temp = mainObject.getString("temp");
            temp_min=mainObject.getString("temp_min");
            temp_max=mainObject.getString("temp_max");
            humidity=mainObject.getString("humidity");

         //   for (int j = 0; j < weatherarray.length(); j++) {

                JSONObject weatherObject = weatherarray.getJSONObject(i);
                description=weatherObject.getString("description");
                icon=weatherObject.getString("icon");


        //    }

            try{
                //2015-08-30T15:00:00Z
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                //date = df1.parse(Date.);
            } catch (Exception e){
                Log.e(LOG_TAG, "Error parsing date", e);
            }

            Log.d(LOG_TAG, name + ", " + temp);

            Country country = new Country(name,
                    temp,temp_min,temp_max,humidity,description,icon);
            result.add(country);


        }
        return result;
    }

}
