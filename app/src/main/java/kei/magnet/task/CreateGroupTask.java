package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import kei.magnet.JSONTask;

/**
 * Created by .Sylvain on 24/11/2015.
 */
public class CreateGroupTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/group/";

    public CreateGroupTask(Activity activity, String token) {
        super(activity);
        setMethod("POST");
        setRequest("body");
        setUrl(URL + token);
    }

    protected void onPostExecute (JSONObject jsonUser) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if (jsonUser != null) {
            getActivity().finish();
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Create Group", Toast.LENGTH_SHORT).show();
    }
}
