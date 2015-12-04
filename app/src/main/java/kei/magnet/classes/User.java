package kei.magnet.classes;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dimitri on 27/10/2015.
 */
public class User implements Parcelable{
    private int id;
    private String login;
    private Location location;

    public User() {

    }

    public User(JSONObject jsonObject) {
        init(jsonObject);
    }

    protected void init(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.login = jsonObject.getString("login");

            Location location = new Location(jsonObject.getJSONObject("location"));
            this.location = location;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Location getLocation() {
        return location;
    }

    public LatLng getLatLng() {
        return new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
    }

    @Override
    public String toString() {
        return login;
    }

    public void setLocation(Location location) {
        this.location = location;
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
        dest.writeString(login);

        Bundle b = new Bundle();
        b.putParcelable("location", location);
        dest.writeBundle(b);
    }

    /**
     * Instanciate a User using Parcelable
     *
     * @param in
     */
    public User(Parcel in) {
        this.id = in.readInt();
        this.login = in.readString();

        Bundle b = in.readBundle(Location.class.getClassLoader());
        location = b.getParcelable("location");
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //END PARCELABLE

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return login.equals(user.getLogin());

    }

}
