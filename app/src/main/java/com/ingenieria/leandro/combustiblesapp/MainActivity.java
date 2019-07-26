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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 18-07-2019
     */
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

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,1);
        }

        btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        IniciarSesion();
                    }
                });

    }

    public void crearusuario(){

        SQLiteDatabase db = myDB.getReadableDatabase();

        db.execSQL("INSERT INTO usuario values(?,'jtorrealba','12345','JUAN','TORREALBA')");
        db.execSQL("INSERT INTO usuario values(?,'jsanmartin','12345','JUAN','SAN MARTIN')");
        db.execSQL("INSERT INTO usuario values(?,'allancaman','12345','ARNOLDO','LLANCAMAN')");
        db.execSQL("INSERT INTO usuario values(?,'bparraguez','12345','BLAS','PARRAGUEZ')");
        db.execSQL("INSERT INTO usuario values(?,'paravena','12345','PATRICIO','ARAVENA')");

    }

    public void IniciarSesion(){
        SQLiteDatabase db=myDB.getWritableDatabase();

        String usuario=et1.getText().toString();
        String pass=et2.getText().toString();

        fila=db.rawQuery("SELECT usuario,password,nombre,apellido FROM usuario WHERE usuario='"+usuario+"' and password='"+pass+"'",null);

        if (fila.moveToFirst()==true){

            String usua=fila.getString(0);
            String passw=fila.getString(1);

            if (usuario.equals(usua)&&pass.equals(passw)){
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

}
