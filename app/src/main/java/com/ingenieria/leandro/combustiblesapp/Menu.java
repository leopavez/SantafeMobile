package com.ingenieria.leandro.combustiblesapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by LEANDRO on 22-03-2018.
 */

public class Menu extends AppCompatActivity implements View.OnClickListener {

    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 18-07-2019
     */
    private static final String TAG = MainActivity.class.getName();
    DatabaseHelper myDB = new DatabaseHelper(this);
    Dialog popnopatente;
    Dialog popsipatente;
    private CardView listado_Card, buscardor_Card, ajustes_Card, reportes_card;


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    final static String urlDescargaListado = "http://santafeinversiones.com/services/listado";
    final static String urlEnvioDatos = "http://santafeinversiones.com/services/despacho/";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();
    String fecha = dateFormat.format(date);

    Solicitudes sol = new Solicitudes();

    ArrayList<Cargado> listacargado;
    Cargado car = new Cargado();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.menu);
        listado_Card = (CardView) findViewById(R.id.listado_Card);
        buscardor_Card = (CardView) findViewById(R.id.buscador_Card);
        ajustes_Card = (CardView) findViewById(R.id.ajustes_Card);
        reportes_card = (CardView)findViewById(R.id.reportescardview);

        listado_Card.setOnClickListener(this);
        buscardor_Card.setOnClickListener(this);
        ajustes_Card.setOnClickListener(this);
        reportes_card.setOnClickListener(this);


        popnopatente = new Dialog(this);
        popsipatente = new Dialog(this);
        myDB = new DatabaseHelper(this);
        Actualizar actualizar = new Actualizar();
        actualizar.execute();
        AlertarListadoAtrasado();

    }

    //SCRIPT QUE PERMITE LA SELECCION DEL MENU MEDIANTE CARD
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.listado_Card :
                i = new Intent(Menu.this,ListadoCard.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.buscador_Card:
                i = new Intent(Menu.this,Buscadorcard.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.reportescardview:
                i = new Intent(Menu.this, Reportes.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.ajustes_Card:
                i = new Intent(Menu.this,Ajustes.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    public void Segundoplano() {

        try {
            Thread.sleep(120000);
            actualizacionlistado();
            enviodedatosautomatico();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public class Actualizar extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i = 1; i <= 3; i++) {
                Segundoplano();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Actualizar actualizar = new Actualizar();
            actualizar.execute();
        }
    }


    /*
     * Script para actualizar el listado del usuario
     * segun le vallan asignando nuevas solicitudes
     *
     * Proximo cambio: Se agregara funcionalidad para
     * realizar una notificacion push
     */
    public void actualizacionlistado() {

        SharedPreferences preferences = getSharedPreferences("surtidor", Context.MODE_PRIVATE);
        String idsurtidor = preferences.getString("idsurtidor", "SIN SURTIDOR");

        String urlActualizacion = urlDescargaListado + "/" + fecha + "/" + idsurtidor;

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
                        Cursor cursor = db.rawQuery("SELECT * FROM listado WHERE id_estatico='" + sol.id_estatico + "'", null);
                        boolean esta = true;

                        if (cursor.getCount() <= 0) {
                            esta = false;
                            myDB.insertDataListado(sol.id_estatico, sol.solicitud_id, sol.unegocio, sol.fentrega, sol.patente, sol.tvehiculo, sol.ubicacion, sol.litros,
                                    sol.lasignados, sol.qrecibe, sol.estado);
                            Log.d("MSG", "AGREGADO A TU LISTADO!");


                            NotificationCompat.Builder builder;
                            NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

                            builder = new NotificationCompat.Builder(getApplicationContext(),"1")
                                    .setSmallIcon(R.drawable.logosantafe)
                                    .setContentTitle("Nuevas solicitudes")
                                    .setContentText("Se han agregado nuevas solicitudes a tu listado")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            notificationManager.notify(1,builder.build());
                        } else {
                            esta = true;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "LISTADO ACTUALIZADO", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
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

    /*
     * Script que realiza la peticion HTTP a la API
     * -> Envio de cargas al Dashboard WEB
     */

    public void enviodedatosautomatico() {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String estado = "CARGADO";

        listacargado = new ArrayList<Cargado>();
        Cursor cursor = db.rawQuery("SELECT id_estatico,fentrega,odometro, lcargados, hentrega, qcarga FROM cargado WHERE estado='" + estado + "' ORDER BY id_estatico ASC", null);

        while (cursor.moveToNext()) {
            car = new Cargado();
            car.setId_estatico(cursor.getString(0));
            car.setFentrega(cursor.getString(1));
            car.setOdometro(cursor.getString(2));
            car.setLcargados(cursor.getString(3));
            car.setHentrega(cursor.getString(4));
            car.setQcarga(cursor.getString(5));
            listacargado.add(car);

        }
        for (int i = 0; i < listacargado.size(); i++) {

            final String id_estatico = listacargado.get(i).getId_estatico().toString();
            final String fentrega = listacargado.get(i).getFentrega().toString();
            final String horometro = listacargado.get(i).getOdometro().toString();
            final String lcargados = listacargado.get(i).getLcargados().toString();
            final String lhora = listacargado.get(i).getHentrega().toString();
            final String loguser = listacargado.get(i).getQcarga().toString();

            final RequestParams params = new RequestParams();
            params.put("id_estatico", id_estatico);
            params.put("frentrega", fentrega);
            params.put("horometro", horometro);
            params.put("lcargados", lcargados);
            params.put("loghora", lhora);
            params.put("logfecha",fentrega);
            params.put("loguser",loguser);

            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.setMaxRetriesAndTimeout(0, 1500);
                    client.post(urlEnvioDatos, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {
                                String response = new String(responseBody).toUpperCase();
                                Log.i("TAG/RESP", "ENVIO STATUS 200 OK: " + response);
                                SQLiteDatabase db = myDB.getReadableDatabase();
                                db.execSQL("UPDATE cargado SET estado='ENVIADO' WHERE id_estatico='" + id_estatico + "'");
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }
            };
            handler.post(runnable);
        }
    }


   /*

    */


    /*
     * Script para alertar que el usuario posee un listado
     * con fecha distinta a la del día actual.
     */
    public void AlertarListadoAtrasado() {
        myDB = new DatabaseHelper(this);
        SQLiteDatabase db = myDB.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);


        Cursor cursor = db.rawQuery("SELECT fentrega FROM listado WHERE fentrega='" + fecha + "'", null);
        if (cursor.moveToFirst()) {
            setContentView(R.layout.menu);
            listado_Card = (CardView) findViewById(R.id.listado_Card);
            buscardor_Card = (CardView) findViewById(R.id.buscador_Card);
            ajustes_Card = (CardView) findViewById(R.id.ajustes_Card);
            reportes_card = (CardView)findViewById(R.id.reportescardview);
            reportes_card.setOnClickListener(this);
            listado_Card.setOnClickListener(this);
            buscardor_Card.setOnClickListener(this);
            ajustes_Card.setOnClickListener(this);

        } else {
            final AlertDialog.Builder alertalistado = new AlertDialog.Builder(Menu.this);
            alertalistado.setTitle("Actualiza tu listado");
            alertalistado.setMessage("Tu listado no corresponde a la entrega del dia." + "\n" + "Por favor actualiza tu listado.");
            alertalistado.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    SQLiteDatabase db = myDB.getWritableDatabase();
                    db.execSQL("DELETE FROM listado");
                    actualizacionlistado();
                }
            });
            alertalistado.show();
        }
    }
}
