package kei.magnet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.AbstractMap;

import kei.magnet.R;
import kei.magnet.task.SignInTask;

public class SignInActivity extends AppCompatActivity {
    private EditText txtLogin;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtLogin = (EditText) findViewById(R.id.sign_in_editText_LOGIN);
        txtPassword = (EditText) findViewById(R.id.sign_in_editText_PASSWORD);
    }


    public void onClick_submit(View V) {
        SignInTask task = new SignInTask(this);
        task.execute(new AbstractMap.SimpleEntry<>("login", txtLogin.getText().toString()),
                     new AbstractMap.SimpleEntry<>("password", txtPassword.getText().toString()));
    }

    public void onClick_signUp(View v){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    //Quit the app if the user doesn't want to sign in (or it will pop a new activity to sign in).
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
