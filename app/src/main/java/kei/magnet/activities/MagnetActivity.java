package kei.magnet.activities;

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

import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.classes.Location;
import kei.magnet.classes.User;
import kei.magnet.enumerations.NavigationDrawerType;
import kei.magnet.fragments.AddUserFragment;
import kei.magnet.fragments.CustomDrawerAdapter;
import kei.magnet.fragments.DrawerItem;
import kei.magnet.utils.Compass;
import kei.magnet.utils.GPSHandler;

public class MagnetActivity extends AppCompatActivity {
    private boolean isInitialised = false;
    private DrawerLayout menuLayout;
    private ActionBarDrawerToggle actionBarButtonLink;
    public GPSHandler gpsHandler;
    private Compass compass;
    private SensorManager sensorManager;
    private ApplicationUser applicationUser;
    private ListView menuList;
    private CustomDrawerAdapter customDrawerAdapter;
    private List<DrawerItem> menu_dataList;
    public static Group selectedGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnet);
        applicationUser = ApplicationUser.getInstance();

        if (applicationUser.getToken() == null) {
            Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(signInIntent);
        } else {
            init();
        }
    }

    public List<DrawerItem> formatGroupsInDataList(List<Group> groups) {
        menu_dataList = new ArrayList<>();

        Group groupZero = new Group();
        groupZero.setCreator(ApplicationUser.getInstance());
        groupZero.setName("Groupe Zero"); //TODO Nom à déterminer
        List<User> groupZeroUsers = new ArrayList<>();

        for (Group group : groups) {
            menu_dataList.add(new DrawerItem(group, NavigationDrawerType.GROUP));
            for (User user : group.getUsers()) {
                menu_dataList.add(new DrawerItem(user, NavigationDrawerType.USER));
                if (!groupZeroUsers.contains(user)) {
                    groupZeroUsers.add(user);
                }
            }
        }

        groupZero.setUsers(groupZeroUsers);
        menu_dataList.add(new DrawerItem(groupZero, NavigationDrawerType.GROUP));
        for (User user : groupZero.getUsers()) {
            menu_dataList.add(new DrawerItem(user, NavigationDrawerType.USER));
        }

        return menu_dataList;
    }


    public void init() {
        isInitialised = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass = new Compass(sensorManager, this);

        try {
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

        initMenu();
    }

    public void initMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuLayout.setDrawerListener(actionBarButtonLink);
        menuLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuList = (ListView) findViewById(R.id.left_drawer);

        actionBarButtonLink = new ActionBarDrawerToggle(this, menuLayout,
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

        menu_dataList = formatGroupsInDataList(applicationUser.getGroups());

        customDrawerAdapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                menu_dataList);
        menuList.setAdapter(customDrawerAdapter);

        menuList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isInitialised) {
            gpsHandler.onPause();
            compass.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (applicationUser.getToken() != null) {
            init();

            gpsHandler.onResume();
            compass.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isInitialised)
            actionBarButtonLink.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isInitialised)
            actionBarButtonLink.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarButtonLink.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

private class DrawerItemClickListener implements
        ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (menu_dataList.get(position).getItem() instanceof Group) {
            AddUserFragment dialog = new AddUserFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("group", (Group) menu_dataList.get(position).getItem());

            selectedGroup = (Group) menu_dataList.get(position).getItem();


            dialog.setArguments(bundle);

            dialog.show(getFragmentManager(), "Add user");
        } else
            Toast.makeText(getApplicationContext(), "Not a valid Group", Toast.LENGTH_LONG).show();
    }
}
}

