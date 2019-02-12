package com.example.leandro.combustiblesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
     * Fecha de actualizacion: 13-11-2018
     */

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

        fila=db.rawQuery("SELECT usuario,password FROM usuario WHERE usuario='"+usuario+"' and password='"+pass+"'",null);
        fila2=db.rawQuery("SELECT nombre,apellido FROM usuario WHERE usuario='"+usuario+"'",null);

        if (fila.moveToFirst()==true){

            String usua=fila.getString(0);
            String passw=fila.getString(1);

            if (usuario.equals(usua)&&pass.equals(passw)){
                if (fila2.moveToFirst()==true){
                    String nom=fila2.getString(0);
                    String ape=fila2.getString(1);
                    Intent nuevoform= new Intent(MainActivity.this, Menu.class);
                    nuevoform.putExtra("NOMBRE",nom);
                    nuevoform.putExtra("APELLIDO",ape);
                    startActivity(nuevoform);
                }


                et1.setText("");
                et2.setText("");
                et1.findFocus();
            }
        } else{
            Toast.makeText(getApplicationContext(),"Usuario incorrecto",Toast.LENGTH_SHORT).show();
        }
    }

}
