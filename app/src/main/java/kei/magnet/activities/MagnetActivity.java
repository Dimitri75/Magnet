package kei.magnet.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import kei.magnet.utils.WifiConnector;
import kei.magnet.utils.Compass;
import kei.magnet.utils.GPSHandler;
import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Location;
import kei.magnet.fragments.AddUserFragment;
import kei.magnet.fragments.CustomDrawerAdapter;
import kei.magnet.fragments.DrawerItem;
import kei.magnet.fragments.FragmentOne;
import kei.magnet.fragments.FragmentThree;
import kei.magnet.fragments.FragmentTwo;

public class MagnetActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    public GPSHandler gpsHandler;
    private WifiConnector wifiConnector;
    private Compass compass;
    private SensorManager mSensorManager;
    private ApplicationUser applicationUser;

    // peut-être à changer
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CustomDrawerAdapter adapter;
    private List<DrawerItem> dataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        applicationUser = ApplicationUser.getInstance();
        Toast.makeText(this, ((Boolean) (applicationUser.getGroups().isEmpty())).toString(), Toast.LENGTH_LONG).show();
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

        //bluetoothConnector = new BluetoothConnector(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass = new Compass(mSensorManager, this);

        try {
            //Toast.makeText(this, applicationUser.getGroups().get(0).getCreator().getLogin(),Toast.LENGTH_LONG).show();
            gpsHandler = new GPSHandler(this, applicationUser);
            gpsHandler.getGoogleMap().setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Intent intent = new Intent(getApplicationContext(), PinCreationActivity.class);
                    intent.putExtra("location", new Location(latLng));
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initializing
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.magnet, GravityCompat.START);

        // Add Drawer Item to dataList
        // Add Drawer Item to dataList
        dataList.add(new DrawerItem("Message", R.drawable.plus81));
        dataList.add(new DrawerItem("Likes", R.drawable.plus81));
        dataList.add(new DrawerItem("Games", R.drawable.plus81));
        dataList.add(new DrawerItem("Lables", R.drawable.plus81));
        dataList.add(new DrawerItem("Search", R.drawable.plus81));
        dataList.add(new DrawerItem("Cloud", R.drawable.plus81));
        dataList.add(new DrawerItem("Camara", R.drawable.plus81));
        dataList.add(new DrawerItem("Video", R.drawable.plus81));
        dataList.add(new DrawerItem("Groups", R.drawable.plus81));
        dataList.add(new DrawerItem("Import & Export", R.drawable.plus81));
        dataList.add(new DrawerItem("About", R.drawable.plus81));
        dataList.add(new DrawerItem("Settings", R.drawable.plus81));
        dataList.add(new DrawerItem("Help", R.drawable.plus81));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
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

    public void SelectItem(int position) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 1:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 2:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 3:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 4:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 5:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 6:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 7:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 8:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 9:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 10:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 11:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            case 12:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
            default:
                break;
        }

        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.map, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(dataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);

            AddUserFragment dialog = new AddUserFragment();
            dialog.show(getFragmentManager(), "Add user");

        }
    }
}

