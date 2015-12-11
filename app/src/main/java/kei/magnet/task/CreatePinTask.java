package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.classes.Pin;

/**
 * Created by .Sylvain on 24/11/2015.
 */
public class CreatePinTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/pin/";

    public CreatePinTask(Activity activity, String token) {
        super(activity);
        setMethod("POST");
        setRequest("body");
        setUrl(URL + token);
    }

    protected void onPostExecute (JSONObject jsonPin) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if(getStatusCode() != 200) {
            this.handleHttpError(getStatusCode());
        }
        else if (jsonPin != null) {
            try {
                int groupId = jsonPin.getInt("group_id");
                for(Group group : ApplicationUser.getInstance().getGroups()) {
                    if(group.getId() == groupId) {
                        group.getPins().add(new Pin(jsonPin));
                        break;
                    }
                }
            }
            catch(Exception e) {}
            getActivity().finish();
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Create Pin", Toast.LENGTH_SHORT).show();
    }
}
