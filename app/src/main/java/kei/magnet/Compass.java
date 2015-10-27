package kei.magnet;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by carlo_000 on 22/10/2015.
 */
public class Compass implements SensorEventListener {
    private LocationActivity parentActivity = null;
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private Sensor mMagnetometer = null;
    private Sensor mRotVectSensor = null;

    /*private int n = 0;
    private float[] mLastAccelerometer = new float[3];
    private float[] mPreviousAccelerometer = null;

    private float[] mLastMagnetometer = new float[3];
    private float[] mPreviousMagnetometer = null;

    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];

    private Activity parentActivity;


    static final float ALPHA = 1.5f;

    public float[] result = new float[2];*/


    private double angle=0;
    private float mDeclination=0;
    private float[] mRotationMatrix=new float[16];

    public Compass(SensorManager mSensorManager, LocationActivity activity) {
        this.parentActivity = activity;
        this.mSensorManager = mSensorManager;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mRotVectSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mSensorManager.registerListener(this, mRotVectSensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        /*if (event.sensor == mAccelerometer) {
            mPreviousAccelerometer = mLastAccelerometer;
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometer = MathUtils.lowPassFilter(mPreviousAccelerometer, mLastAccelerometer, ALPHA);
            mLastAccelerometerSet = true;


        } else if (event.sensor == mMagnetometer) {
            mPreviousMagnetometer = mLastMagnetometer;
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;

            mLastMagnetometer = MathUtils.lowPassFilter(mPreviousMagnetometer, mLastMagnetometer, ALPHA);

        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {


            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);


            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;





        }*/

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

    public void getRotationValue() {

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

    public void onStop() {

    }

    public void onDestroy() {

    }


}
