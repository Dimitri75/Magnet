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

import kei.magnet.classes.Group;
import kei.magnet.classes.Location;
import kei.magnet.classes.User;
import kei.magnet.enumerations.NavigationDrawerType;
import kei.magnet.fragments.AddUserFragment;
import kei.magnet.utils.WifiConnector;
import kei.magnet.utils.Compass;
import kei.magnet.utils.GPSHandler;
import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;
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

    private String groupURL = "http://91.121.161.11/group";


    //TODO à supprimer une fois que la récupération de groupe est fonctionnelle
//    public List<Group> getGroups(){
//        List<Group> groups = new ArrayList<>();
//
//        User creator = new User("Kae", new Location(15, 15));
//
//        List<User> usersOne = new ArrayList<>();
//        usersOne.add(new User("A", new Location(15, 15)));
//        usersOne.add(new User("B", new Location(15, 15)));
//        usersOne.add(new User("C", new Location(15, 15)));
//
//        List<User> usersTwo = new ArrayList<>();
//        usersTwo.add(new User("D", new Location(15, 15)));
//        usersTwo.add(new User("E", new Location(15, 15)));
//        usersTwo.add(new User("F", new Location(15, 15)));
//
//        List<User> usersThree = new ArrayList<>();
//        usersThree.add(new User("G", new Location(15, 15)));
//        usersThree.add(new User("H", new Location(15, 15)));
//        usersThree.add(new User("I", new Location(15, 15)));
//
//        groups.add(new Group(creator, usersOne));
//        groups.add(new Group(creator, usersTwo));
//        groups.add(new Group(creator, usersThree));
//
//        return groups;
//    }

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
        // dataList.add(new DrawerItem(true)); // adding a spinner to the list
        List<Group> groups = ApplicationUser.getInstance().getGroups();

        dataList = formatGroupsInDataList(groups);

//        dataList.add(new DrawerItem("My Favorites")); // adding a header to the list
//        dataList.add(new DrawerItem("Message"));
//        dataList.add(new DrawerItem("Likes"));
//        dataList.add(new DrawerItem("Games"));
//        dataList.add(new DrawerItem("Lables"));
//
//        dataList.add(new DrawerItem("Main Options"));// adding a header to the list
//        dataList.add(new DrawerItem("Search"));
//        dataList.add(new DrawerItem("Cloud"));
//        dataList.add(new DrawerItem("Camara"));
//        dataList.add(new DrawerItem("Video"));
//        dataList.add(new DrawerItem("Groups"));
//        dataList.add(new DrawerItem("Import & Export"));
//
//        dataList.add(new DrawerItem("Other Option")); // adding a header to the list
//        dataList.add(new DrawerItem("About"));
//        dataList.add(new DrawerItem("Settings"));
//        dataList.add(new DrawerItem("Help"));



        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

//        if (savedInstanceState == null) {
//
//            if (dataList.get(0).isSpinner() && dataList.get(1).getTitle() != null) {
//                selectItem(2);
//            } else if (dataList.get(0).getTitle() != null) {
//                selectItem(1);
//            } else {
//                selectItem(0);
//            }
//        }
    }

    public List<DrawerItem> formatGroupsInDataList(List<Group> groups){
        dataList = new ArrayList<DrawerItem>();
        for(Group group : groups){
            dataList.add(new DrawerItem(group.toString(), NavigationDrawerType.GROUP));
            for(User u : group.getUsers()){
                dataList.add(new DrawerItem(u.getLogin(), NavigationDrawerType.USER));
            }
        }
        return dataList;
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

    public void selectItem(int possition) {

        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (possition) {

            case 1:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList
                        .get(possition).getImgResID());
                break;

            case 2:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList
                        .get(possition).getImgResID());
                break;
            case 3:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 4:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 5:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList
                        .get(possition).getImgResID());
                break;
            case 7:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 8:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList
                        .get(possition).getImgResID());
                break;
            case 9:
                fragment = new FragmentTwo();
                args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 10:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 11:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList
                        .get(possition).getImgResID());
                break;
            case 12:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 14:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 15:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 16:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            default:
                break;
        }

        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.map, fragment)
                .commit();

        mDrawerList.setItemChecked(possition, true);
//        setTitle(dataList.get(possition).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);

            AddUserFragment dialog = new AddUserFragment();

            dialog.show(getFragmentManager(), "Add user");
            if (dataList.get(position).getTitle() == null) {
                selectItem(position);
            }

        }
    }
}

