package kei.magnet.activities;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.util.Pair;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap;

/**
 * Created by Suiken on 26/10/2015.
 */
public class GetJSONTask extends AsyncTask<AbstractMap.SimpleEntry<String, String>, Void, JSONObject>{

    /**
     *
     * @param entries : entries of String. The first entry represents the url where the task has to connect, the second entry represents the method of the request (POST or GET), the third entry tells if you are using the slashes or the body of the request
     * @return response of the request
     */
    protected JSONObject doInBackground(AbstractMap.SimpleEntry<String, String>... entries) {
        JSONObject jsonObject = null;
        try {
            if(entries[2].getValue().equals("slash")){
                jsonObject = getSlashJSONObject(entries);
            }else if(entries[2].getValue().equals("body")){
                jsonObject = getJSONObject(entries);
            }
        }catch(Exception e){
            System.out.println("Connection to " + entries[0].getValue() + " failed");
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

    private JSONObject getJSONObject(AbstractMap.SimpleEntry<String, String>... entries){
        JSONObject jsonObject = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("/" + entries[0].getValue()).openConnection();
            connection.setRequestMethod(entries[1].getValue().toString());
            connection.setDoInput(true);

            Uri.Builder builder = new Uri.Builder();
            for (int i = 2; i < entries.length; i++) {
                builder.appendQueryParameter(entries[i].getKey(), entries[i].getValue());
            }
            String query = builder.build().getEncodedQuery();

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            InputStream stream = connection.getInputStream();

            jsonObject = new JSONObject(convertStreamToString(stream));
        }catch(Exception e){
            System.out.println("Connection failed");
        }
        return jsonObject;
    }

    private JSONObject getSlashJSONObject(AbstractMap.SimpleEntry<String, String>... entries){
        JSONObject jsonObject = null;
        try {

            StringBuilder sb = new StringBuilder();
            sb.append(entries[0].getValue());
            for(int i = 3; i < entries.length; i++){
                sb.append("/");
                sb.append(entries[i].getValue());
            }

            HttpURLConnection connection = (HttpURLConnection) new URL(sb.toString()).openConnection();
            connection.setRequestMethod(entries[1].getValue().toString());
            connection.setDoInput(true);

            connection.connect();

            InputStream stream = connection.getInputStream();

            jsonObject = new JSONObject(convertStreamToString(stream));
        }catch(Exception e){
            System.out.println("Connection failed");
        }
        return jsonObject;
    }
}
