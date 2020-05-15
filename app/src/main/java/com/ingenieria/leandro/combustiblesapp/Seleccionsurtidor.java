package com.ingenieria.leandro.combustiblesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Seleccionsurtidor extends AppCompatActivity {

    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 18-07-2019
     */

    DatabaseHelper myDB;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String TAG =MainActivity.class.getName() ;

    final static String urlDescargaListado = "http://santafeinversiones.com/services/listado";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();
    String fecha = dateFormat.format(date);
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccionsurtidor);
        getSupportActionBar().hide();

        myDB = new DatabaseHelper(this);

        listView = (ListView)findViewById(R.id.listview);
        cargaSurtidores();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final String surtidor = (listView.getItemAtPosition(i).toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(Seleccionsurtidor.this);
                builder.setTitle("Confirmar Surtidores: "+surtidor);
                builder.setMessage("¿Estas seguro de confirmar el Surtidores?");
                builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences preferences = getSharedPreferences("Surtidores",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        SQLiteDatabase db = myDB.getWritableDatabase();
                        Cursor cursor = db.rawQuery("SELECT id_comb FROM surtidores WHERE patente='"+surtidor+"' ",null);

                        if (cursor.moveToFirst()){
                            String idsurtidor = cursor.getString(0);
                            editor.putString("idsurtidor",idsurtidor);
                            editor.commit();
                            descargaInmediata(idsurtidor);
                            Toast.makeText(getApplicationContext(), "Tu surtidor se configuro: "+surtidor, Toast.LENGTH_SHORT).show();
                            Intent nuevoform= new Intent(Seleccionsurtidor.this, Menu.class);
                            startActivity(nuevoform);
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }


    private void cargaSurtidores() {
        ArrayList<String>surtidores = new ArrayList<>();

        SQLiteDatabase db = myDB.getReadableDatabase();
        final Cursor c = db.rawQuery("SELECT patente AS _id,patente FROM surtidores", null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            surtidores.add(c.getString(c.getColumnIndex("patente")));
            c.moveToNext();
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,surtidores);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }

    public void descargaInmediata(String idsurtidor){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fechadescarga = dateFormat.format(date);

            String urlActualizacion = urlDescargaListado+"/"+fechadescarga+"/"+idsurtidor;
            Log.i("TAG","EJECUTANDO DESCARGA INMEDIATA");
            mRequestQueue = Volley.newRequestQueue(this);
            mStringRequest = new StringRequest(Request.Method.GET, urlActualizacion, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        String json;
                        Log.i(TAG, "Response" + response.toString());
                        json = response.toString();
                        JSONArray jsonarr = null;
                        jsonarr = new JSONArray(json);
                        Solicitudes sol = new Solicitudes();

                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonObject = jsonarr.getJSONObject(i);
                            sol.id_estatico = jsonObject.getString("id");
                            sol.solicitud_id = jsonObject.getString("solicitud_id");
                            sol.unegocio = jsonObject.getString("unegocio");
                            sol.fentrega = jsonObject.getString("fentrega");
                            sol.patente = jsonObject.getString("patente");
                            sol.tvehiculo = jsonObject.getString("tvehiculo");
                            sol.ubicacion = jsonObject.getString("ubicacion");
                            sol.litros = jsonObject.getString("litros");
                            sol.lasignados = jsonObject.getString("lasignados");
                            sol.qrecibe = jsonObject.getString("qrecibe");
                            sol.estado = "PENDIENTE";


                            SQLiteDatabase db = myDB.getWritableDatabase();
                            db.execSQL("DELETE FROM listado");
                            Cursor cursor = db.rawQuery("SELECT * FROM listado WHERE id_estatico='"+sol.id_estatico+"'",null);
                            boolean esta=true;

                            if (cursor.getCount()<=0){
                                esta=false;
                                myDB.insertDataListado(sol.id_estatico, sol.solicitud_id, sol.unegocio, sol.fentrega, sol.patente, sol.tvehiculo, sol.ubicacion, sol.litros,
                                        sol.lasignados,sol.qrecibe,sol.estado);
                                Log.d("MSG","AGREGADO A TU LISTADO!");
                            }else{
                                esta=true;
                                Log.d("MSG","CAMPO REPETIDO");
                            }
                        }
                        Toast.makeText(getApplicationContext(),"TU LISTADO SE ACTUALIZO CORRECTAMENTE",Toast.LENGTH_SHORT).show();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            mRequestQueue.add(mStringRequest);
        }

    }
