package com.ingenieria.leandro.combustiblesapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class listadocargado extends AppCompatActivity {

    ListView listView;
    DatabaseHelper myDB;


    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    ArrayList<Cargado> listacargado;
    ArrayList<String> listacargadosdeldia;
    Cargado car = new Cargado();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.listadocargado);
        listView = (ListView)findViewById(R.id.listaview_cargado);
        myDB = new DatabaseHelper(this);

        listadosolicitudescargadas();

        ArrayAdapter adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listacargadosdeldia);
        listView.setAdapter(adaptador);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int pos, long l) {

                final AlertDialog.Builder alertalistado = new AlertDialog.Builder(listadocargado.this);
                alertalistado.setTitle("Reimprimir vale N. " + listacargado.get(pos).getId_estatico() +"");
                alertalistado.setMessage("¿Estas seguro de reimprimir?");
                alertalistado.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Main_Activity ma = new Main_Activity();
                        String solicitud = ""+listacargado.get(pos).getSolicitud();
                        String id_estatico = ""+listacargado.get(pos).getId_estatico();
                        String unegocio = ""+listacargado.get(pos).getUnegocio();
                        String patente = ""+listacargado.get(pos).getPatente();
                        String lcargados = ""+listacargado.get(pos).getLcargados();
                        String fentrega = ""+listacargado.get(pos).getFentrega();
                        String hentrega = ""+listacargado.get(pos).getHentrega();
                        try{
                            reimprimir(solicitud,id_estatico,unegocio,patente,lcargados,fentrega,hentrega);
                            Intent intent = new Intent(listadocargado.this,Menu.class);
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
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


            }
        });
    }


    public void reimprimir(final String solicitud,final String id_estatico,final String unegocio,
                           final String patente,final String lcargados,final String fentrega,
                           final String hentrega){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    SharedPreferences preferences = getSharedPreferences("impresora", Context.MODE_PRIVATE);
                    String mascara = preferences.getString("mascara", "");

                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mascara);
                    mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(applicationUUID);
                    mBluetoothSocket.connect();
                    Thread.sleep(1000);
                    if (mBluetoothSocket.isConnected()){
                        OutputStream os = mBluetoothSocket.getOutputStream();

                        String msg2 = " "+" "+" "+" "+" "+" "+" "+"Entrega petroleo "+" "+" "+" "+"\n"+
                                " "+" "+" "+" "+" "+" "+" "+"N Vale: "+id_estatico+" "+" "+" "+"\n"+
                                " " +"\n"+
                                " " + "Fecha: "+ fentrega +" "+ hentrega +"\n"+
                                " " + "Patente: "+ patente +"\n"+
                                " " + "Solicitud: "+ solicitud +"\n"+
                                " " + "Obra: "+ unegocio +"\n"+
                                " " + "L. Cargados: "+ lcargados +"\n"+
                                " " +"\n"+
                                " " +"\n"+
                                " "+" "+" "+" "+" "+" "+" "+"*Copia Reemplazo* "+" "+" "+" "+"\n"+
                                " " +"\n"+
                                " " +"\n"+
                                " " +"\n";

                        os.write(msg2.getBytes());
                        mBluetoothSocket.close();
                    }else{
                        Log.i("TAG/TT","NO CONECTADO");
                        mBluetoothSocket.close();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    private void listadosolicitudescargadas(){
        SQLiteDatabase db = myDB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        listacargado= new ArrayList<Cargado>();
        Cursor cursor = db.rawQuery("SELECT solicitud, unegocio,patente,lcargados,fentrega,hentrega,id_estatico FROM cargado WHERE fentrega='"+fecha+"'" ,null);

        while (cursor.moveToNext()){
            car = new Cargado();
            car.setSolicitud(cursor.getString(0));
            car.setUnegocio(cursor.getString(1));
            car.setPatente(cursor.getString(2));
            car.setLcargados(cursor.getString(3));
            car.setFentrega(cursor.getString(4));
            car.setHentrega(cursor.getString(5));
            car.setId_estatico(cursor.getString(6));
            listacargado.add(car);
        }

        obtenersolicitudescargadas();
    }

    private void obtenersolicitudescargadas(){
        listacargadosdeldia = new ArrayList<String>();

        for (int i=0; i<listacargado.size(); i++){
            listacargadosdeldia.add("Solicitud: "+listacargado.get(i).getSolicitud()+"\n"+
                    "N .Vale: "+listacargado.get(i).getId_estatico()+"\n"+
                    "U.Negocio: "+listacargado.get(i).getUnegocio()+"\n"+
                    "Patente: "+listacargado.get(i).getPatente()+"\n"+
                    "Litros: "+listacargado.get(i).getLcargados()+"\n"+
                    "Fecha: "+listacargado.get(i).getFentrega()+"\n"+
                    "Hora: "+listacargado.get(i).getHentrega());
        }

    }
}
