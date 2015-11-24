package kei.magnet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.JSONTask;
import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;

public class PinCreationActivity extends AppCompatActivity {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/";
    private ApplicationUser applicationUser;
    private EditText txtName;
    private EditText txtDescription;
    private DatePicker activationDate;
    private DatePicker expirationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtName = (EditText) findViewById(R.id.pin_creation_editText_PIN_NAME);
        txtDescription = (EditText) findViewById(R.id.pin_creation_editText_DESCRITPION);
        activationDate = (DatePicker) findViewById(R.id.pin_creation_datePicker_ACTIVATION);
        expirationDate = (DatePicker) findViewById(R.id.pin_creation_datePicker_EXPIRATION);
    }

    public void onClick_submit(View V) {
        try {
            JSONObject jsonObject = JSONTask.getInstance().execute(
                    new AbstractMap.SimpleEntry<>("url", URL),
                    new AbstractMap.SimpleEntry<>("method", "POST"),
                    new AbstractMap.SimpleEntry<>("request", "body")
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
