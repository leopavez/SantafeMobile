package com.example.leandro.combustiblesapp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by LEANDRO on 14-02-2018.
 */

public class bluetooth extends AppCompatActivity {

    private static final String TAG="bluetooth";

    Button boton;
    Button btnbuscarbluetooth;
    BluetoothAdapter mBluetoothAdapter;



    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state= intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG,"onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG,"mBroadCastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG,"mBroadCastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG,"mBroadCastReceiver1: STATE TURNING ON");
                        break;

                }

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);

        Button btnBluetooth = (Button) findViewById(R.id.botonbluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: enablig/disabling bluetooth");
                onoffBT();
            }
        });


    }

    public void onoffBT(){
        if (mBluetoothAdapter == null){
            Log.d(TAG,"onoffBT: Does not have BT capabilities.");
        }
        if (!mBluetoothAdapter.isEnabled()){
            Log.d(TAG,"onoffBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent= new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBluetoothAdapter.isEnabled()){
            Log.d(TAG,"onoffBT: disabling BT.");
             mBluetoothAdapter.disable();

            IntentFilter BTIntent= new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }
}
