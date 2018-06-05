package com.example.leandro.combustiblesapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class PantallaDeCarga extends AppCompatActivity {
    DatabaseHelper myDB;

    Button btnguardareimprimir;
    Button btnvolversinguardar;

    ArrayList<String> obtenidoacargar;
    ArrayList<solicitudes> solicitudinfo;
    TextView patenteacargar;
    TextView litrosacargar;
    TextView operadoracargar;
    TextView lugardeentrega;
    TextView vehiculo;
    TextView obra;
    TextView solicitud;
    EditText litroscargados = null;

    solicitudes sol = new solicitudes();

    //CODIGO PRINCIPAL DE LA PANTALLA DE CARGA EN LA CUAL SE INGRESA LOS LITROS CARGADOS Y SE ENVIA A IMPRIMIR
    //EL VAUCHER PARA CLIENTE Y SURTIDOR

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalladecarga);
        getSupportActionBar().hide();
        patenteacargar = (TextView) findViewById(R.id.txtpatenteentrega);

        litrosacargar = (TextView) findViewById(R.id.txtlitrosacargarentrega);

        operadoracargar = (TextView) findViewById(R.id.txtoperador);

        lugardeentrega = (TextView) findViewById(R.id.txtlugarentrega);

        vehiculo = (TextView) findViewById(R.id.txttipovehiculo);

        obra = (TextView) findViewById(R.id.txtobra);

        solicitud = (TextView) findViewById(R.id.txtnsolicitudentrega);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String p = extras.getString("PATENTEACARGAR");
            String nlitros = extras.getString("NLITROS");
            String qrecibe = extras.getString("QRECIBE");
            String lentrega = extras.getString("LUGARENTREGA");
            String tvehiculo = extras.getString("TIPOVEHICULO");
            String obraa = extras.getString("OBRA");
            String soli = extras.getString("S");

            patenteacargar.setText(p);
            litrosacargar.setText(nlitros);
            operadoracargar.setText(qrecibe);
            lugardeentrega.setText(lentrega);
            vehiculo.setText(tvehiculo);
            obra.setText(obraa);
            solicitud.setText(soli);
        }

        myDB = new DatabaseHelper(this);

        btnvolversinguardar = (Button) findViewById(R.id.btnvolversinguardar);
        btnguardareimprimir = (Button) findViewById(R.id.btnguardareimprimir);
        btnvolversinguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnguardareimprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String lit = ((EditText) findViewById(R.id.editlitroscargados)).getText().toString();
                final String odom = ((EditText) findViewById(R.id.editodometro)).getText().toString();

                if (lit.equals("") && odom.equals("")) {
                    Toast.makeText(getApplicationContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                } else {

                    final AlertDialog.Builder alertalistado = new AlertDialog.Builder(PantallaDeCarga.this);
                    alertalistado.setTitle("Confirma la carga de "+lit+" litros");
                    alertalistado.setMessage("¿Estas seguro de realizar la carga?");
                    alertalistado.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            SimpleDateFormat horaformat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                            Date date = new Date();
                            Intent intent = getIntent();
                            Bundle extras = intent.getExtras();
                            String ides= extras.getString("ID_ES");
                            String patenteaimprimir;
                            String fechaaimprimir;
                            String hora;
                            String NSolicitudaimprimir;
                            String obraaimprimir;
                            String litrosasignadosaimprimir;
                            String litroscargadosaimprimir;
                            String entregadoporaimprimir;
                            String odometroaimprimir;
                            String tvehiculo;
                            String lugar_e;
                            String id_est;
                            //OBTENEMOS LOS DATOS A IMPRIMIR
                            patenteaimprimir = patenteacargar.getText().toString();
                            fechaaimprimir = dateFormat.format(date);
                            hora = horaformat.format(date);
                            NSolicitudaimprimir = solicitud.getText().toString();
                            obraaimprimir = obra.getText().toString();
                            litrosasignadosaimprimir = litrosacargar.getText().toString();
                            litroscargadosaimprimir = lit;
                            odometroaimprimir = odom;
                            tvehiculo=vehiculo.getText().toString();
                            lugar_e=lugardeentrega.getText().toString();
                            id_est=ides.toString();
                            String nom = extras.getString("NOMBR");
                            String ape = extras.getString("APELL");
                            Intent nuevoform = new Intent(PantallaDeCarga.this, Main_Activity.class);
                            nuevoform.putExtra("patente", patenteaimprimir);
                            nuevoform.putExtra("fecha", fechaaimprimir);
                            nuevoform.putExtra("hora",hora);
                            nuevoform.putExtra("solicitud", NSolicitudaimprimir);
                            nuevoform.putExtra("obra", obraaimprimir);
                            nuevoform.putExtra("litrosasignados", litrosasignadosaimprimir);
                            nuevoform.putExtra("litroscargados", litroscargadosaimprimir);
                            nuevoform.putExtra("odometro", odometroaimprimir);
                            nuevoform.putExtra("nombresur", nom);
                            nuevoform.putExtra("apesur", ape);
                            nuevoform.putExtra("qrecibe",operadoracargar.toString());
                            nuevoform.putExtra("tvehiculo",tvehiculo);
                            nuevoform.putExtra("lentrega",lugar_e);
                            nuevoform.putExtra("id_esta",id_est);
                            startActivity(nuevoform);

                        }
                    });

                    alertalistado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertalistado.show();
                }
            }
        });
    }
}




