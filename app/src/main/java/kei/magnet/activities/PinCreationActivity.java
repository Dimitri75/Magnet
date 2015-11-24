package kei.magnet.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.AbstractMap;

import kei.magnet.JSONTask;
import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.classes.Location;

public class PinCreationActivity extends AppCompatActivity {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/pin/";
    private ApplicationUser applicationUser;
    private EditText txtName;
    private EditText txtDescription;
    private Spinner spinnerGroups;
    private DatePicker activationDate;
    private DatePicker expirationDate;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ((applicationUser = (ApplicationUser) getIntent().getExtras().get("applicationUser")) == null)
            finish();

        txtName = (EditText) findViewById(R.id.pin_creation_editText_PIN_NAME);
        txtDescription = (EditText) findViewById(R.id.pin_creation_editText_DESCRIPTION);
        activationDate = (DatePicker) findViewById(R.id.pin_creation_datePicker_ACTIVATION);
        expirationDate = (DatePicker) findViewById(R.id.pin_creation_datePicker_EXPIRATION);
        spinnerGroups = (Spinner) findViewById(R.id.pin_creation_spinner_GROUP);

        final ArrayAdapter<Group> myAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                applicationUser.getGroups());
        spinnerGroups.setAdapter(myAdapter);
    }

    public void onClick_submit(View V){
        try {
            JSONObject jsonObject = JSONTask.getTask().execute(
                    new AbstractMap.SimpleEntry<>("url", URL + applicationUser.getToken()),
                    new AbstractMap.SimpleEntry<>("method", "POST"),
                    new AbstractMap.SimpleEntry<>("request", "body"),
                    new AbstractMap.SimpleEntry<>("name", txtName.getText().toString()),
                    new AbstractMap.SimpleEntry<>("description", txtDescription.getText().toString()),
                    new AbstractMap.SimpleEntry<>("location", "location"),
                    new AbstractMap.SimpleEntry<>("creation_time", activationDate.toString()),
                    new AbstractMap.SimpleEntry<>("deletion_time", expirationDate.toString()),
                    new AbstractMap.SimpleEntry<>("group_id", "0")
            ).get();

            if (jsonObject != null)
                finish();
            else
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Connection to " + URL + " failed", Toast.LENGTH_SHORT).show();
        }
    }
}
