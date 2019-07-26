package com.ingenieria.leandro.combustiblesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class Ajustes extends AppCompatActivity implements View.OnClickListener {

    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 18-07-2019
     */

    private static final String TAG =DescargarCard.class.getName() ;
    private CardView seleccion_impresora,seleccion_surtidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);
        getSupportActionBar().hide();
        seleccion_impresora = (CardView)findViewById(R.id.ajustesImpresoraCard);
        seleccion_surtidor = (CardView)findViewById(R.id.AjustesSurtidorCard);

        seleccion_surtidor.setOnClickListener(this);
        seleccion_impresora.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.ajustesImpresoraCard :
                i = new Intent(Ajustes.this,Seleccionimpresora.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.AjustesSurtidorCard :
                i = new Intent(Ajustes.this,Seleccionsurtidor.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
