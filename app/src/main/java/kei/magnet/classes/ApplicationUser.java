package kei.magnet.classes;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimitri on 27/10/2015.
 */
public class ApplicationUser extends User{
    private String token;
    private List<Group> groups;
    private static ApplicationUser instance = null;

    public static ApplicationUser getInstance() {
        if(instance == null) {
            instance = new ApplicationUser();
        }
        return instance;
    }

    private ApplicationUser() {
        token = null;
    }

    public void init(JSONObject jsonObject){
        token = null;
        super.init(jsonObject);

        groups = new ArrayList<>();

        try {

            JSONArray array = jsonObject.getJSONArray("groups");
            for (int i = 0; i < array.length(); i++){
                Group group = new Group(array.getJSONObject(i));
                groups.add(group);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Group> getGroups() {
        return groups;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeString(token);
        Bundle b = new Bundle();
        b.putParcelableArrayList("groups", (ArrayList) groups);
        dest.writeBundle(b);
    }

    /**
     * Instanciate an ApplicationUser using Parcelable
     * @param in
     */
    public ApplicationUser(Parcel in) {
        super(in);

        token = in.readString();
        Bundle b = in.readBundle(Group.class.getClassLoader());
        groups = b.getParcelableArrayList("groups");
    }

    public static final Parcelable.Creator<ApplicationUser> CREATOR = new Parcelable.Creator<ApplicationUser>() {

        public ApplicationUser createFromParcel(Parcel in) {
            return new ApplicationUser(in);
        }

        public ApplicationUser[] newArray(int size) {
            return new ApplicationUser[size];
        }
    };
}
