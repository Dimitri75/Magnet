package kei.magnet.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.Calendar;

import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.classes.Location;
import kei.magnet.task.CreatePinTask;

public class PinCreationActivity extends FragmentActivity {
    private ApplicationUser applicationUser;
    private EditText txtName;
    private EditText txtDescription;
    private Spinner spinnerGroups;
    private Location location;
    private EditText activationDate;
    private EditText expirationDate;

    int activationYear;
    int activationMonth;
    int activationDay;

    int expirationYear;
    int expirationMonth;
    int expirationDay;

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_creation);

        if ((location = (Location) getIntent().getExtras().get("location")) == null)
            finish();

        applicationUser = ApplicationUser.getInstance();

        txtName = (EditText) findViewById(R.id.pin_creation_editText_PIN_NAME);
        txtDescription = (EditText) findViewById(R.id.pin_creation_editText_DESCRIPTION);
        spinnerGroups = (Spinner) findViewById(R.id.pin_creation_spinner_GROUP);
        activationDate = (EditText) findViewById(R.id.activationDateText);
        expirationDate = (EditText) findViewById(R.id.expirationDateText);

        final ArrayAdapter<Group> myAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                applicationUser.getGroups());
        spinnerGroups.setAdapter(myAdapter);

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        expirationYear  = c.get(Calendar.YEAR);
        expirationMonth = c.get(Calendar.MONTH);
        expirationDay = c.get(Calendar.DAY_OF_MONTH);
        expirationDate.setText(dateToString(expirationYear, expirationMonth, expirationDay));
        activationYear  = expirationYear;
        activationMonth = expirationMonth;
        activationDay = expirationDay;
        activationDate.setText(dateToString(activationYear, activationMonth, activationDay));
    }

    public void oncClick_showActivationDate(View v) {
        DialogFragment newFragment = new DatePickerFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(getActivity(), this, activationYear, activationMonth, activationDay);
            }

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                activationYear = year;
                activationMonth = month;
                activationDay = day;
                activationDate.setText(dateToString(year, month, day));
            }
        };
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void oncClick_showExpirationDate(View v) {
        DialogFragment newFragment = new DatePickerFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(getActivity(), this, expirationYear, expirationMonth, expirationDay);
            }

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                expirationYear = year;
                expirationMonth = month;
                expirationDay = day;
                expirationDate.setText(dateToString(year, month, day));
            }
        };
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onClick_submit(View V) {
        try {
            JSONObject locationJSON = new JSONObject();
            locationJSON.put("latitude", location.getLatitude());
            locationJSON.put("longitude", location.getLongitude());

            CreatePinTask task = new CreatePinTask(this, applicationUser.getToken());
            task.execute(
                    new AbstractMap.SimpleEntry<>("name", txtName.getText().toString()),
                    new AbstractMap.SimpleEntry<>("description", txtDescription.getText().toString()),
                    new AbstractMap.SimpleEntry<>("location", locationJSON.toString()),
                    new AbstractMap.SimpleEntry<>("creation_time", dateToString(activationYear, activationMonth, activationDay)),
                    new AbstractMap.SimpleEntry<>("deletion_time", dateToString(expirationYear, expirationMonth, expirationDay)),
                    new AbstractMap.SimpleEntry<>("group_id", String.valueOf(((Group) spinnerGroups.getSelectedItem()).getId()))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    private String dateToString(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-");

        if((month + 1) < 10) {
            sb.append("0");
        }
        sb.append(month + 1).append("-");

        if(day < 10) {
            sb.append("-");
        }
        sb.append(day);

        return sb.toString();
    }
}
