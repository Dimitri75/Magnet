package com.example.suiken.magnetclient.asynctask;

import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserServiceCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Suiken on 26/10/2015.
 */
public class GetUserTask extends AsyncTask<URL, Void, JSONObject>{

    protected JSONObject doInBackground(URL... url) {
        JSONObject jsonObject = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url[0].openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream stream = connection.getInputStream();

            jsonObject = new JSONObject(convertStreamToString(stream));

        }catch(Exception e){
            System.out.println("Connection to " + url[0].toString() + " failed");
        }
        return jsonObject;
    }

    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
