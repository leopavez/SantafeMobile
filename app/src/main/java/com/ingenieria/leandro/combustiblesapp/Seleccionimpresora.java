package com.ingenieria.leandro.combustiblesapp;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Seleccionimpresora extends AppCompatActivity {

    /**
     * Proyecto CombustiblesApp
     * Ingenieria y Construcciones Santa Fe S.A
     * Creado por: Leandro Pavez
     * Fecha de actualizacion: 20-11-2018
     */

    protected static final String TAG = "TAG";
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccionimpresora);
        getSupportActionBar().hide();
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        ListView mPairedListView = (ListView) findViewById(R.id.paired_devices);
        mPairedListView.setAdapter(mPairedDevicesArrayAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
        mPairedListView.setOnItemClickListener(mDeviceClickListener);

        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                mPairedDevicesArrayAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
            }
        } else {
            String mNoDevices = "None Paired";//getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(mNoDevices);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> mAdapterView, View mView, int mPosition, long mLong) {

            String mDeviceName = ((TextView) mView).getText().toString();
            String mDeviceAddress = mDeviceName.substring(mDeviceName.length() - 17);

            ConfirmacionImpresora(mDeviceName, mDeviceAddress);

        }
    };


    public void ConfirmacionImpresora(String device, final String address) {

        AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
        confirmacion.setTitle("Confirmar dispositivo: " + device);
        confirmacion.setMessage("¿Estas seguro de vincular esta impresora?");
        confirmacion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedmascaraimpresora(address);
                Toast.makeText(getApplicationContext(), "Impresora Configurada", Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            SharedPreferences preferences = getSharedPreferences("impresora", Context.MODE_PRIVATE);
                            String mascara = preferences.getString("mascara", "");

                            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mascara);
                            mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(applicationUUID);
                            mBluetoothSocket.connect();
                            Thread.sleep(1000);
                            if (mBluetoothSocket.isConnected()) {
                                OutputStream os = mBluetoothSocket.getOutputStream();

                                String msg = " " + " " + " " + " " + " " + "IMPRESORA CONFIGURADA" + " " + "\n" +
                                        " " + "\n" +
                                        " " + "\n" +
                                        " " + "\n";
                                os.write(msg.getBytes());
                                mBluetoothSocket.close();
                            } else {
                                mBluetoothSocket.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                Intent intent = new Intent(Seleccionimpresora.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });
        confirmacion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        confirmacion.show();
    }


    private void sharedmascaraimpresora(String mascara) {

        SharedPreferences preferences = getSharedPreferences("impresora", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mascara", mascara);
        editor.commit();
    }

}
