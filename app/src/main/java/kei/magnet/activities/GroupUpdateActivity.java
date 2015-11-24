package kei.magnet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.task.AddUserToGroupTask;
import kei.magnet.task.JSONTask;
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
        AddUserToGroupTask task = new AddUserToGroupTask(this, applicationUser.getToken(), group.getId());
        task.execute(new AbstractMap.SimpleEntry<>("login", txtName.getText().toString()));
    }

}
