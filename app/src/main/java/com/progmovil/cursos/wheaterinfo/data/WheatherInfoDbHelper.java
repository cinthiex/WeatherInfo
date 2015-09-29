package com.progmovil.cursos.wheaterinfo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by diego.olguin on 18/08/2015.
 */
public class WheatherInfoDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "wheather3.db";

    //Sentencia SQL para crear la tabla de Country
    String sqlCreate = "CREATE TABLE if not exists " + CountryColumns.TABLE_NAME +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CountryColumns.COLUMN_COUNTRY + " TEXT, " +
            CountryColumns.COLUMN_TEMP + " TEXT, " +
            CountryColumns.COLUMN_TEMPMIN + " TEXT, " +
            CountryColumns.COLUMN_TEMPMAX + " TEXT, " +
            CountryColumns.COLUMN_HUMIDITY + " TEXT, " +
            CountryColumns.COLUMN_DESCRIPTION+ " TEXT, " +
            CountryColumns.COLUMN_ICON + " TEXT )";


    public WheatherInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//            SQLiteDatabase db = this.getWritableDatabase();
           // this.onCreate(db);
           // Log.d("CREACION TABLA ","");

//        Log.d("SQL CREATE ", String.valueOf(sqlCreate));


    }

    public WheatherInfoDbHelper(Context contexto, String nombre,
                                SQLiteDatabase.CursorFactory factory, int version) {

        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla

        db.execSQL("DROP TABLE IF EXISTS countries1");

        db.execSQL(sqlCreate);
        Log.d("tabla creada", sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS countries1");
        db.execSQL("DROP TABLE IF EXISTS countries");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }


}
