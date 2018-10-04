package sapp.hc05arduinoandroidbluetoothsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = null;
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket bluetoothSocket = null;
    private OutputStream outputStream = null;
    private String address = "98:D3:32:30:4E:EE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start Bluetooth, Connect to Device & Send Message
        initBluetooth();
        outputStream = connect(address);

        if (outputStream != null) {
            sendMessage(outputStream, "Hello World\n");
        }
    }

    /**
     * Toast out errors.
     *
     * @param message
     */
    private void toastOut(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Turn on Bluetooth if not running.
     *
     * @return
     */
    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            toastOut("Bluetooth Not Supported");
            finish();
        } else if (!bluetoothAdapter.isEnabled()) {
            toastOut("Bluetooth Not Enabled");
            Intent enableBluetoothAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetoothAdapter);
            while(!bluetoothAdapter.isEnabled());
            toastOut("Bluetooth Enabled");
        }
    }

    /**
     * Connect to a MAC Address and return OutputStream.
     *
     * @param address
     * @return
     */
    private OutputStream connect(String address) {
        if (bluetoothAdapter.isEnabled()) {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                OutputStream outputStream = bluetoothSocket.getOutputStream();
                return outputStream;
            } catch (Exception e) {
                e.printStackTrace();
                toastOut("Error Connecting");
            }
        } else toastOut("Enable Bluetooth");

        return null;
    }

    /**
     * Send message to outputstream.
     *
     * @param message
     * @return
     */
    private void sendMessage(OutputStream outputStream, String message) {
        try {
            if (outputStream != null) outputStream.write((message).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            toastOut("Error Sending Message");
        }
    }
}
