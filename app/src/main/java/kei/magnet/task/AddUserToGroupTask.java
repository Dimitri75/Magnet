package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by .Sylvain on 24/11/2015.
 */
public class AddUserToGroupTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/group/";

    public AddUserToGroupTask(Activity activity, String token, int groupId) {
        super(activity);
        setMethod("POST");
        setRequest("body");
        setUrl(URL + groupId + "/user/" + token);
    }

    protected void onPostExecute (JSONObject jsonUser) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if (jsonUser != null) {
            getActivity().finish();
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail adding a User", Toast.LENGTH_SHORT).show();
    }
}
