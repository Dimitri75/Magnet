package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import kei.magnet.activities.MagnetActivity;
import kei.magnet.enumerations.NavigationDrawerType;
import kei.magnet.utils.DrawerItem;

/**
 * Created by .Sylvain on 15/12/2015.
 */
public class RemoveUserFromGroupTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/group/";
    private String token;

    public RemoveUserFromGroupTask(Activity activity, String token, int groupId) {
        super(activity);
        this.token = token;

        setMethod("DELETE");
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
            Toast.makeText(getActivity().getApplicationContext(), "Fail removing a User", Toast.LENGTH_SHORT).show();
    }

    public void updateMenu(){
        MagnetActivity magnetActivity = (MagnetActivity) getActivity();
        List<DrawerItem> menuDataList =  magnetActivity.getMenuDataList();


        menuDataList.add(new DrawerItem(token, NavigationDrawerType.USER));
    }
}
