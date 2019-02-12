package com.example.leandro.combustiblesapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

public class Seleccionimpresora extends AppCompatActivity {

    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 20-11-2018
     */

    Spinner spinnerimpresora;
    private boolean isFirst;
    Button guardarimpresora;
    DatabaseHelper myDB;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String TAG =MainActivity.class.getName() ;


    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccionimpresora);
        getSupportActionBar().hide();

        spinnerimpresora = (Spinner)findViewById(R.id.spinnerimpresora);
        guardarimpresora = (Button)findViewById(R.id.btnguardarimpresora);

        myDB = new DatabaseHelper(this);

        final List<String> listaimpresoras = new ArrayList<String>();
        listaimpresoras.add("Selecciona");
        listaimpresoras.add("IMP-1");
        listaimpresoras.add("IMP-2");
        listaimpresoras.add("IMP-3");
        listaimpresoras.add("IMP-4");
        listaimpresoras.add("IMP-5");

        final ArrayAdapter<String> simpleAdapter = new ArrayAdapter<>(Seleccionimpresora.this, R.layout.support_simple_spinner_dropdown_item, listaimpresoras);
        spinnerimpresora.setAdapter(simpleAdapter);
        spinnerimpresora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                if (isFirst){
                    isFirst=false;
                }else{
                    if (listaimpresoras.get(i)=="Selecciona"){
                        guardarimpresora.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Selecciona una patente valida", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        if (listaimpresoras.get(i)=="IMP-1"){
                            guardarimpresora.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = getIntent();
                                    Bundle extras = intent.getExtras();
                                    final String nom = extras.getString("NOM");
                                    final String ape = extras.getString("APEL");
                                    String address="66:12:DB:2A:3F:FA";
                                    SQLiteDatabase db = myDB.getWritableDatabase();
                                    db.execSQL("DELETE FROM impresora");
                                    myDB.insertarImpresoraMask(address);
                                    Toast.makeText(getApplicationContext(), "IMPRESORA CONFIGURADA: "+address, Toast.LENGTH_SHORT).show();
                                    Intent nuevoform= new Intent(Seleccionimpresora.this, Menu.class);
                                    nuevoform.putExtra("NOMBRE",nom);
                                    nuevoform.putExtra("APELLIDO",ape);
                                    startActivity(nuevoform);
                                }
                            });

                        }else if (listaimpresoras.get(i)=="IMP-2"){
                            guardarimpresora.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = getIntent();
                                    Bundle extras = intent.getExtras();
                                    final String nom = extras.getString("NOM");
                                    final String ape = extras.getString("APEL");
                                    String address="66:12:04:0B:54:EF";
                                    SQLiteDatabase db = myDB.getWritableDatabase();
                                    db.execSQL("DELETE FROM impresora");
                                    myDB.insertarImpresoraMask(address);
                                    Toast.makeText(getApplicationContext(), "IMPRESORA CONFIGURADA: "+address, Toast.LENGTH_SHORT).show();
                                    Intent nuevoform= new Intent(Seleccionimpresora.this, Menu.class);
                                    nuevoform.putExtra("NOMBRE",nom);
                                    nuevoform.putExtra("APELLIDO",ape);
                                    startActivity(nuevoform);
                                }
                            });
                        }else if(listaimpresoras.get(i)=="IMP-3"){
                            guardarimpresora.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = getIntent();
                                    Bundle extras = intent.getExtras();
                                    final String nom = extras.getString("NOM");
                                    final String ape = extras.getString("APEL");
                                    String address="0F:02:17:A2:19:9F";
                                    SQLiteDatabase db = myDB.getWritableDatabase();
                                    db.execSQL("DELETE FROM impresora");
                                    myDB.insertarImpresoraMask(address);
                                    Toast.makeText(getApplicationContext(), "IMPRESORA CONFIGURADA: "+address, Toast.LENGTH_SHORT).show();
                                    Intent nuevoform= new Intent(Seleccionimpresora.this, Menu.class);
                                    nuevoform.putExtra("NOMBRE",nom);
                                    nuevoform.putExtra("APELLIDO",ape);
                                    startActivity(nuevoform);
                                }
                            });
                        }else if(listaimpresoras.get(i)=="IMP-4"){
                            guardarimpresora.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = getIntent();
                                    Bundle extras = intent.getExtras();
                                    final String nom = extras.getString("NOM");
                                    final String ape = extras.getString("APEL");
                                    String address="0F:02:17:B1:60:35";
                                    SQLiteDatabase db = myDB.getWritableDatabase();
                                    db.execSQL("DELETE FROM impresora");
                                    myDB.insertarImpresoraMask(address);
                                    Toast.makeText(getApplicationContext(), "IMPRESORA CONFIGURADA: "+address, Toast.LENGTH_SHORT).show();
                                    Intent nuevoform= new Intent(Seleccionimpresora.this, Menu.class);
                                    nuevoform.putExtra("NOMBRE",nom);
                                    nuevoform.putExtra("APELLIDO",ape);
                                    startActivity(nuevoform);
                                }
                            });

                        }else if(listaimpresoras.get(i)=="IMP-5"){
                            guardarimpresora.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = getIntent();
                                    Bundle extras = intent.getExtras();
                                    final String nom = extras.getString("NOM");
                                    final String ape = extras.getString("APEL");
                                    String address="0F:02:17:A1:76:0B";
                                    SQLiteDatabase db = myDB.getWritableDatabase();
                                    db.execSQL("DELETE FROM impresora");
                                    myDB.insertarImpresoraMask(address);
                                    Toast.makeText(getApplicationContext(), "IMPRESORA CONFIGURADA: "+address, Toast.LENGTH_SHORT).show();
                                    Intent nuevoform= new Intent(Seleccionimpresora.this, Menu.class);
                                    nuevoform.putExtra("NOMBRE",nom);
                                    nuevoform.putExtra("APELLIDO",ape);
                                    startActivity(nuevoform);
                                }
                            });

                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }
}
