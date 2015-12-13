package kei.magnet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.AbstractMap;

import kei.magnet.R;
import kei.magnet.model.ApplicationUser;
import kei.magnet.task.CreatePinTask;

public class GroupCreationActivity extends AppCompatActivity {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/group/";
    private ApplicationUser applicationUser;
    private EditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        applicationUser = ApplicationUser.getInstance();
        
        txtName = (EditText) findViewById(R.id.group_creation_editText_GROUPNAME);
    }

    public void onClick_submit(View V) {
        CreatePinTask task = new CreatePinTask(this, applicationUser.getToken());
        task.execute(new AbstractMap.SimpleEntry<>("name", txtName.getText().toString()));
    }
}
