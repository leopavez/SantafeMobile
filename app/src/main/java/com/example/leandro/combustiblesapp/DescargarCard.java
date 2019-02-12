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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DescargarCard extends AppCompatActivity{

    //SCRIPT PARA DESCARGAR EL LISTADO DEL DIA.

    private static final String TAG =DescargarCard.class.getName() ;
    DatabaseHelper myDB;
    Button descargarlistado;
    Button volveramenu;
    Spinner simplespinner;

    ArrayList<Cargado> listacargado;
    Cargado car = new Cargado();

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    Cursor cursor;
    final static String urlsanta = "http://santafeinversiones.com/services/listado";
    final static String urldespacho="http://santafeinversiones.com/services/despacho/";


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();
    String fecha = dateFormat.format(date);
    private boolean isFirst = true;
    Button enviardatos;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descargarcard);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String nom = extras.getString("NOM");
        final String ape = extras.getString("APEL");


        myDB = new DatabaseHelper(this);

        final SQLiteDatabase db = myDB.getWritableDatabase();


        descargarlistado = (Button)findViewById(R.id.btndescargarlistado);
        volveramenu=(Button)findViewById(R.id.btnvolvermenu);
        enviardatos=(Button)findViewById(R.id.btnenviodatos);

        volveramenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nuevoform= new Intent(DescargarCard.this, Menu.class);
                nuevoform.putExtra("NOMBRE",nom);
                nuevoform.putExtra("APELLIDO",ape);
                startActivity(nuevoform);
            }
        });

        enviardatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            consultarlistadodeldia();
            }
        });



        simplespinner = (Spinner) findViewById(R.id.spinnerpatente);


        final List<String> simpleList = new ArrayList<String>();
        simpleList.add("Selecciona");
        simpleList.add("KFHD13");
        simpleList.add("GBSP72");
        simpleList.add("MAPEL");
        simpleList.add("MOLINA");

        final ArrayAdapter<String> simpleAdapter= new ArrayAdapter<>(DescargarCard.this,R.layout.support_simple_spinner_dropdown_item,simpleList);
        simplespinner.setAdapter(simpleAdapter);
        simplespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                    if (isFirst){
                        isFirst = false;
                    }else{
                        if (simpleList.get(i) == "Selecciona"){
                            descargarlistado.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplicationContext(),"Selecciona una patente valida",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            if (simpleList.get(i) =="KFHD13"){
                                descargarlistado.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String idpatente = "2";

                                        Cursor cursor = db.rawQuery("SELECT id_surtidor FROM surtidor",null);
                                        if (cursor.moveToFirst()==true){
                                            db.execSQL("DELETE FROM surtidor");




                                        }else{
                                        }


                                    }
                                });
                            }else{
                                if (simpleList.get(i)=="GBSP72"){
                                    descargarlistado.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String idpatente= "1";
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);

                                            URL url = null;
                                            HttpURLConnection conn;

                                            try {
                                                url = new URL(urlsanta + "/" + fecha + "/" + idpatente);
                                                conn = (HttpURLConnection) url.openConnection();
                                                conn.setRequestMethod("GET");
                                                conn.connect();
                                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                                                String inputLine;
                                                StringBuffer response = new StringBuffer();
                                                String json = "";

                                                while ((inputLine = in.readLine()) != null) {
                                                    response.append(inputLine);
                                                }


                                                json = response.toString();
                                                JSONArray jsonarr = null;
                                                jsonarr = new JSONArray(json);

                                                Solicitudes sol = new Solicitudes();


                                                boolean isInserted;
                                                for (int i = 0; i < jsonarr.length(); i++) {
                                                    JSONObject jsonObject = jsonarr.getJSONObject(i);
                                                    Log.d("SALIDABUENA",jsonObject.toString());


                                                    sol.id_estatico=jsonObject.getString("id");
                                                    sol.solicitud_id = jsonObject.getString("solicitud_id");
                                                    sol.unegocio = jsonObject.getString("unegocio");
                                                    sol.fentrega = jsonObject.getString("fentrega");
                                                    sol.patente = jsonObject.getString("patente");
                                                    sol.tvehiculo = jsonObject.getString("tvehiculo");
                                                    sol.ubicacion = jsonObject.getString("ubicacion");
                                                    sol.litros = jsonObject.getString("litros");
                                                    sol.lasignados = jsonObject.getString("lasignados");
                                                    sol.qrecibe= jsonObject.getString("qrecibe");
                                                    sol.estado="PENDIENTE";

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
                                                Toast.makeText(getApplicationContext(),"Tu listado se descargo correctamente",Toast.LENGTH_SHORT).show();

                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }else{
                                    if (simpleList.get(i)=="MAPEL"){
                                        descargarlistado.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String idpatente= "4";
                                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                                StrictMode.setThreadPolicy(policy);

                                                URL url = null;
                                                HttpURLConnection conn;

                                                try {
                                                    url = new URL(urlsanta + "/" + fecha + "/" + idpatente);
                                                    conn = (HttpURLConnection) url.openConnection();
                                                    conn.setRequestMethod("GET");
                                                    conn.connect();
                                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                                                    String inputLine;
                                                    StringBuffer response = new StringBuffer();
                                                    String json = "";

                                                    while ((inputLine = in.readLine()) != null) {
                                                        response.append(inputLine);
                                                    }


                                                    json = response.toString();
                                                    JSONArray jsonarr = null;
                                                    jsonarr = new JSONArray(json);

                                                    Solicitudes sol = new Solicitudes();


                                                    boolean isInserted;
                                                    for (int i = 0; i < jsonarr.length(); i++) {
                                                        JSONObject jsonObject = jsonarr.getJSONObject(i);
                                                        Log.d("SALIDABUENA",jsonObject.toString());


                                                        sol.id_estatico=jsonObject.getString("id");
                                                        sol.solicitud_id = jsonObject.getString("solicitud_id");
                                                        sol.unegocio = jsonObject.getString("unegocio");
                                                        sol.fentrega = jsonObject.getString("fentrega");
                                                        sol.patente = jsonObject.getString("patente");
                                                        sol.tvehiculo = jsonObject.getString("tvehiculo");
                                                        sol.ubicacion = jsonObject.getString("ubicacion");
                                                        sol.litros = jsonObject.getString("litros");
                                                        sol.lasignados = jsonObject.getString("lasignados");
                                                        sol.qrecibe= jsonObject.getString("qrecibe");
                                                        sol.estado="PENDIENTE";

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
                                                    Toast.makeText(getApplicationContext(),"Tu listado se descargo correctamente",Toast.LENGTH_SHORT).show();

                                                } catch (MalformedURLException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    }else{
                                        if (simpleList.get(i)=="MOLINA"){
                                            descargarlistado.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String idpatente= "6";
                                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                                    StrictMode.setThreadPolicy(policy);

                                                    URL url = null;
                                                    HttpURLConnection conn;

                                                    try {
                                                        url = new URL(urlsanta + "/" + fecha + "/" + idpatente);
                                                        conn = (HttpURLConnection) url.openConnection();
                                                        conn.setRequestMethod("GET");
                                                        conn.connect();
                                                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                                                        String inputLine;
                                                        StringBuffer response = new StringBuffer();
                                                        String json = "";

                                                        while ((inputLine = in.readLine()) != null) {
                                                            response.append(inputLine);
                                                        }


                                                        json = response.toString();
                                                        JSONArray jsonarr = null;
                                                        jsonarr = new JSONArray(json);

                                                        Solicitudes sol = new Solicitudes();


                                                        boolean isInserted;
                                                        for (int i = 0; i < jsonarr.length(); i++) {
                                                            JSONObject jsonObject = jsonarr.getJSONObject(i);
                                                            Log.d("SALIDABUENA",jsonObject.toString());


                                                            sol.id_estatico=jsonObject.getString("id");
                                                            sol.solicitud_id = jsonObject.getString("solicitud_id");
                                                            sol.unegocio = jsonObject.getString("unegocio");
                                                            sol.fentrega = jsonObject.getString("fentrega");
                                                            sol.patente = jsonObject.getString("patente");
                                                            sol.tvehiculo = jsonObject.getString("tvehiculo");
                                                            sol.ubicacion = jsonObject.getString("ubicacion");
                                                            sol.litros = jsonObject.getString("litros");
                                                            sol.lasignados = jsonObject.getString("lasignados");
                                                            sol.qrecibe= jsonObject.getString("qrecibe");
                                                            sol.estado="PENDIENTE";

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
                                                        Toast.makeText(getApplicationContext(),"Tu listado se descargo correctamente",Toast.LENGTH_SHORT).show();

                                                    } catch (MalformedURLException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });

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

    private void consultarlistadodeldia(){
        SQLiteDatabase db = myDB.getReadableDatabase();

        listacargado= new ArrayList<Cargado>();
        cursor = db.rawQuery("SELECT id_estatico,fentrega,odometro, lcargados, hentrega, qcarga FROM cargado",null);

        while (cursor.moveToNext()){
            car = new Cargado();
            car.setId_estatico(cursor.getString(0));
            car.setFentrega(cursor.getString(1));
            car.setOdometro(cursor.getString(2));
            car.setLcargados(cursor.getString(3));
            car.setHentrega(cursor.getString(4));

            listacargado.add(car);

        }

        for (int i=0; i<listacargado.size(); i++){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = null;
            HttpURLConnection conn;
            try {
                url =  new URL(urldespacho);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                conn.connect();

                JSONObject json= new JSONObject();
                json.put("id_estatico",listacargado.get(i).getId_estatico().toString());
                json.put("frentrega",listacargado.get(i).getFentrega().toString());
                json.put("horometro",listacargado.get(i).getOdometro().toString());
                json.put("lcargados",listacargado.get(i).getLcargados().toString());
                json.put("loghora",listacargado.get(i).getHentrega().toString());
                OutputStreamWriter out = new   OutputStreamWriter(conn.getOutputStream());
                out.write(json.toString());
                out.close();

                int responseCode= conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    Toast.makeText(getApplicationContext(),"DATOS ENVIADOS CORRECTAMENTE",Toast.LENGTH_SHORT).show();
                    db.execSQL("DELETE FROM cargado");

                }else{
                    Toast.makeText(getApplicationContext(),"DATOS NO ENVIADOS",Toast.LENGTH_SHORT).show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }


    public void descargarListado(String id){

        //SCRIPT DE DESCARGA DEL LISTADO

        if(cursor.moveToFirst()==true){

            String url = urlsanta+fecha+id;
            mRequestQueue = Volley.newRequestQueue(this);
            mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        String json;
                        json = response.toString();
                        JSONArray jsonarr = null;
                        jsonarr = new JSONArray();
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

                            Cursor cursor = db.rawQuery("SELECT * FROM listado WHERE id_estatico='" + sol.id_estatico + "'", null);
                            boolean esta = true;

                            if (cursor.getCount() <= 0) {

                                esta = false;
                                myDB.insertDataListado(sol.id_estatico, sol.solicitud_id, sol.unegocio, sol.fentrega, sol.patente, sol.tvehiculo, sol.ubicacion, sol.litros,
                                        sol.lasignados, sol.qrecibe, sol.estado);

                            }
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG,"Error de conexion: "+error.toString());
                }
            });
            mRequestQueue.add(mStringRequest);
        }


    }



}
