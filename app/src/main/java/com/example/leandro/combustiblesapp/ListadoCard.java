package com.example.leandro.combustiblesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListadoCard extends AppCompatActivity {


    DatabaseHelper myDB;
    public static TextView data;
    public static TextView solicitudes;
    public static TextView patentes;
    public static TextView asignados;
    public static TextView ubicacion;

    final static String urlsanta = "http://santafeinversiones.com/services/listado";


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();
    String camion_id;
    String fecha = dateFormat.format(date);

    Cursor cursor;
    ListView lista;
    ArrayList<String> listadeldia;
    ArrayList<solicitudes> listasolicitudes;


    solicitudes sol = new solicitudes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.listadocard);
        getSupportActionBar().hide();
       //patentes= (TextView) findViewById(R.id.patentestxt);
        //asignados= (TextView) findViewById(R.id.listrosasignados);
        //ubicacion= (TextView)findViewById(R.id.ubicacion);
        lista = (ListView)findViewById(R.id.Listacard);

        myDB = new DatabaseHelper(this);


        consultarlistadodeldia();
        ArrayAdapter adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listadeldia);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                final String nom = extras.getString("NOM");
                final String ape = extras.getString("APEL");
                String patente=""+listasolicitudes.get(pos).getPatente();
                String litros=""+listasolicitudes.get(pos).getLasignados();
                String ope=""+listasolicitudes.get(pos).getQrecibe();
                String lugarentrega=""+listasolicitudes.get(pos).getUbicacion();
                String tipov=""+listasolicitudes.get(pos).getTvehiculo();
                String obra=""+listasolicitudes.get(pos).getUnegocio();
                String s=""+listasolicitudes.get(pos).getSolicitud_id();
                String id_es=""+listasolicitudes.get(pos).getId_estatico();
                Intent nuevoform= new Intent(ListadoCard.this, PantallaDeCarga.class);
                nuevoform.putExtra("PATENTEACARGAR",patente);
                nuevoform.putExtra("NLITROS",litros);
                nuevoform.putExtra("QRECIBE",ope);
                nuevoform.putExtra("LUGARENTREGA",lugarentrega);
                nuevoform.putExtra("TIPOVEHICULO",tipov);
                nuevoform.putExtra("OBRA",obra);
                nuevoform.putExtra("S",s);
                nuevoform.putExtra("ID_ES",id_es);
                nuevoform.putExtra("NOMBR",nom);
                nuevoform.putExtra("APELL",ape);


                startActivity(nuevoform);


            }
        });


    }


    private void consultarlistadodeldia(){
        SQLiteDatabase db = myDB.getReadableDatabase();
        com.example.leandro.combustiblesapp.solicitudes sol = null;
        String estado="PENDIENTE";

        listasolicitudes= new ArrayList<solicitudes>();
        Cursor cursor = db.rawQuery("SELECT id_estatico,solicitud,unegocio, patente, lasignados,ubicacion,tipo_vehiculo,qrecibe FROM listado WHERE estado='"+estado+"'",null);

        while (cursor.moveToNext()){
            sol= new solicitudes();
            sol.setId_estatico(cursor.getString(0));
            sol.setSolicitud_id(cursor.getString(1));
            sol.setUnegocio(cursor.getString(2));
            sol.setPatente(cursor.getString(3));
            sol.setLasignados(cursor.getString(4));
            sol.setUbicacion(cursor.getString(5));
            sol.setTvehiculo(cursor.getString(6));
            sol.setQrecibe(cursor.getString(7));

            listasolicitudes.add(sol);

        }

        obtenerlistado();
    }

    private void obtenerlistado(){
        listadeldia = new ArrayList<String>();

        for (int i=0; i<listasolicitudes.size(); i++){
            listadeldia.add("Solicitud: "+listasolicitudes.get(i).getSolicitud_id()+"\n"+
                            "U.Negocio: "+listasolicitudes.get(i).getUnegocio()+"\n"+
                            "Patente: "+listasolicitudes.get(i).getPatente()+"\n"+
                            "Litros: "+listasolicitudes.get(i).getLasignados()+"\n"+
                            "Ubicacion: "+listasolicitudes.get(i).getUbicacion());

        }


    }
}
