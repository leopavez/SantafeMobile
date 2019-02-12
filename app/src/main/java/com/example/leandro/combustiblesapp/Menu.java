package com.example.leandro.combustiblesapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by LEANDRO on 22-03-2018.
 */

public class Menu extends AppCompatActivity implements View.OnClickListener{

    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 13-11-2018
     */
    private static final String TAG =MainActivity.class.getName() ;
    DatabaseHelper myDB = new DatabaseHelper(this);
    Dialog popnopatente;
    Dialog popsipatente;
    private CardView listado_Card,buscardor_Card,ajustes_Card;


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    final static String urlDescargaListado = "http://santafeinversiones.com/services/listado";
    final static String urlEnvioDatos="http://santafeinversiones.com/services/despacho/";
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
        listado_Card.setOnClickListener(this);
        buscardor_Card.setOnClickListener(this);
        ajustes_Card.setOnClickListener(this);
        popnopatente = new Dialog(this);
        popsipatente = new Dialog(this);
        myDB = new DatabaseHelper(this);
        Actualizar actualizar = new Actualizar();
        actualizar.execute();
        AlertarListadoAtrasado();

    }

    //SCRIPT QUE PERMITE LA SELECCION DEL MENU MEDIANTE CARD
    @Override
    public void onClick(View v){
        Intent i;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String nom = extras.getString("NOMBRE");
        final String ape = extras.getString("APELLIDO");
        switch (v.getId()){
            case R.id.listado_Card : i = new Intent(this,ListadoCard.class);i.putExtra("NOM",nom);i.putExtra("APEL",ape);startActivity(i);break;
            case R.id.buscador_Card: i = new Intent(this,Buscadorcard.class);i.putExtra("NOM",nom);i.putExtra("APEL",ape);startActivity(i);break;
            case R.id.ajustes_Card: i = new Intent(this,Ajustes.class);i.putExtra("NOM",nom);i.putExtra("APEL",ape);startActivity(i);break;
            default:break;
        }
    }

    public void Segundoplano() {

        try {
            Thread.sleep(120000);
            actualizacionlistado();
            enviodedatosautomatico();
            EliminarDatosEnviados();
        }catch (InterruptedException e){
            e.printStackTrace();
        }


    }

    public class Actualizar extends AsyncTask<Void,Integer,Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... voids) {
            for(int i = 1; i<=3;i++){
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


    public void actualizacionlistado(){

        SQLiteDatabase db = myDB.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_surtidor FROM surtidor",null);

        if (cursor.moveToFirst()==true){
            String idsurtidor = cursor.getString(0);
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
                        Toast.makeText(getApplicationContext(),"LISTADO ACTUALIZADO",Toast.LENGTH_SHORT).show();
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


    public void enviodedatosautomatico(){
        SQLiteDatabase db = myDB.getReadableDatabase();
        String estado = "CARGADO";

        listacargado= new ArrayList<Cargado>();
        Cursor cursor = db.rawQuery("SELECT id_estatico,fentrega,odometro, lcargados, hentrega, qcarga FROM cargado WHERE estado='"+estado+"'",null);

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


            final String id_estatico = listacargado.get(i).getId_estatico().toString();
            final String fentrega = listacargado.get(i).getFentrega().toString();
            final String horometro = listacargado.get(i).getOdometro().toString();
            final String lcargados = listacargado.get(i).getLcargados().toString();
            final String lhora = listacargado.get(i).getHentrega().toString();

            mRequestQueue = Volley.newRequestQueue(this);
            mStringRequest = new StringRequest(Request.Method.POST, urlEnvioDatos, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("MSG", "Response: " + response);

                    SQLiteDatabase db = myDB.getReadableDatabase();

                    db.execSQL("UPDATE cargado SET estado='ENVIADO' WHERE id_estatico='"+id_estatico+"'");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.i(TAG,"Error de conexion: "+error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError{

                    Map<String, String>params = new HashMap<String, String>();
                    params.put("id_estatico",id_estatico);
                    params.put("frentrega",fentrega);
                    params.put("horometro",horometro);
                    params.put("lcargados",lcargados);
                    params.put("loghora",lhora);
                    return params;

                }
            };
            mRequestQueue.add(mStringRequest);


        }


    }


    public void EliminarDatosEnviados(){

        SQLiteDatabase db = myDB.getWritableDatabase();
        String estado ="ENVIADO";
        listacargado= new ArrayList<Cargado>();
        Cursor cursor = db.rawQuery("SELECT id_estatico FROM cargado WHERE estado='"+estado+"'",null);

        while (cursor.moveToNext()){
            car = new Cargado();
            car.setId_estatico(cursor.getString(0));
            listacargado.add(car);

        }
        for (int i=0; i<listacargado.size(); i++) {

            db.execSQL("DELETE FROM cargado WHERE id_estatico="+listacargado.get(i).getId_estatico()+"");
        }

    }

    public void limpiezadatabaselistado(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        SQLiteDatabase db = myDB.getWritableDatabase();
        String fecha = dateFormat.format(date);
        listacargado= new ArrayList<Cargado>();
        Cursor cursor = db.rawQuery("SELECT * FROM cargado WHERE fentrega="+fecha+"",null);

        while (cursor.moveToNext()){
            car = new Cargado();
            car.setId_estatico(cursor.getString(0));
            listacargado.add(car);

        }
        for (int i=0; i<listacargado.size(); i++) {

            db.execSQL("DELETE FROM cargado WHERE id_estatico="+listacargado.get(i).getId_estatico()+"");
        }

    }




    public  void AlertarListadoAtrasado(){
        myDB = new DatabaseHelper(this);
        SQLiteDatabase db = myDB.getReadableDatabase();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);


        Cursor cursor=db.rawQuery("SELECT fentrega FROM listado WHERE fentrega='"+fecha+"'",null);
        if (cursor.moveToFirst()){
            setContentView(R.layout.menu);
            listado_Card= (CardView)findViewById(R.id.listado_Card);
            buscardor_Card= (CardView)findViewById(R.id.buscador_Card);
            ajustes_Card = (CardView)findViewById(R.id.ajustes_Card);
            listado_Card.setOnClickListener(this);
            buscardor_Card.setOnClickListener(this);
            ajustes_Card.setOnClickListener(this);

        }else{
            final AlertDialog.Builder alertalistado= new AlertDialog.Builder(Menu.this);
            alertalistado.setTitle("Actualiza tu listado");
            alertalistado.setMessage("Tu listado no corresponde a la entrega del dia."+"\n"+"Por favor actualiza tu listado.");
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
