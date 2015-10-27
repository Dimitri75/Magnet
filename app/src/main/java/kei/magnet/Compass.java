package kei.magnet;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import kei.magnet.activities.MagnetActivity;

/**
 * Created by carlo_000 on 22/10/2015.
 */
public class Compass implements SensorEventListener {
    private MagnetActivity parentActivity = null;
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private Sensor mMagnetometer = null;
    private Sensor mRotVectSensor = null;

    private double angle=0;
    private float mDeclination=0;
    private float[] mRotationMatrix=new float[16];

    public Compass(SensorManager mSensorManager, MagnetActivity activity) {
        this.parentActivity = activity;
        this.mSensorManager = mSensorManager;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mRotVectSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mSensorManager.registerListener(this, mRotVectSensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
            if (Math.abs(Math.toDegrees(orientation[0]) - angle) > 0.8) {
                float bearing = (float) Math.toDegrees(orientation[0]) + mDeclination;

                parentActivity.gpsHandler.rotateMap(bearing);

            }
            angle = Math.toDegrees(orientation[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onResume() {
        mSensorManager.registerListener(this, mRotVectSensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }
}
