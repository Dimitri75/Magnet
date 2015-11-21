package kei.magnet;

import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import kei.magnet.activities.MagnetActivity;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.classes.User;

/**
 * Created by carlo_000 on 24/10/2015.
 */
public class GPSHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = MagnetActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private GoogleMap googleMap;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity parentActivity;
    private ApplicationUser applicationUser;


    protected int mDpi = 0;

    public GPSHandler(FragmentActivity parentActivity) {

        this.parentActivity = parentActivity;
        /*compass = new Compass(m,view);
        view.compass=compass;*/

        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(parentActivity.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10)        // 10 seconds, in milliseconds
                .setFastestInterval(1); // 1 second, in milliseconds

        mDpi = parentActivity.getResources().getDisplayMetrics().densityDpi;
    }

    public void rotateMap(float bearing){

        CameraPosition pos = CameraPosition.builder(googleMap.getCameraPosition()).bearing(bearing)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }

    public void onPause() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        //m.unregisterListener(compass);
    }

    public void onResume( ) {
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
        //m.registerListener(compass, m.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        //m.registerListener(compass, m.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
    }



    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        if (location != null){
            handleNewLocation(getLatLng(location));
        }
    }

    public void updateMarkers(Group group){
        googleMap.clear();
        for (User user : group.getUsers()){
            Bitmap icon = BitmapFactory.decodeResource(parentActivity.getResources(), R.drawable.map_marker);
            icon = Bitmap.createScaledBitmap(icon, icon.getWidth()/ 10, icon.getHeight() / 10, true);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude()))
                    .title(user.getLogin())
                    .icon(BitmapDescriptorFactory.fromBitmap(icon));
            googleMap.addMarker(markerOptions);
        }
        handleNewLocation(applicationUser.getLatLng());
    }
    protected Bitmap adjustImage(Bitmap image) {
        int dpi = image.getDensity();
        if (dpi == mDpi)
            return image;
        else {
            int width = (image.getWidth() * mDpi + dpi / 2) / dpi;
            int height = (image.getHeight() * mDpi + dpi / 2) / dpi;
            Bitmap adjustedImage = Bitmap.createScaledBitmap(image, width/100, height/100, true);
            adjustedImage.setDensity(mDpi);
            return adjustedImage;
        }
    }
    private void handleNewLocation(LatLng latLng) {
        if (marker != null)
            marker.remove();

        Bitmap icon = BitmapFactory.decodeResource(parentActivity.getResources(), R.drawable.map_marker);
        icon = Bitmap.createScaledBitmap(icon, icon.getWidth()/ 10, icon.getHeight() / 10, true);

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I am here !")
                .icon(BitmapDescriptorFactory.fromBitmap(icon));
        marker = googleMap.addMarker(markerOptions);
        marker.setPosition(latLng);


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
    }

    private LatLng getLatLng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) parentActivity.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // Check if we were successful in obtaining the map.

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(parentActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (applicationUser == null || applicationUser.getLocation() == null)
            return;
        applicationUser.setLocation(new kei.magnet.classes.Location(location.getLatitude(), location.getLongitude()));
        handleNewLocation(getLatLng(location));
    }
}
