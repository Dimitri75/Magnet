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
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;

public class GroupUpdateActivity extends AppCompatActivity {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/";
    private ApplicationUser applicationUser;
    private EditText txtName;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_update);

        if ((group = (Group) getIntent().getExtras().get("group")) == null)
            finish();

        applicationUser = ApplicationUser.getInstance();

        txtName = (EditText) findViewById(R.id.group_creation_editText_GROUPNAME);
    }

    public void onClick_submit(View V) {
        try {
            JSONObject jsonUser = JSONTask.getTask().execute(
                    new AbstractMap.SimpleEntry<>("url", URL + "user/" +  txtName.getText().toString()),
                    new AbstractMap.SimpleEntry<>("method", "GET"),
                    new AbstractMap.SimpleEntry<>("request", "slash")
            ).get();

            if (jsonUser != null) {
                JSONObject jsonObject = JSONTask.getTask().execute(
                        new AbstractMap.SimpleEntry<>("url", URL + "user/" + group.getId() + "/user/" + applicationUser.getToken()),
                        new AbstractMap.SimpleEntry<>("method", "POST"),
                        new AbstractMap.SimpleEntry<>("request", "body"),
                        new AbstractMap.SimpleEntry<>("id_user", String.valueOf(group.getId()))
                ).get();

                if (jsonObject != null)
                    finish();
                else
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
