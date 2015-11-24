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
import kei.magnet.task.SignUpTask;

public class SignUpActivity extends AppCompatActivity {
    private EditText txtLogin;
    private EditText txtPassword;
    private EditText txtPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtLogin = (EditText) findViewById(R.id.sign_up_editText_LOGIN);
        txtPassword = (EditText) findViewById(R.id.sign_up_editText_PASSWORD);
        txtPasswordConfirmation = (EditText) findViewById(R.id.sign_up_editText_PASSWORD2);
    }

    public void onClick_submit(View V) {
        if (txtPassword.getText().toString().equals(txtPasswordConfirmation.getText().toString())) {
            SignUpTask task = new SignUpTask(this);
            task.execute(new AbstractMap.SimpleEntry<>("login", txtLogin.getText().toString()),
                         new AbstractMap.SimpleEntry<>("password", txtPassword.getText().toString()));
        }
    }
}
