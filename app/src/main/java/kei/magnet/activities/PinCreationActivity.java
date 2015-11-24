package kei.magnet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.classes.Location;
import kei.magnet.task.CreatePinTask;

public class PinCreationActivity extends AppCompatActivity {
    private ApplicationUser applicationUser;
    private EditText txtName;
    private EditText txtDescription;
    private Spinner spinnerGroups;
    private DatePicker activationDate;
    private DatePicker expirationDate;
    private Location location;

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    //2015-11-24 07:18:07
    private String datePickerToString(DatePicker picker) {
        StringBuilder sb = new StringBuilder(picker.getYear());
        sb.append("-").append(picker.getMonth()).append("-").append(activationDate.getDayOfMonth());

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ((location = (Location) getIntent().getExtras().get("location")) == null)
            finish();

        applicationUser = ApplicationUser.getInstance();

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
            JSONObject locationJSON = new JSONObject();
            locationJSON.put("latitude", location.getLatitude());
            locationJSON.put("longitude", location.getLongitude());

            CreatePinTask task = new CreatePinTask(this, applicationUser.getToken());
            task.execute(
                    new AbstractMap.SimpleEntry<>("name", txtName.getText().toString()),
                    new AbstractMap.SimpleEntry<>("description", txtDescription.getText().toString()),
                    new AbstractMap.SimpleEntry<>("location", locationJSON.toString()),
                    new AbstractMap.SimpleEntry<>("creation_time", datePickerToString(activationDate)),
                    new AbstractMap.SimpleEntry<>("deletion_time", datePickerToString(expirationDate)),
                    new AbstractMap.SimpleEntry<>("group_id", String.valueOf(((Group) spinnerGroups.getSelectedItem()).getId()))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
