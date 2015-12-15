package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kei.magnet.activities.MagnetActivity;
import kei.magnet.enumerations.NavigationDrawerType;
import kei.magnet.fragments.DrawerItem;
import kei.magnet.model.ApplicationUser;
import kei.magnet.model.Group;
import kei.magnet.model.User;

/**
 * Created by .Sylvain on 24/11/2015.
 */
public class    AddUserToGroupTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/group/";
    private String token;
    private int groupId;

    public AddUserToGroupTask(Activity activity, String token, int groupId) {
        super(activity);
        this.token = token;
        this.groupId = groupId;

        setMethod("POST");
        setRequest("body");
        setUrl(URL + String.valueOf(groupId) + "/user/" + token);
    }

    protected void onPostExecute (JSONObject jsonUser) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if(getStatusCode() != 200) {
            this.handleHttpError(getStatusCode());
        }
        else if (jsonUser != null) {
            updateMenu();
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail adding a User", Toast.LENGTH_SHORT).show();
    }

    public void updateMenu(){
        MagnetActivity magnetActivity = (MagnetActivity) getActivity();
        magnetActivity.updateApplicationUser();
    }
}
