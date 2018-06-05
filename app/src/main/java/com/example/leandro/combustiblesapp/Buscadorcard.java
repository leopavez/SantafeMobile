package com.example.leandro.combustiblesapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by LEANDRO on 22-03-2018.
 */

public class Buscadorcard extends AppCompatActivity {

    DatabaseHelper myDB;
    Button btnvolver;
    Button verlistado;
    Dialog popnopatente;
    Dialog popsipatente;

    ArrayList<String> listadeldia;
    ArrayList<solicitudes> listasolicitudes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscadorcard);
        getSupportActionBar().hide();
        myDB = new DatabaseHelper(this);
        popnopatente = new Dialog(this);
        popsipatente = new Dialog(this);
        btnvolver= findViewById(R.id.btnvolver);
        verlistado=findViewById(R.id.btnverlistado);
        final EditText solicitud;
        solicitud= (EditText)findViewById(R.id.txtsolicitud);
        ArrayAdapter adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listadeldia);

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                final String nom = extras.getString("NOM");
                final String ape = extras.getString("APEL");
                Intent nuevoform= new Intent(Buscadorcard.this, perfilmenunuevo.class);
                nuevoform.putExtra("NOMBRE",nom);
                nuevoform.putExtra("APELLIDO",ape);
                startActivity(nuevoform);
            }
        });

        verlistado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                final String nom = extras.getString("NOM");
                final String ape = extras.getString("APEL");
                Intent nuevoform= new Intent(Buscadorcard.this, ListadoCard.class);
                nuevoform.putExtra("NOM",nom);
                nuevoform.putExtra("APEL",ape);
                startActivity(nuevoform);
            }
        });

    }

    public void ShowPopup(View V) {
        EditText patente;
        patente = (EditText) findViewById(R.id.txtpatentebuscador);
        String p = patente.getText().toString().toUpperCase();
        SQLiteDatabase db = myDB.getWritableDatabase();
        String estado="PENDIENTE";

        Cursor cursor =db.rawQuery("SELECT id_estatico, solicitud,unegocio, patente, lasignados,ubicacion,tipo_vehiculo,qrecibe FROM listado WHERE patente='"+p+"' AND estado='"+estado+"'",null);

        if (p.isEmpty()) {
            Toast.makeText(getApplicationContext(),"CAMPO PATENTE VACIO",Toast.LENGTH_SHORT).show();
        } else  if(cursor.moveToFirst()==true){
            solicitudes sol = new solicitudes();
            TextView txtcerrarpopsi;
            TextView textpatentesi;
            TextView txtpatentepopup;
            TextView textnsol;
            TextView txtnsolicitudpopup;
            TextView textlts;
            TextView txtltsacargarpopup;
            TextView textlugar;
            TextView txtlugarpopup;
            Button btncancelar;
            Button btnbtncargarpopup;
            popsipatente.setContentView(R.layout.popsiregistrasolicitud);
            txtcerrarpopsi= (TextView)popsipatente.findViewById(R.id.txtcerrarpopsi);
            textpatentesi= (TextView)popsipatente.findViewById(R.id.textpatentesi);
            txtpatentepopup= (TextView)popsipatente.findViewById(R.id.txtpatentepopup);
            textnsol= (TextView)popsipatente.findViewById(R.id.textnsol);
            txtnsolicitudpopup= (TextView)popsipatente.findViewById(R.id.txtnsolicitudpopup);
            textlts= (TextView)popsipatente.findViewById(R.id.textlts);
            txtltsacargarpopup= (TextView)popsipatente.findViewById(R.id.txtltsacargarpopup);
            textlugar= (TextView)popsipatente.findViewById(R.id.textlugar);
            txtlugarpopup= (TextView)popsipatente.findViewById(R.id.txtlugarpopup);
            btncancelar = (Button)popsipatente.findViewById(R.id.btncancelar);
            btnbtncargarpopup= (Button)popsipatente.findViewById(R.id.btncargarpopup);
            sol= new solicitudes();
            sol.setId_estatico(cursor.getString(0));
            sol.setSolicitud_id(cursor.getString(1));
            sol.setUnegocio(cursor.getString(2));
            sol.setPatente(cursor.getString(3));
            sol.setLasignados(cursor.getString(4));
            sol.setUbicacion(cursor.getString(5));
            sol.setTvehiculo(cursor.getString(6));
            sol.setQrecibe(cursor.getString(7));

            txtpatentepopup.setText(sol.getPatente());
            txtnsolicitudpopup.setText(sol.getSolicitud_id());
            txtltsacargarpopup.setText(sol.getLasignados());
            txtlugarpopup.setText(sol.getUbicacion());




            txtcerrarpopsi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popsipatente.dismiss();
                }
            });
            btncancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popsipatente.dismiss();
                }
            });
            btnbtncargarpopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditText patente;
                    patente = (EditText) findViewById(R.id.txtpatentebuscador);
                    String p = patente.getText().toString().toUpperCase();
                    SQLiteDatabase db = myDB.getWritableDatabase();

                    Cursor cursor =db.rawQuery("SELECT id_estatico,solicitud,unegocio, patente, lasignados,ubicacion,tipo_vehiculo,qrecibe FROM listado WHERE patente='"+p+"'",null);
                    solicitudes sol = new solicitudes();
                    if(cursor.moveToFirst()==true){
                        sol.setId_estatico(cursor.getString(0));
                        sol.setSolicitud_id(cursor.getString(1));
                        sol.setUnegocio(cursor.getString(2));
                        sol.setPatente(cursor.getString(3));
                        sol.setLasignados(cursor.getString(4));
                        sol.setUbicacion(cursor.getString(5));
                        sol.setTvehiculo(cursor.getString(6));
                        sol.setQrecibe(cursor.getString(7));
                        Intent intent = getIntent();
                        Bundle extras = intent.getExtras();
                        final String nom = extras.getString("NOM");
                        final String ape = extras.getString("APEL");
                        String pa=""+sol.getPatente();
                        String litros=""+sol.getLasignados();
                        String ope=""+sol.getQrecibe();
                        String lugarentrega=""+sol.getUbicacion();
                        String tipov=""+sol.getTvehiculo();
                        String obra=""+sol.getUnegocio();
                        String s=""+sol.getSolicitud_id();
                        String id_e=""+sol.getId_estatico();
                        Intent nuevoform= new Intent(Buscadorcard.this, PantallaDeCarga.class);
                        nuevoform.putExtra("PATENTEACARGAR",pa);
                        nuevoform.putExtra("NLITROS",litros);
                        nuevoform.putExtra("QRECIBE",ope);
                        nuevoform.putExtra("LUGARENTREGA",lugarentrega);
                        nuevoform.putExtra("TIPOVEHICULO",tipov);
                        nuevoform.putExtra("OBRA",obra);
                        nuevoform.putExtra("S",s);
                        nuevoform.putExtra("NOMBR",nom);
                        nuevoform.putExtra("APELL",ape);
                        nuevoform.putExtra("ID_ES",id_e);
                        startActivity(nuevoform);
                    }

                }
            });
            popsipatente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popsipatente.show();

        }else{

            TextView txtclosepopup;
            Button btnok;
            TextView textpatente;
            popnopatente.setContentView(R.layout.popnoregistrasolicitud);
            txtclosepopup = (TextView) popnopatente.findViewById(R.id.txtclosepopup);
            btnok = (Button) popnopatente.findViewById(R.id.btnok);
            textpatente = (TextView) popnopatente.findViewById(R.id.textpatente);
            txtclosepopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popnopatente.dismiss();
                }
            });
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popnopatente.dismiss();
                }
            });
            popnopatente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popnopatente.show();
        }


    }

    public void onClicksolicitud(View V) {
        SQLiteDatabase db = myDB.getWritableDatabase();
        EditText solicitud;
        Button buscar;
        solicitud = (EditText) findViewById(R.id.txtsolicitud);
        buscar = (Button) findViewById(R.id.btnbuscarxsolicitud);
        String s = solicitud.getText().toString();

        Cursor cursor =db.rawQuery("SELECT id_estatico, solicitud,unegocio, patente, lasignados,ubicacion,tipo_vehiculo,qrecibe FROM listado WHERE solicitud='"+s+"'",null);

        if (s.isEmpty()){
            Toast.makeText(getApplicationContext(),"CAMPO SOLICITUD VACIO",Toast.LENGTH_SHORT).show();

        }else{
            if(cursor.moveToFirst()==true){
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                final String nom = extras.getString("NOM");
                final String ape = extras.getString("APEL");
                Intent nuevoform= new Intent(Buscadorcard.this, listadosolicitud.class);
                nuevoform.putExtra("SOLICITUDXBUSCAR",s);
                nuevoform.putExtra("NOM",nom);
                nuevoform.putExtra("APEL",ape);
                startActivity(nuevoform);
            }else{
                Toast.makeText(getApplicationContext(),"NO SE ENCONTRO LA SOLICITUD",Toast.LENGTH_SHORT).show();
            }

        }
    }





}
