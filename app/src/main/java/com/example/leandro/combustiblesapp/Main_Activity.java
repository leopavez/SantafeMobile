package com.example.leandro.combustiblesapp;

//SCRIPT DE LA IMPRESORA TERMICA
//SCRIPT DE LA IMPRESORA TERMICA
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class Main_Activity extends Activity implements Runnable {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;


    Cursor cursor;
    DatabaseHelper myDB;
    solicitudes sol = new solicitudes();
    MainActivity principal = new MainActivity();


    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String patente = extras.getString("patente");
        final String fecha = extras.getString("fecha");
        final String hora= extras.getString("hora");
        final String solicitud = extras.getString("solicitud");
        final String obra = extras.getString("obra");
        final String litrosasignados = extras.getString("litrosasignados");
        final String litroscargados = extras.getString("litroscargados");
        final String entregadopor = extras.getString("entregadopor");
        final String odo= extras.getString("odometro");
        final String nom= extras.getString("nombresur");
        final String ape= extras.getString("apesur");
        final String tipo_v= extras.getString("tvehiculo");
        final String lentrega = extras.getString("lentrega");
        final String id_estati = extras.getString("id_esta");
        String f = fecha.toString();
        String h = hora.toString();
        String p = patente.toString().toUpperCase();
        String obr = obra.toString().toUpperCase();
        String lasig = litrosasignados.toString();
        String lentre = litroscargados.toUpperCase();
        String entre = nom.toString().toUpperCase()+" "+ape.toString().toUpperCase();
        String odom= odo.toString();
        String soli = solicitud.toString();
        String t_ve = tipo_v.toString();
        String lugar_de_entrega = lentre.toString();
        String id_estatico = id_estati.toString();
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.testimpresora);
        mScan = (Button) findViewById(R.id.button_scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(Main_Activity.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(Main_Activity.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        mPrint = (Button) findViewById(R.id.Send_Button);
        mPrint.setOnClickListener(new View.OnClickListener() {

            public void onClick(View mView) {

                        try {
                            OutputStream os = mBluetoothSocket
                                    .getOutputStream();
                            Handler mHandler= new Handler(Looper.getMainLooper());
                            Intent intent = getIntent();
                            Bundle extras = intent.getExtras();
                            final String patente = extras.getString("patente");
                            final String fecha = extras.getString("fecha");
                            final String hora= extras.getString("hora");
                            final String solicitud = extras.getString("solicitud");
                            final String obra = extras.getString("obra");
                            final String litrosasignados = extras.getString("litrosasignados");
                            final String litroscargados = extras.getString("litroscargados");
                            final String entregadopor = extras.getString("entregadopor");
                            final String odo= extras.getString("odometro");
                            final String nom= extras.getString("nombresur");
                            final String ape= extras.getString("apesur");
                            final String tipo_v= extras.getString("tvehiculo");
                            final String lentrega = extras.getString("lentrega");
                            final String id_estati = extras.getString("id_esta");

                            String f = fecha.toString();
                            String h = hora.toString();
                            String p = patente.toString().toUpperCase();
                            String obr = obra.toString().toUpperCase();
                            String lasig = litrosasignados.toString();
                            String lentre = litroscargados.toUpperCase();
                            String entre = nom.toString().toUpperCase()+" "+ape.toString().toUpperCase();
                            String odom= odo.toString();
                            String soli = solicitud.toString();
                            String t_ve = tipo_v.toString();
                            String lugar_de_entrega = lentre.toString();
                            String id_estatico = id_estati.toString();



                            String msg2 = " "+" "+" "+" "+" "+" "+" "+"Entrega petroleo "+" "+" "+" "+"\n"+
                                    " "+" "+" "+" "+" "+" "+" "+"N° Vale: "+id_estati+" "+" "+" "+"\n"+
                                    " " +"\n"+
                                    " " + "Fecha: "+ f + h +"\n"+
                                    " " + "Patente: "+ p +"\n"+
                                    " " + "Solicitud: "+ soli +"\n"+
                                    " " + "Obra: "+ obr +"\n"+
                                    " " + "L. Asignados: "+ lasig +"\n"+
                                    " " + "L. Cargados: "+ lentre +"\n"+
                                    " " + "Entregado: "+ entre+"\n"+
                                    " " + "Odometro: "+ odom+"\n"+
                                    " " +"\n"+
                                    " " +"\n"+
                                    " " + "RUT:..........................." +"\n"+
                                    " " +"\n"+
                                    " " +"\n"+
                                    " " + "Nombre:........................" +"\n"+
                                    " " +"\n"+
                                    " " +"\n"+
                                    " " + "Firma:........................." +"\n"+
                                    " " +"\n"+
                                    " " +"\n"+
                                    " "+" "+" "+" "+" "+" "+" "+"Copia departamento "+" "+" "+" "+"\n"+
                                    " " +"\n"+
                                    " " +"\n"+
                                    " " +"\n";

                            os.write(msg2.getBytes());

                                Thread.sleep(8000);

                            String msg3 = " "+" "+" "+" "+" "+" "+" "+"Entrega petroleo "+" "+" "+" "+"\n"+
                                    " "+" "+" "+" "+" "+" "+" "+"N° Vale: "+id_estati+" "+" "+" "+"\n"+
                                    " " +"\n"+
                                    " " + "Fecha: "+ f + h +"\n"+
                                    " " + "Patente: "+ p +"\n"+
                                    " " + "Solicitud: "+ soli +"\n"+
                                    " " + "Obra: "+ obr +"\n"+
                                    " " + "L. Asignados: "+ lasig +"\n"+
                                    " " + "L. Cargados: "+ lentre +"\n"+
                                    " " + "Entregado: "+ entre+"\n"+
                                    " " + "Odometro: "+ odom+"\n"+
                                    " " +"\n"+
                                    " " +"\n"+
                                    " "+" "+" "+" "+" "+" "+" "+"Copia Cliente "+" "+" "+" "+"\n"+
                                    " " +"\n"+
                                    " " +"\n";

                            os.write(msg3.getBytes());
                            onDestroy();

                            Intent nuevoform= new Intent(Main_Activity.this, perfilmenunuevo.class);
                            nuevoform.putExtra("NOMBRE",nom);
                            nuevoform.putExtra("APELLIDO",ape);
                            startActivity(nuevoform);



                            //This is printer specific code you can comment ==== > Start




                        } catch (Exception e) {
                            Log.e("MainActivity", "Exe ", e);
                        }
                    }

        });
        myDB = new DatabaseHelper(this);
        SQLiteDatabase db = myDB.getWritableDatabase();
        String estado="CARGADO";
        db.execSQL("UPDATE listado SET estado='CARGADO' WHERE solicitud='"+solicitud+"' AND patente='"+patente+"'");
        db.execSQL("INSERT INTO cargado (id_estatico, solicitud, unegocio, fentrega, hentrega, patente, tipo_vehiculo, ubicacion, estado, odometro, lcargados, qcarga) VALUES ('"+id_estatico+"','"+soli+"','"+obra+"','"+fecha+"','"+hora+"','"+p+"','"+t_ve+"','"+lugar_de_entrega+"','"+estado+"','"+odom+"','"+lentre+"','"+entre+"')");


    }// onCreate

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Conectando...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(Main_Activity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(Main_Activity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(Main_Activity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

}
