package com.progmovil.cursos.wheaterinfo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.progmovil.cursos.wheaterinfo.Utility;

public class WheatherInfoContentProvider extends ContentProvider {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    private static final UriMatcher uriMatcher = buildUriMatcher();

    static final int RESULT = 100;
    static final int RESULT_WITH_TEAM = 101;

    private WheatherInfoDbHelper dbHelper;


    /**
     * Construcción del UriMatcher, Este UriMatcher hara posible
     * que por cada URI que se pase, se devuelva un valor constante
     * que nos permita identificar luego en el sistema
     */
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WheatherInfoDbContract.CONTENT_AUTHORITY;

        // “content://com.salamancasolutions.WheaterInfo/matchs” –> Acceso genérico a tabla de partidos
        // “content://com.salamancasolutions.WheaterInfo/matchs/81″ –> Acceso directo a partidos por id equipo
        matcher.addURI(authority, WheatherInfoDbContract.PATH_RESULT, RESULT);
        matcher.addURI(authority, WheatherInfoDbContract.PATH_RESULT + "/#", RESULT_WITH_TEAM);

        return matcher;
    }

    public WheatherInfoContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
          int match = uriMatcher.match(uri);
        int deleted;

        if (selection == null)
            selection = "1";

        if (match == RESULT) {
            deleted = db.delete(CountryColumns.TABLE_NAME, selection, selectionArgs);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (deleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case RESULT:
                return CountryColumns.CONTENT_TYPE;
            case RESULT_WITH_TEAM:
                return CountryColumns.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //db.execSQL("DROP TABLE " + CountryColumns.TABLE_NAME);
       // dbHelper.onCreate(getContext());
        db.delete( CountryColumns.TABLE_NAME, null, null);
        int match = uriMatcher.match(uri);
        Uri returnUri;

        if (match == RESULT) {
            long id = db.insert(CountryColumns.TABLE_NAME, null, values);

            if (id > 0)
                returnUri = CountryColumns.buildMatchUri(id);
            else
                throw new SQLException("Failed to insert row into " + uri);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WheatherInfoDbHelper(getContext());
        Log.d("creacion db", String.valueOf(dbHelper.getDatabaseName()));
        return false;

    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        //Si es una consulta a un team_id concreto construimos el WHERE
        String where = selection;


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(CountryColumns.TABLE_NAME, projection, where,
                selectionArgs, null, null, sortOrder);


        String[] nombrecol=c.getColumnNames();
      //  Log.d(LOG_TAG, "inserttt" +c.getColumnIndex("_id"));

        for(int i=0; i< c.getColumnCount(); i++) {
            Log.d(LOG_TAG, "columna de tabla" + nombrecol[i].toString());
        }

        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int updated;

        if (selection == null)
            selection = "1";

        if (match == RESULT) {
            updated = db.update(CountryColumns.TABLE_NAME, values, selection, selectionArgs);



        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (updated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return updated;
    }
}
