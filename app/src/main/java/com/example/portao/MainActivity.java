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
import java.io.IOException;
import java.io.OuputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
	private final Button btOk;
	private final BluetoothAdapter bthAdapter;
	private final Button btConn;
	private final Handler mHandler;	// Hendler for messages
	private final ConnectedThread mThread;	// Thread for message communication
	private static final int INTENT_ENABLE_BTH = 1
	private static final string UUID = "2b338a7b-c423-4595-a878-b4d20beb5853"
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
	btConn = (Button) findViewById(R.id.btConn);
	btConn.setEnabled(false);
	filterCon = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	registerReceiver(filterCon, receiverBth);

	if(!bthAdapter) tvErr.append("Bluetooth nao suportado!");
    	// Button pair
	else {
		btConn.setOnClickListener(View view(){
			@Override
			public void onClick(view) {
				if( !bthAdapter.isEnabled() ) {
					Intent intentEnableBth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(intentEnableBth, INTENT_ENABLE_BTH);
				}
			}
		}); 

		btOk.setOnCLickListener(View view() {
			@Override
			public void onClick(view) {
				if(ThredComm) {
					ConnectedThread.write('1');
				}	
				else tvErr.append("Thread nao inicializada!");
			} 
		});
	}
    }

   // Bluetooth pairing intent
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
   private class ConnThread extends Thread {
	private final BluetoothSocket btCommSocket;
	private final BluetoothDevice btDevice;

	public ConnThread(BluetoothDevice dev) {
		//Temporary socket for connection testing
		BluetoothSocket tmp = null;
		btDevice = device;

		
		try {
			tmp = device.createRfcommSocketToServiceRecord(UUID);	
		} catch (IOException e) {
			Log.e(TAG, "Socket not created!", e);
		}

		btCommSocket = tmp;
	}	

	public void run() {
		//Too much load
		bluetoothAdapter.cancelDiscovery();

		try {
			btCommSocket.connect();
		} catch (IOException connError) {
			try {
				btCommSocket.close();
			} catch (IOException closeErr) {
				Log.e(TAG, "Couldn't close the socket", closeErr);
			}	
		return;
		}
	}

	public void cancel() {
		try {
			btCommSocket.close();
		} catch (IOException e) {
			Log.e("TAG", "Could not close the socket", e);
		}
	}
   }

   // Thread send that message myboy
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final OutputStream mmOutputStream;
		private final InputStream mmInputStream;
		private byte[] message;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket socket;
			OutputStream tmpOutput = null;
			InputStream tmpInput = null;

			try {
				tmpInput = socket.getInputStream();
			} catch (IOException e) {
				Log.e(TAG, "Error creating stream", e);
			}
			try {
				tmpOutput = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "Error creating stream", e);
			}
			
			mmInputStream = tmpInput;
			mmOutputStream = tmpOutput;
		}

		public run() {
			mmBuffer = new byte[1024];
			int numBytes;	// recebidos pelo inputStream

			while ( true ) {
				try {
					SystemClock.sleep(100);
					numBytes = mmInputStream.read(message);
				} catch (IOException e) {
					Log.e(TAG, "Input Disconnected!", e);	
				}
			}
		}

		public void write(char liga) {
			byte[] byteTransf = liga.getBytes();

			try {
				mmOutStream.write(byteTranf);
			} catch (IOException e) {
				Log.e(TAG, "Could not send the message!", e);
			}
		}	

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "Could not close the socket!", e);
			}
		}
	} 

   // Filtering intent results
	@Override
 	protected void OnActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case INTENT_ENABLE_BTH:
				if(resultCode == Activity.RESULT_CANCELED) {
					tvErr.append("Too Bad!");
				}
				else {
					ConnThread.run();
					tvErr.append("YOOOOLLOOOOOO!!!!");
					btOK.setEnabled(true);
				}
		}
	}

	@Override
	onDestroy() {
		unregisterReceiver(bthReceiver);
		ConnThread.cancel();
		ConnectedThread.cancel();
	}
}
