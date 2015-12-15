package kei.magnet.utils;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import kei.magnet.R;
import kei.magnet.activities.MagnetActivity;
import kei.magnet.model.ApplicationUser;
import kei.magnet.model.Group;
import kei.magnet.model.Pin;
import kei.magnet.model.User;
import kei.magnet.task.UpdateUserTask;

/**
 * Created by carlo_000 on 24/10/2015.
 */
public class GPSHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = MagnetActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity parentActivity;
    private ApplicationUser applicationUser;
    private HashMap<Object, Pair<Marker, MarkerOptions>> markerDictionnary;
   /* private UpdateUserTask task;
    private AbstractMap.SimpleEntry<String,String> abstractMap;
    private JSONObject locationJSON;*/

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public GPSHandler(FragmentActivity parentActivity, ApplicationUser user) {

        this.parentActivity = parentActivity;
        this.applicationUser = user;
        this.markerDictionnary = new HashMap<>();
        /*locationJSON = new JSONObject();
        abstractMap = null;
        task = new UpdateUserTask(parentActivity,applicationUser.getToken());*/


        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(parentActivity.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(500);

    }

    public void rotateMap(float bearing) {
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder(googleMap.getCameraPosition()).bearing(bearing)
                .build()));
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
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            applicationUser.setLocation(new kei.magnet.model.Location(location.getLatitude(), location.getLongitude()));

            moveCamera(applicationUser.getLatLng(), 10);

            updateMarkers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMarkers() {
        googleMap.clear();

        if (MagnetActivity.selectedGroup == null) {
            for (Group group : applicationUser.getGroups()) {
                if (group != null && !group.getUsers().isEmpty()) {
                    for (User user : group.getUsers()) {
                        if (user.getId() != applicationUser.getId())
                            drawMarker(user);
                    }
                } else
                    Toast.makeText(parentActivity.getApplicationContext(), "issue when showing a group", Toast.LENGTH_LONG).show();
            }
        } else updateMarkers(MagnetActivity.selectedGroup);

        drawMarker(applicationUser);
    }

    public void updateMarkers(Group group) {
        googleMap.clear();

        for (User user : group.getUsers()) {
            if (user.getId() != applicationUser.getId())
                drawMarker(user);
        }
        for (Pin pin : group.getPins()) {
            drawPin(pin);
        }

        drawMarker(applicationUser);
    }


    public void moveCamera(LatLng location, Integer zoom) {
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(location).zoom(zoom).build()));
    }

    private void drawMarker(User user) {
        Marker marker;
        if (!markerDictionnary.containsValue(user)) {
            if (user.getLatLng() != null) {
                MarkerOptions markerOptions = new MarkerOptions().position(user.getLatLng())
                        .title(user.getLogin());

                //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin56));
                Pair<Integer, BitmapDescriptor> pair = ImagesUtils.getInstance().getFriendImage(user.getImageId());
                user.setImageId(pair.first);
                markerOptions.icon(pair.second);

                if (user.getId() != applicationUser.getId())
                    markerOptions.alpha(0.9f);
                else
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_application_user));

                marker = googleMap.addMarker(markerOptions);
                markerDictionnary.put(user, new Pair<>(marker, markerOptions));
            }
        } else {
            markerDictionnary.get(user).first.setPosition(user.getLatLng());
            markerDictionnary.get(user).second.position(user.getLatLng());
        }

    }

    public Object getValueFromHashmap(Marker marker) {
        for (Map.Entry<Object, Pair<Marker, MarkerOptions>> entry : markerDictionnary.entrySet()) {
            if (entry.getValue().first.equals(marker))
                return entry.getKey();
        }
        return null;
    }

    private void drawPin(Pin pin) {
        Marker marker;
        if (!markerDictionnary.containsValue(pin)) {
            MarkerOptions markerOptions = new MarkerOptions().position(pin.getLocation().getLatLng())
                    .title(pin.getName());


            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gps29)).alpha(0.9f);

            marker = googleMap.addMarker(markerOptions);
            markerDictionnary.put(pin, new Pair<>(marker, markerOptions));
        } else {
            markerDictionnary.get(pin).first.setPosition(pin.getLocation().getLatLng());
            markerDictionnary.get(pin).second.position(pin.getLocation().getLatLng());

        }


    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) parentActivity.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View v = null;
                    Object object;
                    if ((object = getValueFromHashmap(marker)) instanceof Pin) {
                        Pin pin = (Pin) object;
                        v = parentActivity.getLayoutInflater().inflate(R.layout.pin_infoview, null);
                        ((TextView) v.findViewById(R.id.pin_infoview_textView_PINNAME)).setText(pin.getName());
                        ((EditText) v.findViewById(R.id.pin_infoview_editText_DESCRIPTION)).setText(pin.getDescription());
                        ((EditText) v.findViewById(R.id.pin_infoview_editText_GROUP)).setText(MagnetActivity.selectedGroup.toString());

                        String expirationDate = "N/A";
                        if (pin.getDeletion_time() != null)
                            expirationDate = pin.getDeletion_time().toString();
                        ((EditText) v.findViewById(R.id.pin_infoview_editText_expirationDateText)).setText(expirationDate);

                    } else if ((object = getValueFromHashmap(marker)) instanceof User) {

                    }
                    return v;
                }
            });
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
        applicationUser.getLocation().setLatitude(location.getLatitude());
        applicationUser.getLocation().setLongitude(location.getLongitude());

        try {
            JSONObject locationJSON = new JSONObject();
            locationJSON.put("latitude", applicationUser.getLocation().getLatitude());
            locationJSON.put("longitude", applicationUser.getLocation().getLongitude());

            UpdateUserTask task = new UpdateUserTask(parentActivity, applicationUser.getToken());
            task.execute(new AbstractMap.SimpleEntry<>("location", locationJSON.toString()));

        } catch (Exception e) {

        }
        updateMarkers();
    }
}
//bou:aad97bf7214c1bb0f81af4de6f22ae4e9246cd2af3d5563221fc4e8e25c203a2
//bob:e86158e1d323b5618af99e9879a5b39dd74c77b6844411648113ccab24a21759
//alice:cd6bcc50af028d1a8f143a100822ec42ce7b70b9790c89df96713a67eb796c8b
//oscar:63dfc13530fb96c790e63bc40e0f0bf8b9fb60768354913355caa0467829f7c6