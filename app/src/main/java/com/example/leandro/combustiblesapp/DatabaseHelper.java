package com.example.leandro.combustiblesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME= "Combustibles.db";
    public static final String TABLE_NAME= "Choferes_table";
    public static final String ID= "ID";
    public static final String NOMBRE= "NOMBRE";
    public static final String AP_PATERNO= "APELLIDO_PATERNO";
    public static final String AP_MATERNO= "APELLIDO_MATERNO";
    public static final String USUARIO= "USUARIO";
    public static final String PASS= "PASSWORD";

    public static final String id_est="id_estatico";
    public static final String SOL= "solicitud";
    public static final String UNEGOCIO= "unegocio";
    public static final String FENTREGA= "fentrega";
    public static final String PATENTE= "patente";
    public static final String TVEHICULO= "tipo_vehiculo";
    public static final String UBICACION= "ubicacion";
    public static final String LITROS= "litros";
    public static final String LASIGNADOS= "lasignados";
    public static final String QRECIBE="qrecibe";
    public static final String ESTADO="estado";

    final String CREAR_TABLA_LISTADO="CREATE TABLE listado (id_estatico TEXT unique, solicitud TEXT, unegocio TEXT, fentrega TEXT, patente TEXT, tipo_vehiculo TEXT, ubicacion TEXT, litros TEXT, lasignados TEXT, qrecibe TEXT,estado TEXT)";
    final String CREAR_TABLA_LISTADO2="CREATE TABLE cargado (id_estatico TEXT, solicitud TEXT, unegocio TEXT, fentrega TEXT, hentrega TEXT, patente TEXT, tipo_vehiculo TEXT, ubicacion TEXT,estado TEXT,odometro TEXT, lcargados TEXT, qcarga TEXT)";
    final String CREAR_TABLA_USUARIO="CREATE TABLE usuario (id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, password TEXT, nombre TEXT, apellido TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREAR_TABLA_LISTADO);
        db.execSQL(CREAR_TABLA_USUARIO);
        db.execSQL(CREAR_TABLA_LISTADO2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS listado");
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS cargado");

        db.execSQL(CREAR_TABLA_USUARIO);
        db.execSQL(CREAR_TABLA_LISTADO);
        db.execSQL(CREAR_TABLA_LISTADO2);
    }

    public boolean eliminardatos(){
        try{

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("listado","1",null);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertDataListado(String id_estatico, String solicitud, String unegocio, String fecha, String patente, String tipo_vehiculo, String ubicacion, String litros, String lasignados, String qrecibe, String estado) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(id_est,id_estatico);
            contentValues.put(SOL, solicitud);
            contentValues.put(UNEGOCIO, unegocio);
            contentValues.put(FENTREGA, fecha);
            contentValues.put(PATENTE, patente);
            contentValues.put(TVEHICULO, tipo_vehiculo);
            contentValues.put(UBICACION, ubicacion);
            contentValues.put(LITROS, litros);
            contentValues.put(LASIGNADOS, lasignados);
            contentValues.put(QRECIBE,qrecibe);
            contentValues.put(ESTADO,estado);

            db.insert("listado",null,contentValues);
            db.close();
            return true;
        }catch(Exception exp){
            exp.printStackTrace();
            return false;
        }

    }


}
