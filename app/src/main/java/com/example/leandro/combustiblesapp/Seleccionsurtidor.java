package com.example.leandro.combustiblesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
     * Fecha de actualizacion: 13-11-2018
     */

    Spinner spinnersurtidor;
    private boolean isFirst = true;
    Button guardarsurtidor;
    DatabaseHelper myDB;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String TAG =MainActivity.class.getName() ;

    final static String urlDescargaListado = "http://santafeinversiones.com/services/listado";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();
    String fecha = dateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccionsurtidor);
        getSupportActionBar().hide();

        spinnersurtidor = (Spinner) findViewById(R.id.spinnerpatente);
        guardarsurtidor = (Button) findViewById(R.id.btnguardarsurtidor);

        myDB = new DatabaseHelper(this);

        final List<String> Listasurtidores = new ArrayList<String>();
        Listasurtidores.add("Selecciona");
        Listasurtidores.add("KFHD13");
        Listasurtidores.add("GBSP72");
        Listasurtidores.add("FHGS44");
        Listasurtidores.add("MAPEL");
        Listasurtidores.add("MOLINA");
        Listasurtidores.add("COPEC");

        final ArrayAdapter<String> simpleAdapter = new ArrayAdapter<>(Seleccionsurtidor.this, R.layout.support_simple_spinner_dropdown_item, Listasurtidores);
        spinnersurtidor.setAdapter(simpleAdapter);
        spinnersurtidor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    if (Listasurtidores.get(i) == "Selecciona") {
                        guardarsurtidor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Selecciona una patente valida", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        if (Listasurtidores.get(i) == "KFHD13") {
                            guardarsurtidor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = getIntent();
                                    Bundle extras = intent.getExtras();
                                    final String nom = extras.getString("NOM");
                                    final String ape = extras.getString("APEL");
                                    String idsurtidor = "2";
                                    SQLiteDatabase db = myDB.getWritableDatabase();
                                    db.execSQL("DELETE FROM surtidor");
                                    myDB.insertSurtidor(idsurtidor);
                                    descargaInmediata(idsurtidor);
                                    db.execSQL("DELETE FROM listado");
                                    Toast.makeText(getApplicationContext(), "Tu surtidor se configuro: KFHD13", Toast.LENGTH_SHORT).show();
                                    Intent nuevoform= new Intent(Seleccionsurtidor.this, Menu.class);
                                    nuevoform.putExtra("NOMBRE",nom);
                                    nuevoform.putExtra("APELLIDO",ape);
                                    startActivity(nuevoform);
                                }
                            });
                        } else {
                            if (Listasurtidores.get(i) == "GBSP72") {
                                guardarsurtidor.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = getIntent();
                                        Bundle extras = intent.getExtras();
                                        final String nom = extras.getString("NOM");
                                        final String ape = extras.getString("APEL");
                                        String idsurtidor = "1";
                                        SQLiteDatabase db = myDB.getWritableDatabase();
                                        db.execSQL("DELETE FROM surtidor");
                                        myDB.insertSurtidor(idsurtidor);
                                        db.execSQL("DELETE FROM listado");
                                        descargaInmediata(idsurtidor);
                                        Toast.makeText(getApplicationContext(), "Tu surtidor se configuro: GBSP72", Toast.LENGTH_SHORT).show();
                                        Intent nuevoform= new Intent(Seleccionsurtidor.this, Menu.class);
                                        nuevoform.putExtra("NOMBRE",nom);
                                        nuevoform.putExtra("APELLIDO",ape);
                                        startActivity(nuevoform);
                                    }
                                });


                            } else {
                                if (Listasurtidores.get(i) == "FHGS44") {
                                    guardarsurtidor.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = getIntent();
                                            Bundle extras = intent.getExtras();
                                            final String nom = extras.getString("NOM");
                                            final String ape = extras.getString("APEL");
                                            String idsurtidor = "7";
                                            SQLiteDatabase db = myDB.getWritableDatabase();
                                            db.execSQL("DELETE FROM surtidor");
                                            myDB.insertSurtidor(idsurtidor);
                                            db.execSQL("DELETE FROM listado");
                                            descargaInmediata(idsurtidor);
                                            Toast.makeText(getApplicationContext(), "Tu surtidor se configuro: FHGS44", Toast.LENGTH_SHORT).show();
                                            Intent nuevoform= new Intent(Seleccionsurtidor.this, Menu.class);
                                            nuevoform.putExtra("NOMBRE",nom);
                                            nuevoform.putExtra("APELLIDO",ape);
                                            startActivity(nuevoform);
                                        }
                                    });

                                } else {
                                    if (Listasurtidores.get(i) == "MAPEL") {
                                        guardarsurtidor.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = getIntent();
                                                Bundle extras = intent.getExtras();
                                                final String nom = extras.getString("NOM");
                                                final String ape = extras.getString("APEL");
                                                String idsurtidor = "4";
                                                SQLiteDatabase db = myDB.getWritableDatabase();
                                                db.execSQL("DELETE FROM surtidor");
                                                myDB.insertSurtidor(idsurtidor);
                                                db.execSQL("DELETE FROM listado");
                                                descargaInmediata(idsurtidor);
                                                Toast.makeText(getApplicationContext(), "Tu surtidor se configuro: MAPEL", Toast.LENGTH_SHORT).show();
                                                Intent nuevoform= new Intent(Seleccionsurtidor.this, Menu.class);
                                                nuevoform.putExtra("NOMBRE",nom);
                                                nuevoform.putExtra("APELLIDO",ape);
                                                startActivity(nuevoform);
                                            }
                                        });


                                    } else {
                                        if (Listasurtidores.get(i) == "MOLINA") {
                                            guardarsurtidor.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = getIntent();
                                                    Bundle extras = intent.getExtras();
                                                    final String nom = extras.getString("NOM");
                                                    final String ape = extras.getString("APEL");
                                                    String idsurtidor = "6";
                                                    SQLiteDatabase db = myDB.getWritableDatabase();
                                                    db.execSQL("DELETE FROM surtidor");
                                                    myDB.insertSurtidor(idsurtidor);
                                                    db.execSQL("DELETE FROM listado");
                                                    descargaInmediata(idsurtidor);
                                                    Toast.makeText(getApplicationContext(), "Tu surtidor se configuro: MOLINA", Toast.LENGTH_SHORT).show();
                                                    Intent nuevoform= new Intent(Seleccionsurtidor.this, Menu.class);
                                                    nuevoform.putExtra("NOMBRE",nom);
                                                    nuevoform.putExtra("APELLIDO",ape);
                                                    startActivity(nuevoform);
                                                }
                                            });


                                        }else{
                                            if(Listasurtidores.get(i)=="COPEC"){
                                                guardarsurtidor.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = getIntent();
                                                        Bundle extras = intent.getExtras();
                                                        final String nom = extras.getString("NOM");
                                                        final String ape = extras.getString("APEL");
                                                        String idsurtidor = "3";
                                                        SQLiteDatabase db = myDB.getWritableDatabase();
                                                        db.execSQL("DELETE FROM surtidor");
                                                        myDB.insertSurtidor(idsurtidor);
                                                        db.execSQL("DELETE FROM listado");
                                                        descargaInmediata(idsurtidor);
                                                        Toast.makeText(getApplicationContext(), "Tu surtidor se configuro: COPEC", Toast.LENGTH_SHORT).show();
                                                        Intent nuevoform= new Intent(Seleccionsurtidor.this, Menu.class);
                                                        nuevoform.putExtra("NOMBRE",nom);
                                                        nuevoform.putExtra("APELLIDO",ape);
                                                        startActivity(nuevoform);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void descargaInmediata(String idsurtidor){

        SQLiteDatabase db = myDB.getWritableDatabase();

            String urlActualizacion = urlDescargaListado+"/"+fecha+"/"+idsurtidor;

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
