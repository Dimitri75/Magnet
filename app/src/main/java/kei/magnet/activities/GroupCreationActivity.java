package kei.magnet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.GetJSONTask;
import kei.magnet.R;

public class GroupCreationActivity extends AppCompatActivity {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClick_submit(View V) {
        try {
            JSONObject jsonObject = GetJSONTask.getInstance().execute(
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
