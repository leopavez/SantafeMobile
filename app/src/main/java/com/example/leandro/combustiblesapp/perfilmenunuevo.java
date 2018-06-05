package com.example.leandro.combustiblesapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LEANDRO on 22-03-2018.
 */

//SCRIPT DEL MENU DE LA APLICACION

public class perfilmenunuevo extends AppCompatActivity implements View.OnClickListener{

    DatabaseHelper myDB;
    Dialog popnopatente;
    Dialog popsipatente;
    Button imprimir_btn;
    private CardView listado_Card,buscardor_Card,descargar_Card,enviardatos_Card;

    solicitudes sol = new solicitudes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SE DEFINE Y SE IDENTIFICAN LOS COMPONENTES DE LA PANTALLA DE MENU
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.perfilmenunuevo);
        listado_Card = (CardView) findViewById(R.id.listado_Card);
        buscardor_Card = (CardView) findViewById(R.id.buscador_Card);
        descargar_Card = (CardView) findViewById(R.id.descargar_Card);
        listado_Card.setOnClickListener(this);
        buscardor_Card.setOnClickListener(this);
        descargar_Card.setOnClickListener(this);
        popnopatente = new Dialog(this);
        popsipatente = new Dialog(this);
        myDB = new DatabaseHelper(this);
        AlertarListadoAtrasado();

    }

    //METODO DE SELECCION DEL MENU DE LA APLICACION
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
            case R.id.descargar_Card: i = new Intent(this,DescargarCard.class);i.putExtra("NOM",nom);i.putExtra("APEL",ape);startActivity(i);break;
            default:break;
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
            setContentView(R.layout.perfilmenunuevo);
            listado_Card= (CardView)findViewById(R.id.listado_Card);
            buscardor_Card= (CardView)findViewById(R.id.buscador_Card);
            descargar_Card = (CardView)findViewById(R.id.descargar_Card);
            listado_Card.setOnClickListener(this);
            buscardor_Card.setOnClickListener(this);
            descargar_Card.setOnClickListener(this);

            }else{
            final AlertDialog.Builder alertalistado= new AlertDialog.Builder(perfilmenunuevo.this);
            alertalistado.setTitle("Actualiza tu listado");
            alertalistado.setMessage("Tu listado no corresponde a la entrega del dia."+"\n"+"Por favor actualiza tu listado.");
            alertalistado.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = getIntent();
                    Bundle extras = intent.getExtras();
                    final String nom = extras.getString("NOMBRE");
                    final String ape = extras.getString("APELLIDO");
                    Intent nuevoform= new Intent(perfilmenunuevo.this, DescargarCard.class);
                    nuevoform.putExtra("NOM",nom);
                    nuevoform.putExtra("APEL",ape);
                    startActivity(nuevoform);
                }
            });
            alertalistado.show();
        }

        }

}
