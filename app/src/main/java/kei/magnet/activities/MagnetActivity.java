package kei.magnet.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import kei.magnet.WifiConnector;
import kei.magnet.Compass;
import kei.magnet.GPSHandler;
import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;

public class MagnetActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    public GPSHandler gpsHandler;
    private WifiConnector wifiConnector;
    private Compass compass;
    private SensorManager mSensorManager;
    private ApplicationUser applicationUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        gpsHandler = new GPSHandler(this);
        //bluetoothConnector = new BluetoothConnector(this);
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass = new Compass(mSensorManager, this);

        try {
            if ((applicationUser = (ApplicationUser) getIntent().getExtras().get("applicationUser")) == null)
                finish();

            gpsHandler.updateMarkers(applicationUser.getGroups().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//        compass.onDestroy();
        //bluetoothConnector.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //bluetoothConnector.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

}
