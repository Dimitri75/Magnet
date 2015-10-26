package kei.magnet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by carlo_000 on 21/10/2015.
 */
public class LocationView extends View {
    public Bitmap img = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.chouette));
    private Paint mPaint;
    private float mFontSize;
    private SensorManager m;
    public Compass compass;
    private Canvas canvas;
    public LocationView(final Context context,Compass compass) {
        super(context);
        m = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mFontSize = 25 * getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(mFontSize);



    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        float a = 16 * getResources().getDisplayMetrics().density;
        this.canvas = canvas;
        canvas.drawCircle(200, 100, a, mPaint);
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);

        Matrix matrix = new Matrix();
        matrix.postRotate(compass.result[1],img.getWidth()/2,img.getHeight()/2);
        canvas.drawBitmap(img, matrix, mPaint);



// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                canvas.drawText("latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude(), 200, 200, mPaint);
                invalidate();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    }


}
