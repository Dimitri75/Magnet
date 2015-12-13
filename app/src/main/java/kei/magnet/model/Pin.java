package kei.magnet.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Dimitri on 27/10/2015.
 */
public class Pin implements Parcelable{
    private String name;
    private String description;
    private Location location;
    private Date creation_time;
    private Date deletion_time;


    public Pin() {

    }

    public Pin(JSONObject jsonObject){
        try {
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");

            Location location = new Location(jsonObject.getJSONObject("location"));
            this.location = location;

            this.creation_time = DateFormat.getDateTimeInstance().parse(jsonObject.getString("creation_date"));
            this.deletion_time = DateFormat.getDateTimeInstance().parse(jsonObject.getString("deletion_time"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
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
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(creation_time.toString());
        dest.writeString(deletion_time.toString());
        Bundle b = new Bundle();
        b.putParcelable("location", location);
        dest.writeBundle(b);
    }

    /**
     * Instanciate a User using Parcelable
     * @param in
     */
    public Pin(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        try {
            this.creation_time = DateFormat.getDateTimeInstance().parse(in.readString());
            this.deletion_time = DateFormat.getDateTimeInstance().parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Bundle b = in.readBundle(Location.class.getClassLoader());
        location = b.getParcelable("location");
    }
    public static final Parcelable.Creator<Pin> CREATOR = new Parcelable.Creator<Pin>() {
        public Pin createFromParcel(Parcel in) {
            return new Pin(in);
        }
        public Pin[] newArray(int size) {
            return new Pin[size];
        }
    };
    //END PARCELABLE
}
