package kei.magnet.activities;

import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/user/";
    private EditText txtLogin;
    private EditText txtPassword;
    private EditText txtPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtLogin = (EditText) findViewById(R.id.sign_up_editText_LOGIN);
        txtPassword = (EditText) findViewById(R.id.sign_up_editText_PASSWORD);
        txtPasswordConfirmation = (EditText) findViewById(R.id.sign_up_editText_PASSWORD2);
    }

    public void onClick_submit(View V) {
        if (txtPassword.getText().toString().equals(txtPasswordConfirmation.getText().toString())) {
            try {
                JSONObject jsonUser = JSONTask.getInstance().execute(
                        new AbstractMap.SimpleEntry<>("url", URL),
                        new AbstractMap.SimpleEntry<>("method", "POST"),
                        new AbstractMap.SimpleEntry<>("request", "body"),
                        new AbstractMap.SimpleEntry<>("login", txtLogin.getText().toString()),
                        new AbstractMap.SimpleEntry<>("password", txtPassword.getText().toString())
                ).get();

                if (jsonUser != null) {
                        JSONObject jsonToken = JSONTask.getInstance().execute(
                                new AbstractMap.SimpleEntry<>("url", URL),
                                new AbstractMap.SimpleEntry<>("method", "GET"),
                                new AbstractMap.SimpleEntry<>("request", "slash"),
                                new AbstractMap.SimpleEntry<>("login", txtLogin.getText().toString()),
                                new AbstractMap.SimpleEntry<>("password", txtPassword.getText().toString())
                        ).get();

                    ApplicationUser applicationUser = new ApplicationUser(jsonUser);
                    Intent intent = new Intent(this, MagnetActivity.class);
                    intent.putExtra("applicationUser", applicationUser);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Connection to " + URL + " failed", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getApplicationContext(), "The passwords don't match.", Toast.LENGTH_SHORT).show();
    }
}
