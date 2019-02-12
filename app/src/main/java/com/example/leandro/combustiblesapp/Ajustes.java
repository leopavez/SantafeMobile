package com.example.leandro.combustiblesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class Ajustes extends AppCompatActivity {

    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 13-11-2018
     */

    private static final String TAG =DescargarCard.class.getName() ;
    DatabaseHelper myDB;
    Button seleccionsurtidor;
    Button seleccionimpresora;
    Spinner simplespinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);
        getSupportActionBar().hide();
        seleccionsurtidor = (Button)findViewById(R.id.btnseleccionsurtidor);
        seleccionimpresora = (Button)findViewById(R.id.btnseleccionimpresora);

        seleccionsurtidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vistasurtidor();
            }
        });

        seleccionimpresora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vistaimpresora();
            }
        });



    }

    private void vistasurtidor(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String nom = extras.getString("NOM");
        final String ape = extras.getString("APEL");
        Intent nuevoform= new Intent(Ajustes.this, Seleccionsurtidor.class);
        nuevoform.putExtra("NOMBRE",nom);
        nuevoform.putExtra("APELLIDO",ape);
        startActivity(nuevoform);
    }

    private void vistaimpresora(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String nom = extras.getString("NOM");
        final String ape = extras.getString("APEL");
        Intent nuevoform= new Intent(Ajustes.this, Seleccionimpresora.class);
        nuevoform.putExtra("NOMBRE",nom);
        nuevoform.putExtra("APELLIDO",ape);
        startActivity(nuevoform);
    }
}
