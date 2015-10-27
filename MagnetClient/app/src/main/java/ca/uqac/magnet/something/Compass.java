package ca.uqac.magnet.something;

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
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private Sensor mMagnetometer = null;
    private LocationView compassView = null;
    private int n = 0;
    private float[] mLastAccelerometer = new float[3];
    private float[] mPreviousAccelerometer = null;

    private float[] mLastMagnetometer = new float[3];
    private float[] mPreviousMagnetometer = null;

    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float smoothDegree=0;
    private float mCurrentDegree = 0f;

    static final float ALPHA = 1.5f;

    public float[] result=new float[2];

    public Compass(SensorManager mSensorManager,LocationView view) {
        this.mSensorManager = mSensorManager;
        this.compassView = view;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        result[0]=0;
        result[1]=1;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(n==10){

            result[0] = mCurrentDegree;
            result[1] = (smoothDegree/n)%360;
            smoothDegree=0;
            n=0;

        }else{
            if (event.sensor == mAccelerometer) {
                mPreviousAccelerometer=mLastAccelerometer;
                System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                mLastAccelerometer = MathUtils.lowPassFilter(mPreviousAccelerometer,mLastAccelerometer,ALPHA);
                mLastAccelerometerSet = true;



            } else if (event.sensor == mMagnetometer) {
                mPreviousMagnetometer=mLastMagnetometer;
                System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
                mLastMagnetometerSet = true;

                mLastMagnetometer = MathUtils.lowPassFilter(mPreviousMagnetometer,mLastMagnetometer,ALPHA);

            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {


                SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
                SensorManager.getOrientation(mR, mOrientation);


                float azimuthInRadians = mOrientation[0];
                float azimuthInDegrees = (float)(Math.toDegrees(azimuthInRadians)+360)%360;




                mCurrentDegree = -azimuthInDegrees;



                smoothDegree+=mCurrentDegree;
                n++;
                compassView.invalidate();
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




}
