package kei.magnet.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import kei.magnet.R;
import kei.magnet.enumerations.NavigationDrawerType;
import kei.magnet.fragments.AddUserFragment;
import kei.magnet.task.CreateGroupTask;
import kei.magnet.utils.CallBack;
import kei.magnet.utils.CustomDrawerAdapter;
import kei.magnet.utils.DrawerItem;
import kei.magnet.model.ApplicationUser;
import kei.magnet.model.Group;
import kei.magnet.model.Location;
import kei.magnet.model.User;
import kei.magnet.task.GetUserTask;
import kei.magnet.task.RemoveUserFromGroupTask;
import kei.magnet.task.UpdateUserTask;
import kei.magnet.utils.Compass;
import kei.magnet.utils.GPSHandler;

public class MagnetActivity extends AppCompatActivity {
    private static String FILENAME = "magnet_token";
    public static Integer GROUPE_CREATED = 1;
    private static Integer TOKEN_SIZE = 64;

    public boolean isInitialised = false;
    private DrawerLayout menuLayout;
    private ActionBarDrawerToggle actionBarButtonLink;
    public GPSHandler gpsHandler;
    private Compass compass;
    private SensorManager sensorManager;
    private ApplicationUser applicationUser;
    private ListView menuList;
    private CustomDrawerAdapter customDrawerAdapter;
    private List<DrawerItem> menuDataList;
    public static Group selectedGroup;
    private MenuItem userItem;
    private Switch switchPrivate;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnet);

        applicationUser = ApplicationUser.getInstance();

        if (applicationUser.getToken() == null) {
            String token = null;
            try {
                byte[] buffer = new byte[TOKEN_SIZE];
                FileInputStream fis = openFileInput(FILENAME);
                if(fis.read(buffer, 0, TOKEN_SIZE) != -1) {
                    token = new String(buffer, "UTF-8");
                }
                fis.close();
            }
            catch(Exception e) {}

            if(token != null) {

                try {
                    GetUserTask task = new GetUserTask(this);
                    task.execute(new AbstractMap.SimpleEntry<>("token", token)).get();
                }
                catch(Exception e) {}
                init();
            }
            else {

                Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInIntent);
            }
        }

        switchPrivate = (Switch)findViewById(R.id.switch_private);
        switchPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int visible = 1;
                if(isChecked) {
                    visible = 0;
                }
                applicationUser.setVisible(visible);
                UpdateUserTask task = new UpdateUserTask(null, applicationUser.getToken());
                task.execute(new AbstractMap.SimpleEntry<>("visible", String.valueOf(applicationUser.getVisible())));
            }
        });
    }

    public List<DrawerItem> formatGroupsInDataList(List<Group> groups) {
        menuDataList = new ArrayList<>();

        Group globalGroup = new Group();
        globalGroup.setCreator(ApplicationUser.getInstance());
        globalGroup.setName("Friend list");
        List<User> globalGroupUsers = new ArrayList<>();

        menuDataList.add(new DrawerItem("Create Group", NavigationDrawerType.BUTTONGROUP, null));

        for (Group group : groups) {
            menuDataList.add(new DrawerItem(group, NavigationDrawerType.GROUP, group));
            for (User user : group.getUsers()) {
                if (user.getId() != applicationUser.getId()){
                    menuDataList.add(new DrawerItem(user, NavigationDrawerType.USER, group));

                    if (!globalGroupUsers.contains(user)) {
                        globalGroupUsers.add(user);
                    }
                }

            }
        }
        globalGroup.setUsers(globalGroupUsers);

        menuDataList.add(new DrawerItem(globalGroup, NavigationDrawerType.GROUP,null));
        for (User user : globalGroup.getUsers()) {
            if (user.getId() != applicationUser.getId())
                menuDataList.add(new DrawerItem(user, NavigationDrawerType.USER, null));
        }

        return menuDataList;
    }


    public void init() {
        isInitialised = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass = new Compass(sensorManager, this);

        ((Switch) findViewById(R.id.switch_private)).setSelected(ApplicationUser.getInstance().getVisible() != 1);

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
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    try{
                        //do your code here
                        //also call the same runnable
                        updateApplicationUser();
                        gpsHandler.updateMarkers();

                    }
                    catch (Exception e) {
                        // TODO: handle exception
                    }
                    finally{
                        //also call the same runnable
                        handler.postDelayed(this, 6000);
                    }
                }
            };
            handler.postDelayed(runnable, 6000);

        } catch (Exception e) {
            isInitialised = false;
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
        menuList = (ListView) findViewById(R.id.left_drawer_listview);

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

        menuDataList = formatGroupsInDataList(applicationUser.getGroups());

        customDrawerAdapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                menuDataList);
        menuList.setAdapter(customDrawerAdapter);

        menuList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuDataList.get(position).getItem() instanceof Group) {
                    selectedGroup = (Group) menuDataList.get(position).getItem();
                    view.setSelected(true);
                    gpsHandler.updateMarkers(selectedGroup);
                }
                else if (menuDataList.get(position).getItem() instanceof User) {
                    User selectedUser = (User) menuDataList.get(position).getItem();
                    gpsHandler.moveCamera(selectedUser.getLatLng(), 10);
                }
            }
        });

        menuList.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuDataList.get(position).getItem() instanceof Group) {
                    selectedGroup = (Group) menuDataList.get(position).getItem();

                    AddUserFragment dialog = new AddUserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("group", selectedGroup);
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "Add user");
                } else if (menuDataList.get(position).getItem() instanceof User) {
                    final int itemPosition = position;
                    final DrawerItem item = menuDataList.get(position);
                    final User user = (User) item.getItem();
                    new AlertDialog.Builder(MagnetActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Removing User from Group")
                            .setMessage("Are you sure you want to remove this user from this group?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //marche pas on dirait
                                    RemoveUserFromGroupTask task = new RemoveUserFromGroupTask(getParent(), applicationUser.getToken(), item.getGroup().getId());
                                    task.execute(new AbstractMap.SimpleEntry<>("login", user.getLogin()));
                                    item.getGroup().getUsers().remove(user);
                                    updateMenu();
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();

                }

                return true;
            }
        });
    }


    public void updateApplicationUser(){
        try {
            GetUserTask task = new GetUserTask(this);
            task.execute(new AbstractMap.SimpleEntry<>("token", ApplicationUser.getInstance().getToken())).get();
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void updateMenu() {
        menuDataList = formatGroupsInDataList(applicationUser.getGroups());

        customDrawerAdapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                menuDataList);
        menuList.setAdapter(customDrawerAdapter);

    }

    public void onClick_createGroup(View V) {
        Intent intent = new Intent(getApplicationContext(), GroupCreationActivity.class);
        startActivityForResult(intent, GROUPE_CREATED);
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
        if (isInitialised) {
            gpsHandler.onResume();
            compass.onResume();
        } else if (applicationUser.getToken() != null) {
            init();
        }
        switchPrivate.setChecked(applicationUser.getVisible() == 1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isInitialised) {
            gpsHandler.onResume();
            compass.onResume();
        } else if (applicationUser.getToken() != null) {
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GROUPE_CREATED && resultCode == Activity.RESULT_OK) {
            String jsonString = data.getDataString();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                Group group = new Group(jsonObject);
                applicationUser.getGroups().add(group);
                updateMenu();
            }
            catch(Exception e) {}
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        userItem = menu.findItem(R.id.action_user);
        userItem.setTitle(applicationUser.getLogin());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarButtonLink.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_log_out:
                applicationUser.setToken(null);
                try {
                    FileOutputStream fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write("".getBytes());
                }
                catch(Exception e) {}
                Intent signInIntent = new Intent(this.getApplicationContext(), SignInActivity.class);
                this.startActivity(signInIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public List<DrawerItem> getMenuDataList() {
        return menuDataList;
    }
}

