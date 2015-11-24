package kei.magnet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.JSONTask;
import kei.magnet.R;

public class GroupCreationActivity extends AppCompatActivity {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/";
    private EditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtName = (EditText) findViewById(R.id.group_creation_editText_GROUPNAME);
    }

    public void onClick_submit(View V) {
        try {
            JSONObject jsonObject = JSONTask.getInstance().execute(
                    new AbstractMap.SimpleEntry<>("url", URL),
                    new AbstractMap.SimpleEntry<>("method", "POST"),
                    new AbstractMap.SimpleEntry<>("request", "slash")
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
