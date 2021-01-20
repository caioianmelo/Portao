package com.example.portao;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.Thread;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.view.Menu;
import android.content.Intent;
import java.util.Set;
import android.handler.Handler;

public class MainActivity extends AppCompatActivity {
	private final Button btOk;
	private final BluetoothAdapter bthAdapter;
	private final Button btPair;
	private final Handler mHandler;	// Hendler for messages
	private final ConnectedThread mThread;	// Thread for message communication
	private static final INTENT_ENABLE_BTH = 1
	private final Set<BluetoothDevice> pairedBth;
	private final BluetoothDevice portaoBth;
	private final IntentFilter filterCon;
	private final BroadcastReceiver receiverBth;
	private static final string PORTAO_NAME="****";

	// variables initiation	
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	tvErr = (TextView) findViewById(R.id.tvErr);
	btOk = (Button) findViewById(R.id.btOk);
	bthAdapter = BluetoothAdapter.getDefautAdapter();
	btPair = (Button) findViewById(R.id.btPair);
	btPair.setEnabled(false);
	filterCon = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	registerReceiver(filterCon, receiverBth);

	if(!bthAdapter) tvErr.append("Bluetooth nao suportado!");
    }

    //Button pair
   btPair.setOnClickListener(View view(){
	if( !bthAdapter.isEnabled() ) {
		Intent intentEnableBth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intentEnableBth, INTENT_ENABLE_BTH);
	}
   }); 

   // Bluetooth receiver discovering devices
   receiverBth = new BroadcastReceiver() {
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(BluetoothDevice.ACTION_FOUND.equals(action)) {
			//device found
			portaoBth = intent.getParcelable(BluetoothDevice.EXTRA_DEVICE); 
			btOk.setEnabled(true);
		}
	}	
   };

   // Connecting to the device on another thread
   private class ConThread extends Thread {
	
   }
   // Filtering intent results
	@Override
 	protected void OnActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case INTENT_ENABLE_BTH:
				if(resultCode == Activity.RESULT_CANCELED) {
					tvErr.append("Too Bad!");
				}
		}
	}

	@Override
	onDestroy() {
		unregisterReceiver(bthReceiver);	
	}
}
