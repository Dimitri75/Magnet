package kei.magnet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;

public class MainActivity extends AppCompatActivity {
    private static String serverURL = "http://91.121.161.11/magnet/user"; //TODO à changer
    private EditText txtLogin;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtPassword = (EditText) findViewById(R.id.txtLogin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkLogin(View V){
        try {
            JSONObject jsonObject = new GetJSONTask().execute(
                    new AbstractMap.SimpleEntry<>("url", serverURL),
                    new AbstractMap.SimpleEntry<>("method", "GET"),
                    new AbstractMap.SimpleEntry<>("request", "slash"),
                    new AbstractMap.SimpleEntry<>("login", txtLogin.getText().toString()),
                    new AbstractMap.SimpleEntry<>("password", txtPassword.getText().toString())
            ).get();

            if(jsonObject != null){
                Intent intent = new Intent(this, MagnetActivity.class);
                startActivity(intent);
            }
            else
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Connection to " + serverURL + " failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View V){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}