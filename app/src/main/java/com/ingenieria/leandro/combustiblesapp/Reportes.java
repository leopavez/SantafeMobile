package com.ingenieria.leandro.combustiblesapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class Reportes extends AppCompatActivity implements View.OnClickListener {

    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 22-07-2019
     */

    CardView miscargas, resumen;
    DatabaseHelper myDB = new DatabaseHelper(this);

    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    private BluetoothAdapter mBluetoothAdapter;
    Menu menu = new Menu();
    ArrayList<Cargado> listacargado;
    Cargado car = new Cargado();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.reportescard);
        miscargas = (CardView) findViewById(R.id.mis_cargas);
        resumen = (CardView) findViewById(R.id.resumen_diario_card);
        miscargas.setOnClickListener(this);
        resumen.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.mis_cargas:
                i = new Intent(Reportes.this, listadocargado.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.resumen_diario_card:
                try{
                    resumen();
                    Thread.sleep(3000);
                    Enviodedatos();
                    i = new Intent(Reportes.this, Menu.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
    }


    public void resumen() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        SQLiteDatabase db = myDB.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT solicitud, unegocio,patente,lcargados,fentrega,hentrega,id_estatico FROM cargado WHERE fentrega='" + fecha + "'", null);

        if (cursor.moveToFirst() == true) {
            SharedPreferences preferences = getSharedPreferences("impresora", Context.MODE_PRIVATE);
            final String mascara = preferences.getString("mascara", "");

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mascara);
                        mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(applicationUUID);
                        mBluetoothSocket.connect();
                        Thread.sleep(1000);
                        if (mBluetoothSocket.isConnected()) {
                            OutputStream outputStream = mBluetoothSocket.getOutputStream();

                            int total_litros = 0;

                            for (int i = 0; i <= cursor.getCount(); cursor.moveToNext()) {

                                i = i + 1;
                                String solicitud = cursor.getString(0);
                                String unegocio = cursor.getString(1);
                                String patente = cursor.getString(2);
                                String lcargados = cursor.getString(3);
                                String fentrega = cursor.getString(4);
                                String hentrega = cursor.getString(5);
                                String numvale = cursor.getString(6);
                                int lts = Integer.parseInt(lcargados);

                                total_litros = total_litros + lts;

                                if (i == 1) {
                                    String msg = " " + " " + " " + " " + " " + " " + " " + "Combustibles Santa Fe " + " " + "\n" +
                                            " " + " " + " " + " " + " " + " " + " " + " " + "Fecha: " + fentrega + " " + " " + " " + "\n" +
                                            " " + "\n" +
                                            " " + "N.Vale  " + "Patente  "+ "Lts  "+"Solicitud"+"\n"+
                                            " " + numvale+"   "+patente+"     "+lcargados+"    "+solicitud+"\n";
                                    try {
                                        outputStream.write(msg.getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (i > 1 && i <= cursor.getCount()) {
                                    String msg2 =" " + numvale+"   "+patente+"     "+lcargados+"    "+solicitud+"\n";
                                    try {
                                        outputStream.write(msg2.getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (i == cursor.getCount()) {
                                    String msg3 =" " + " " + " " + "" + " " + " " + " " + " " + " " + "" + " " + "\n" +
                                            " " + " " + " " + "" + " " + " " + " " + " " + " " + " " + " " + " " + " " + " Resumen: " + " " + " " + "" + " " + "\n" +
                                            " " + " " + " " + "" + " " + " " + " " + " " + " " + "" + " " + "\n" +
                                            " " + "Total de litros: " + total_litros + "\n" +
                                            " " + "\n" +
                                            " " + " " +  "Ingenieria y Const. Santa Fe " + " " + " " + "\n" +
                                            " " + "\n" +
                                            " " + "\n" +
                                            " " + "\n";
                                    try {
                                        outputStream.write(msg3.getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            mBluetoothSocket.close();
                        } else {
                            mBluetoothSocket.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }


    private void Enviodedatos() {
        menu.enviodedatosautomatico();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fechaactual = dateFormat.format(date);

        SQLiteDatabase db = myDB.getWritableDatabase();
        String estado = "ENVIADO";
        listacargado = new ArrayList<Cargado>();
        Cursor cursor = db.rawQuery("SELECT id_estatico,fentrega FROM cargado WHERE estado='" + estado + "'", null);

        while (cursor.moveToNext()) {
            String fecha =cursor.getString(1);
            if (fechaactual.equals(fecha)){

            }else{
                car = new Cargado();
                car.setId_estatico(cursor.getString(0));
                listacargado.add(car);
            }
        }
        for (int i = 0; i < listacargado.size(); i++) {
            db.execSQL("DELETE FROM cargado WHERE id_estatico=" + listacargado.get(i).getId_estatico() + "");
        }

    }
}
