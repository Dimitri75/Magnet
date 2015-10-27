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
     * @param entries : entries of String. The first entry represents the url where the task has to connect
     * @return response of the request
     */
    protected JSONObject doInBackground(AbstractMap.SimpleEntry<String, String>... entries) {
        JSONObject jsonObject = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(entries[0].getValue()).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);

            Uri.Builder builder = new Uri.Builder();
            for(int i = 1; i < entries.length; i++) {
                builder.appendQueryParameter((String) entries[i].getKey(), (String) entries[i].getValue());
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
            System.out.println("Connection to " + entries[0].getValue().toString() + " failed");
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
