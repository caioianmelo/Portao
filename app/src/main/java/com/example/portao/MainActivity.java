package com.example.portao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.view.Menu;
import android.content.Intent;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
	private TextView tvErr; 
	private Button btOk;
	private EditText edtText;
	private BluetoothAdapter bthAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	tvErr = (TextView) findViewById(R.id.tvErr);
	btOk = (Button) findViewById(R.id.btOk);
	edtText = (EditText) findViewById(R.id.edtText);
	bthAdapter = BluetoothAdapter.getDefautAdapter();

	if(!bthAdapter) tvErr.append("Bluetooth nao suportado!");
    }

    
}
