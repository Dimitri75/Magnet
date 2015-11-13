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
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.Set;
import java.util.UUID;

/**
 * Created by carlo_000 on 25/10/2015.
 */
public class WifiConnector {

    private FragmentActivity parentActivity;


    private final IntentFilter intentFilter = new IntentFilter();


    WifiP2pManager.Channel mChannel;
    WifiP2pManager mManager;

    public WifiConnector(FragmentActivity parentActivity) {
        this.parentActivity = parentActivity;

        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) parentActivity.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(parentActivity, parentActivity.getMainLooper(), null);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    public void showKnownDevices(){

    }


    public void makeDeviceVisible(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        parentActivity.startActivity(discoverableIntent);
    }









    public void manageConnectedSocket(BluetoothSocket socket){
        Toast.makeText(parentActivity.getApplicationContext(),"connected",Toast.LENGTH_LONG);
    }



    public void onResume(){

    }

    public void onPause(){

    }

    public void onDestroy(){


    }
}
