package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import kei.magnet.JSONTask;
import kei.magnet.activities.PinCreationActivity;
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
        else if (jsonPin != null) {
            try {
                int groupId = jsonPin.getInt("group_id");
                List<Group> groups = ApplicationUser.GetInstance().getGroups();
                int i = 0;
                Group group = null;
                for(Group currentGroup : groups) {
                    if(group.getId() == groupId) {
                        group = currentGroup;
                        break;
                    }
                }

                if(group != null) {
                    Pin pin = new Pin(jsonPin);
                    group.getPins().add(pin);
                }
            }
            catch(Exception e) {}

            getActivity().finish();
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Sign Up", Toast.LENGTH_SHORT).show();
    }
}
