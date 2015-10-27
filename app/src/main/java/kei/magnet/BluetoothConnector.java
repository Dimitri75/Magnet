package kei.magnet;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by carlo_000 on 25/10/2015.
 */
public class BluetoothConnector {

    private FragmentActivity parentActivity;

    private int REQUEST_ENABLE_BT= Activity.RESULT_CANCELED;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> devices;

    public AcceptThread serverThread;
    public ConnectThread clientThread;

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(parentActivity, "New Device = " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };



    public BluetoothConnector(FragmentActivity parentActivity) {
        this.parentActivity = parentActivity;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(parentActivity, "Pas de Bluetooth",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(parentActivity, "Avec Bluetooth",
                    Toast.LENGTH_SHORT).show();
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                parentActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                Toast.makeText(parentActivity, "Activation du bluetooth",
                        Toast.LENGTH_SHORT).show();

            }
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            parentActivity.registerReceiver(bluetoothReceiver, filter);
            mBluetoothAdapter.startDiscovery();

            serverThread= new AcceptThread();
            serverThread.run();

        }



    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != REQUEST_ENABLE_BT)
            return;
        if (resultCode == parentActivity.RESULT_OK) {
            // L'utilisation a activé le bluetooth
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
            showKnownDevices();
        } else {

        }
    }

    public void showKnownDevices(){
        devices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice blueDevice : devices) {
            Toast.makeText(parentActivity, "Device = " + blueDevice.getName(), Toast.LENGTH_SHORT).show();
        }
    }


    public void makeDeviceVisible(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        parentActivity.startActivity(discoverableIntent);
    }


    public void playClientRole(){

    }

    public void playServerRole(){
        AcceptThread thread = new AcceptThread();
        thread.run();
    }
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
            manageConnectedSocket(mmSocket);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }

    }
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("magnet", UUID.randomUUID());
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }

                if (socket != null) {
                    manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }


    public void manageConnectedSocket(BluetoothSocket socket){
        Toast.makeText(parentActivity.getApplicationContext(),"connected",Toast.LENGTH_LONG);

    }



    public void onResume(){

    }

    public void onPause(){
        serverThread.cancel();
    }

    public void onDestroy(){
        mBluetoothAdapter.cancelDiscovery();
        parentActivity.unregisterReceiver(bluetoothReceiver);
    }
}
