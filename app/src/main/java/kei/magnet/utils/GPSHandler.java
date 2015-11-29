package kei.magnet.utils;

import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

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

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.List;

import kei.magnet.R;
import kei.magnet.activities.MagnetActivity;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.classes.User;
import kei.magnet.task.UpdateUserTask;

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

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public GPSHandler(FragmentActivity parentActivity, ApplicationUser user) {

        this.parentActivity = parentActivity;
        this.applicationUser = user;
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
                .setInterval(10*1000)        // 2 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
    }

    public void rotateMap(float bearing) {

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
    }

    public void onResume() {
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        applicationUser.setLocation(new kei.magnet.classes.Location(location.getLatitude(), location.getLongitude()));
        CameraPosition pos = CameraPosition.builder().target(applicationUser.getLatLng()).zoom(10).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
        updateMarkers(true);
    }

    public void updateMarkers(boolean isAppUsrRequest) {
        googleMap.clear();

        List<Group> groups = applicationUser.getGroups();
        drawMarker(applicationUser);

        //if (!isAppUsrRequest) {
            for (Group group : groups) {

                if (group != null && group.getUsers() != null) {
                    if(MagnetActivity.selectedGroup==null || MagnetActivity.selectedGroup.getId()==group.getId()) {
                        for (User user : group.getUsers()) {
                            drawMarker(user);
                        }
                    }

                } else {
                    Toast.makeText(parentActivity.getApplicationContext(),"issue when showing a group",Toast.LENGTH_LONG).show();
                }
            }
        //}

    }

    private void drawMarker(User user) {
        MarkerOptions markerOptions = new MarkerOptions().position(user.getLatLng())
                .title(user.getLogin());
        Bitmap userIcon = BitmapFactory.decodeResource(parentActivity.getResources(), R.drawable.pin56);
        if (user instanceof ApplicationUser)
            userIcon = BitmapFactory.decodeResource(parentActivity.getResources(), R.drawable.map_marker_application_user);

        userIcon = Bitmap.createScaledBitmap(userIcon, userIcon.getWidth() / 10, userIcon.getHeight() / 10, false);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(userIcon));

        googleMap.addMarker(markerOptions);
    }

    private LatLng getLatLng(Location location) {
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
        if (applicationUser == null || applicationUser.getLocation() == null || location == null)
            return;
        applicationUser.setLocation(new kei.magnet.classes.Location(location.getLatitude(), location.getLongitude()));

        try {
            JSONObject locationJSON = new JSONObject();
            locationJSON.put("latitude", applicationUser.getLocation().getLatitude());
            locationJSON.put("longitude", applicationUser.getLocation().getLongitude());

            UpdateUserTask task = new UpdateUserTask(parentActivity, applicationUser.getToken());
            task.execute(new AbstractMap.SimpleEntry<>("location", locationJSON.toString()));
        }
        catch(Exception e) {}

        updateMarkers(true);

    }
}
//bou:aad97bf7214c1bb0f81af4de6f22ae4e9246cd2af3d5563221fc4e8e25c203a2
//bob:e86158e1d323b5618af99e9879a5b39dd74c77b6844411648113ccab24a21759
//alice:cd6bcc50af028d1a8f143a100822ec42ce7b70b9790c89df96713a67eb796c8b
//oscar:63dfc13530fb96c790e63bc40e0f0bf8b9fb60768354913355caa0467829f7c6