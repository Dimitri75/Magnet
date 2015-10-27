package kei.magnet;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LocationActivity extends FragmentActivity {
    public GPSHandler gpsHandler;
    private BluetoothConnector bluetoothConnector;
    private Compass compass;
    private SensorManager mSensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        gpsHandler = new GPSHandler(this);
        //bluetoothConnector = new BluetoothConnector(this);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        compass = new Compass(mSensorManager,this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsHandler.onPause();
        compass.onPause();
        //bluetoothConnector.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpsHandler.onResume();
        compass.onResume();
        //bluetoothConnector.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compass.onDestroy();
        //bluetoothConnector.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //bluetoothConnector.onActivityResult(requestCode,resultCode,data);

    }


}
