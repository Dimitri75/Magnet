package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.JSONTask;

/**
 * Created by .Sylvain on 24/11/2015.
 */
public class UpdateUserTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/user/";

    public UpdateUserTask(Activity activity, String token) {
        super(activity);
        setMethod("PUT");
        setRequest("body");
        setUrl(URL + token);
    }

    protected void onPostExecute (JSONObject jsonUser) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if (jsonUser != null) {

        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Sign Up", Toast.LENGTH_SHORT).show();
    }
}
