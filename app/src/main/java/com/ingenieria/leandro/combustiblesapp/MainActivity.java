package com.ingenieria.leandro.combustiblesapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 18-07-2019
     */

    public static final String APIUsuarios = "http://santafeinversiones.org/api/combustibles-app/usuarios";
    public static final String APISURTIDORES = "http://santafeinversiones.org/api/combustibles-app/surtidores";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private BluetoothAdapter mBluetoothAdapter;
    DatabaseHelper myDB;
    private Cursor fila;
    private Cursor fila2;
    EditText et1,et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btninicio);
        et1 =(EditText)findViewById(R.id.txtcorreo);
        et2 = (EditText) findViewById(R.id.txtclave);
        myDB = new DatabaseHelper(this);
        myDB = new DatabaseHelper(this);

      /**  mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,1);
        }**/

        btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        IniciarSesion();
                    }
                });

        descargasurtidores();
        descargausuarios();
    }

    public void descargasurtidores(){
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, APISURTIDORES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    Log.i("CONEXION SURTIDORES OK","Respuesta: "+response);
                    String json;

                    json= response.toString();
                    JSONArray jsonArray = null;
                    jsonArray = new JSONArray(json);
                    Surtidores surtidores = new Surtidores();

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        surtidores.id = jsonObject.getString("id");
                        surtidores.patente = jsonObject.getString("patente");
                        surtidores.id_comb = jsonObject.getString("id_comb");

                        SQLiteDatabase db = myDB.getWritableDatabase();

                        Cursor cursor = db.rawQuery("SELECT id,patente,id_comb FROM surtidores WHERE patente ='"+ surtidores.patente+"'",null);

                        if (cursor.getCount() <=0){
                            //EL USUARIO NO SE ENCUENTRA EN LA DB
                            myDB.RegistroSurtidores(surtidores.id,surtidores.patente,surtidores.id_comb);
                        }else{
                            if (cursor.moveToFirst() == true){
                                String id = cursor.getString(0);
                                String patente = cursor.getString(1);
                                String id_comb = cursor.getString(2);
                                    if (patente != surtidores.patente){
                                        db.execSQL("DELETE FROM surtidores WHERE patente ='"+patente+"'");
                                        myDB.RegistroSurtidores(surtidores.id,surtidores.patente,surtidores.id_comb);
                                    }
                                    if (id_comb != surtidores.id_comb){
                                        db.execSQL("DELETE FROM surtidores WHERE patente='"+patente+"'");
                                        myDB.RegistroSurtidores(surtidores.id,surtidores.patente,surtidores.id_comb);
                                    }
                            }
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR SURTIDORES:",error.toString());
            }
        });
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }

    public void IniciarSesion(){
        SQLiteDatabase db=myDB.getWritableDatabase();

        String usuario=et1.getText().toString();
        String pass=et2.getText().toString();

        fila=db.rawQuery("SELECT usuario,password,nombre,apellido FROM usuario WHERE usuario='"+usuario+"' and password='"+pass+"'",null);

        if (fila.moveToFirst()==true){

            String usua=fila.getString(0);
            String passw=fila.getString(1);

            if (usuario.equalsIgnoreCase(usua)&&pass.equalsIgnoreCase(passw)){
                String nom=fila.getString(2);
                String ape=fila.getString(3);

                SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nombre",nom);
                editor.putString("apellido",ape);
                editor.commit();
                Intent nuevoform= new Intent(MainActivity.this, Menu.class);
                startActivity(nuevoform);
                et1.setText("");
                et2.setText("");
                et1.findFocus();
            }
        } else{
            Toast.makeText(getApplicationContext(),"Usuario incorrecto",Toast.LENGTH_SHORT).show();
        }
    }



    public void descargausuarios(){
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, APIUsuarios, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    Log.i("CONEXION USUARIOS OK","Respuesta: "+response);
                    String json;

                    json= response.toString();
                    JSONArray jsonArray = null;
                    jsonArray = new JSONArray(json);
                    Usuarios usuarios = new Usuarios();

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        usuarios.id = jsonObject.getString("id");
                        usuarios.nombre = jsonObject.getString("nombre");
                        usuarios.apellido = jsonObject.getString("apellido");
                        usuarios.usuario = jsonObject.getString("usuario");
                        usuarios.password = jsonObject.getString("password");

                        SQLiteDatabase db = myDB.getWritableDatabase();

                        Cursor cursor = db.rawQuery("SELECT nombre, apellido, usuario, password FROM usuario WHERE id ='"+ usuarios.id+"'",null);

                        if (cursor.getCount() <=0){
                            //EL USUARIO NO SE ENCUENTRA EN LA DB
                            myDB.RegistroUsuarios(usuarios.id,usuarios.usuario,usuarios.nombre,usuarios.apellido,usuarios.password);
                        }else{
                            if (cursor.moveToFirst() == true){
                                String nombre = cursor.getString(0);
                                String apellido = cursor.getString(1);
                                String username = cursor.getString(2);
                                String password = cursor.getString(3);
                                if (username != usuarios.usuario){
                                    db.execSQL("UPDATE usuario SET usuario='"+usuarios.usuario.toLowerCase()+"' WHERE id='"+usuarios.id+"'");
                                }
                                if (nombre != usuarios.nombre){
                                    db.execSQL("UPDATE usuario SET nombre='"+usuarios.nombre+"' WHERE id='"+usuarios.id+"'");
                                }
                                if (apellido != usuarios.apellido){
                                    db.execSQL("UPDATE usuario SET apellido='"+usuarios.apellido+"' WHERE id='"+usuarios.id+"'");
                                }
                                if (password != usuarios.password){
                                    db.execSQL("UPDATE usuario SET password='"+usuarios.password+"' WHERE id='"+usuarios.id+"'");
                                }
                            }
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR USUARIOS:",error.toString());
            }
        });
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }

}
