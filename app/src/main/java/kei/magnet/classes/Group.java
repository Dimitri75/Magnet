package kei.magnet.classes;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimitri on 27/10/2015.
 */
public class Group implements Parcelable {
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

    // PARCELABLE
    @Override
    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putParcelable("creator", creator);
        b.putParcelableArrayList("users", (ArrayList)users);
        dest.writeBundle(b);
    }

    /**
     * Instanciate a game using Parcelable
     * @param in
     */
    public Group(Parcel in) {
        Bundle b = in.readBundle(User.class.getClassLoader());
        creator = b.getParcelable("creator");
        users = b.getParcelableArrayList("rounds");
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {

        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
    //END PARCELABLE
}