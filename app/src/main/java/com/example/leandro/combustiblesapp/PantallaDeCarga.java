package com.example.leandro.combustiblesapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class PantallaDeCarga extends AppCompatActivity {

    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    DatabaseHelper myDB;

    Button btnguardareimprimir;
    Button btnvolversinguardar;

    ArrayList<String> obtenidoacargar;
    ArrayList<Solicitudes> solicitudinfo;
    TextView patenteacargar;
    TextView litrosacargar;
    TextView operadoracargar;
    TextView lugardeentrega;
    TextView vehiculo;
    TextView obra;
    TextView solicitudacargar;
    EditText litroscargados = null;


    Solicitudes sol = new Solicitudes();

    Main_Activity ma = new Main_Activity();

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

        solicitudacargar = (TextView) findViewById(R.id.txtnsolicitudentrega);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

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
        solicitudacargar.setText(soli);

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
                final String litroscargados = ((EditText) findViewById(R.id.editlitroscargados)).getText().toString();
                final String odom = ((EditText) findViewById(R.id.editodometro)).getText().toString();

                if (litroscargados.equals("") || odom.equals("")) {
                    Toast.makeText(getApplicationContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                } else if (litroscargados.equals("0")){

                    Toast.makeText(getApplicationContext(), "Los valores no pueden ser 0", Toast.LENGTH_SHORT).show();
                }else{


                    final AlertDialog.Builder alertalistado = new AlertDialog.Builder(PantallaDeCarga.this);
                   alertalistado.setTitle("Confirma la carga de "+litroscargados+" litros");
                   alertalistado.setMessage("¿Estas seguro de realizar la carga?");
                   alertalistado.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                       @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                           SQLiteDatabase db = myDB.getWritableDatabase();
                           Cursor cursor = db.rawQuery("SELECT mascara FROM impresora",null);
                           if (cursor.moveToFirst()==true) {
                               String mascara = cursor.getString(0);

                               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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
                               NSolicitudaimprimir = solicitudacargar.getText().toString();
                               obraaimprimir = obra.getText().toString();
                               litrosasignadosaimprimir = litrosacargar.getText().toString();
                               litroscargadosaimprimir = litroscargados.toString();
                               odometroaimprimir = odom;
                               tvehiculo=vehiculo.getText().toString();
                               lugar_e=lugardeentrega.getText().toString();
                               id_est=ides.toString();
                               String nom = extras.getString("NOMBR");
                               String ape = extras.getString("APELL");

                               String entregadopor = nom+" "+ape;



                               try {
                                   dialogInterface.dismiss();

                                   ma.imprimir(fechaaimprimir,hora,patenteaimprimir,NSolicitudaimprimir,obraaimprimir,litrosasignadosaimprimir,litroscargadosaimprimir,odometroaimprimir, entregadopor,id_est,mascara);
                                   String estado="CARGADO";
                                   db.execSQL("UPDATE listado SET estado='CARGADO' WHERE solicitud='"+NSolicitudaimprimir+"' AND patente='"+patenteaimprimir+"'");
                                   db.execSQL("INSERT INTO cargado (id_estatico, solicitud, unegocio, fentrega, hentrega, patente, tipo_vehiculo, ubicacion, estado, odometro, lcargados, qcarga) VALUES ('"+id_est+"','"+NSolicitudaimprimir+"','"+obraaimprimir+"','"+fechaaimprimir+"','"+hora+"','"+patenteaimprimir+"','"+tvehiculo+"','"+lugar_e+"','"+estado+"','"+odometroaimprimir+"','"+litroscargadosaimprimir+"','"+entregadopor+"')");
                                   Intent nuevoform= new Intent(PantallaDeCarga.this, Menu.class);
                                   nuevoform.putExtra("NOMBRE",nom);
                                   nuevoform.putExtra("APELLIDO",ape);
                                   startActivity(nuevoform);

                               } catch (Exception e) {
                                   e.printStackTrace();
                               }


                           }

                }
            });

            alertalistado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
               }
             });
              alertalistado.show();
        };
            }

        });
    }
}





