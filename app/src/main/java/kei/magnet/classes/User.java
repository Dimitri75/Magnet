package kei.magnet.classes;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dimitri on 27/10/2015.
 */
public class User {
    private String login;
    private LatLng location;

    public User() {

    }

    public User(JSONObject jsonObject){
        try {
            this.login = jsonObject.getString("login");
            this.location = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(String login, LatLng location) {
        this.login = login;
        this.location = location;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
