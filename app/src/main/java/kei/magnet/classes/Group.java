package kei.magnet.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimitri on 27/10/2015.
 */
public class Group {
    User creator;
    List<User> users;

    public Group(JSONObject jsonObject) {
        try {
            users = new ArrayList<>();
            creator = new User(jsonObject.getJSONObject("user"));

            JSONArray array = jsonObject.getJSONArray("users");
            for (int i = 0; i < array.length(); i++) {
                User user = new User(array.getJSONObject(i));
                users.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Group(User creator, List<User> users) {
        this.creator = creator;
        this.users = users;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
