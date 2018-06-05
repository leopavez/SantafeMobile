package com.example.leandro.combustiblesapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Date;
import java.util.Locale;

/**
 * Created by LEANDRO on 22-03-2018.
 */

public class Listadodescargado extends AppCompatActivity {

    //SCRIPT DEL LISTADO

    DatabaseHelper myDB;
    public static TextView data;
    public static TextView solicitudes;
    public static TextView patentes;
    public static TextView asignados;
    public static TextView ubicacion;

    final static String urlsanta = "http://santafeinversiones.com/services/listado";


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();
    String camion_id;
    String fecha = dateFormat.format(date);
    Button imprimir_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listadodescargado);
        getSupportActionBar().hide();
        imprimir_btn = (Button) findViewById(R.id.botonbluetooth);
        solicitudes= (TextView) findViewById(R.id.solicitudestxt);
        patentes= (TextView) findViewById(R.id.patentestxt);
        asignados= (TextView) findViewById(R.id.listrosasignados);
        ubicacion= (TextView)findViewById(R.id.ubicacion);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        getDataListado();
        GuardarListado();

        if (extras != null) {
            final String id = extras.getString("IDPATENTE");

        }
        myDB = new DatabaseHelper(this);

    }

    //METODO PARA OBTENER EL LISTADO CORRESPONDIENTE A LA PATENTE SELECCIONADA POR EL SURTIDOR
    public void getDataListado(){

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("IDPATENTE");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url= null;
        HttpURLConnection conn;

        try {
            url = new URL(urlsanta+"/"+fecha+"/"+id);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json ="";

            while((inputLine = in.readLine())!= null) {
                response.append(inputLine);
            }

            json = response.toString();
            JSONArray jsonarr = null;
            jsonarr = new JSONArray(json);

            String solicitudesdata="";
            String patentesdata="";
            String lasignados="";
            String ubicaciondata="";
            String unegocio="";
            String fentrega="";
            String tipo_vehiculo="";
            String litros = "";


            boolean isInserted;
            for (int i = 0; i<jsonarr.length();i++){
                JSONObject jsonObject = jsonarr.getJSONObject(i);
                solicitudesdata +=" "+jsonObject.optString("solicitud_id")+"\n";
                unegocio +=" "+jsonObject.optString("unegocio")+"\n";
                fentrega +=" "+jsonObject.optString("fentrega")+"\n";
                patentesdata +=" "+jsonObject.optString("patente")+"\n";
                tipo_vehiculo +=" "+jsonObject.optString("tvehiculo")+"\n";
                ubicaciondata+=" "+jsonObject.optString("ubicacion")+"\n";
                litros +=" "+jsonObject.optString("litros")+"\n";
                lasignados +=" "+jsonObject.optString("lasignados")+"\n";


            }
            solicitudes.setText(solicitudesdata);
            patentes.setText(patentesdata);
            asignados.setText(lasignados);
            ubicacion.setText(ubicaciondata);




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GuardarListado() {

        myDB = new DatabaseHelper(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("IDPATENTE");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(urlsanta + "/" + fecha + "/" + id);
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

            solicitudes sol = new solicitudes();


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

                myDB.insertDataListado(sol.id_estatico,sol.solicitud_id, sol.unegocio, sol.fentrega, sol.patente, sol.tvehiculo, sol.ubicacion, sol.litros,
                        sol.lasignados,sol.qrecibe,sol.estado);



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



