package kei.magnet;

import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by carlo_000 on 24/10/2015.
 */
public class GPSHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = LocationActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private GoogleMap googleMap;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity parentActivity;

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
                .setInterval(2 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(500); // 1 second, in milliseconds
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

        if (location != null)
            handleNewLocation(getLatLng(location));
    }

    private void handleNewLocation(LatLng latLng) {
        if (marker != null)
            marker.remove();

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here !");
        marker = googleMap.addMarker(markerOptions);
        marker.setPosition(latLng);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
    }

    private LatLng getLatLng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void setUpMap() {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) parentActivity.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
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
        handleNewLocation(getLatLng(location));
    }
}
