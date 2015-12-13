package kei.magnet.model;

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
    private int id;
    private User creator;
    private String name;
    private List<User> users;
    private List<Pin> pins;

    public List<Pin> getPins() {
        return pins;
    }

    public Group(){
        users = new ArrayList<>();
        pins = new ArrayList<>();
    }

    public Group(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            users = new ArrayList<>();
            pins = new ArrayList<>();
            creator = new User(jsonObject.getJSONObject("creator"));

            JSONArray arrayUsers = jsonObject.getJSONArray("users");
            for (int i = 0; i < arrayUsers.length(); i++) {
                User user = new User(arrayUsers.getJSONObject(i));
                users.add(user);
            }

            JSONArray arrayPins = jsonObject.getJSONArray("pins");
            for (int i = 0; i < arrayPins.length(); i++) {
                Pin pin = new Pin(arrayPins.getJSONObject(i));
                pins.add(pin);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
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

    public void setName(String name){
        this.name = name;
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
        dest.writeInt(id);
        dest.writeString(name);

        Bundle b = new Bundle();
        b.putParcelable("creator", creator);
        b.putParcelableArrayList("users", (ArrayList) users);
        b.putParcelableArrayList("pins", (ArrayList) pins);
        dest.writeBundle(b);
    }

    /**
     * Instanciate a Group using Parcelable
     *
     * @param in
     */
    public Group(Parcel in) {
        id = in.readInt();
        name = in.readString();

        Bundle b = in.readBundle(User.class.getClassLoader());
        creator = b.getParcelable("creator");
        users = b.getParcelableArrayList("users");
        pins = b.getParcelableArrayList("pins");
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