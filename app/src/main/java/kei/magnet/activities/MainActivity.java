package kei.magnet.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;
import java.net.URL;

import kei.magnet.LocationActivity;
import kei.magnet.R;

public class MainActivity extends AppCompatActivity {

    private static String serverURL = "http://192.168.42.170:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
            /*JSONObject jsonObject = new GetUserTask().execute(new URL(serverURL)).get();
            System.out.println("Finished");*/

            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        }catch(Exception e){
            System.out.println("Connection to " + serverURL + " failed");
        }
    }

    public void signUp(View V){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}