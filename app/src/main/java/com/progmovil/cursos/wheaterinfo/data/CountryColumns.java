package com.progmovil.cursos.wheaterinfo.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by cinthia.martinez on 18/08/2015.
 */
public class CountryColumns implements BaseColumns {

    // Path base de results
    public static final Uri CONTENT_URI = WheatherInfoDbContract.BASE_CONTENT_URI.buildUpon().appendPath(WheatherInfoDbContract.PATH_RESULT).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + WheatherInfoDbContract.CONTENT_AUTHORITY + "/" + WheatherInfoDbContract.PATH_RESULT;

    public static final String TABLE_NAME = "countries1";

    //Nombres de columnas

    //public static final String COLUMN_IDENTIFIER = "country_id";
    public static final String COLUMN_COUNTRY = "name";
    public static final String COLUMN_TEMP = "temp";
    public static final String COLUMN_TEMPMIN = "temp_min";
    public static final String COLUMN_TEMPMAX = "temp_max";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ICON = "icon";


    public static int getCountryFromUri(Uri uri) {
        return Integer.parseInt(uri.getPathSegments().get(1));
    }

    public static Uri buildMatchUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }


}
