package com.progmovil.cursos.wheaterinfo;

import com.progmovil.cursos.wheaterinfo.data.WheatherInfoDbHelper;
import com.progmovil.cursos.wheaterinfo.data.CountryColumns;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {
    private ListView mainList;
    private MainListAdapter mainListAdapter;
    private static final String LOG_TAG = Utility.class.getSimpleName();
    private String CountryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<Country> countries = new ArrayList<Country>();
        mainListAdapter = new MainListAdapter(this, countries);
        mainList = (ListView) findViewById(R.id.main_list);

        mainList.setAdapter(mainListAdapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Intent intent = new Intent(getBaseContext(), MatchDetailsActivity.class);
                Match item = (Match) mainListAdapter.getItem(i);
                intent.putExtra("IDMATCH", item.getIdentifier());
                startActivity(intent);*/

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshResults();
    }

    public void refreshResults() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        CountryId = prefs.getString("defaultCountry", "Cochabamba");

        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        loadResultsList(CountryId);

        GetResultTask task = new GetResultTask();
        task.execute(CountryId);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(LOG_TAG, "itemselected" + String.valueOf(id));
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_refresh) {
            refreshResults();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class GetResultTask extends AsyncTask<String, Void, ArrayList<Country>> {
         public final String LOG_TAG = Utility.class.getSimpleName();

        @Override
        protected ArrayList<Country> doInBackground(String... params) {

            if (params.length != 1) {
                return null;
            }
            String resultString = Utility.getJsonStringFromNetwork(params[0]);
            Log.d(LOG_TAG, "RESULTSTRING" + resultString);


            try {

                return Utility.parseFixtureJson(resultString);


            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing" + e.getMessage(), e);
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Country> results) {
            Log.d("CMC- a onPostExe", "");
            if (results != null) {
                if (!existsLocalData(CountryId)) {
                    saveData(results, CountryId);

                } else {
                    updateData(results, CountryId);
                }

                mainListAdapter.clear();
                mainList.invalidate();
                for (Country result : results) {
                    mainListAdapter.add(result);
                }
            } else {
                Toast.makeText(getBaseContext(), getBaseContext().getResources().getString(R.string.error_get_results), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void updateData(ArrayList<Country> results, String teamId) {



        for (Country result : results) {
            ContentValues matchValues = new ContentValues();
            matchValues.put(CountryColumns.COLUMN_COUNTRY, result.getCountryName());
            matchValues.put(CountryColumns.COLUMN_TEMP, result.getTemp());
            matchValues.put(CountryColumns.COLUMN_TEMPMIN, result.getTempMin());
            matchValues.put(CountryColumns.COLUMN_TEMPMAX, result.getTempMax());
            matchValues.put(CountryColumns.COLUMN_HUMIDITY, result.getHumidity());
            matchValues.put(CountryColumns.COLUMN_DESCRIPTION, result.getDescription());
            matchValues.put(CountryColumns.COLUMN_ICON, result.getIcon());



            this.getContentResolver().insert(CountryColumns.CONTENT_URI, matchValues);
            Log.d(LOG_TAG, "Se guardaron los datos");
        }

    }

    public void loadResultsList(String countryName) {

        mainListAdapter.clear();
        mainList.invalidate();

        Cursor cursor = this.getContentResolver().query(
                CountryColumns.CONTENT_URI,
                null,
                null,//CountryColumns.COLUMN_COUNTRY + " = " + countryName,
                null,
                CountryColumns._ID+  " DESC");


        while (cursor.moveToNext()) {
            Log.d(LOG_TAG, "cinthia"+ cursor.getString(1) +" -"+cursor.getString(2)+" -"+cursor.getString(3)+" -"+ cursor.getString(4)+" -"+ cursor.getString(5)+" -"+ cursor.getString(6)+" -"+ cursor.getString(7));

            mainListAdapter.add(new Country(cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7)));

            Log.d(LOG_TAG, "Estoy dentro del cursor");
        }

    }



    private void saveData(ArrayList<Country> results, String teamId) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Country result : results) {
            ContentValues matchValues = new ContentValues();
            matchValues.put(CountryColumns.COLUMN_COUNTRY, result.getCountryName());
            matchValues.put(CountryColumns.COLUMN_TEMP, result.getTemp());
            matchValues.put(CountryColumns.COLUMN_TEMPMIN, result.getTempMin());
            matchValues.put(CountryColumns.COLUMN_TEMPMAX, result.getTempMax());
            matchValues.put(CountryColumns.COLUMN_HUMIDITY, result.getHumidity());
            matchValues.put(CountryColumns.COLUMN_DESCRIPTION, result.getDescription());
            matchValues.put(CountryColumns.COLUMN_ICON, result.getIcon());


            this.getContentResolver().insert(CountryColumns.CONTENT_URI, matchValues);
            Log.d(LOG_TAG, "Se guardaron los datos");

        }

    }

    private boolean existsLocalData(String mycountry) {

      //  String[] columns = new String[]{CountryColumns._ID,CountryColumns.TABLE_NAME,CountryColumns.COLUMN_TEMP};

        Cursor cursor = this.getContentResolver().query(
                CountryColumns.CONTENT_URI,
                null,
                null,
                null, //CountryColumns.COLUMN_COUNTRY + " = " + mycountry,
                null,
                null);
        if (cursor.moveToFirst()) {

            Log.d(LOG_TAG, "RESULTADO  TRUE");

            return true;
        } else {
            Log.d(LOG_TAG, "RESULTADO  FALSE" );
            return false;
        }




    }



    public Date toFormatedDate(String s) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date res = null;
        try {

            res = formatter.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

}

