package kei.magnet.task;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.JSONTask;
import kei.magnet.activities.MagnetActivity;
import kei.magnet.classes.ApplicationUser;

/**
 * Created by .Sylvain on 23/11/2015.
 */
public class GetUserTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/user/";
    private String token;

    public GetUserTask(Activity activity) {
        super(activity);
        setMethod("GET");
        setRequest("slash");
        setUrl(URL);
    }

    protected JSONObject doInBackground(AbstractMap.SimpleEntry<String, String>... entries) {
        token = entries[0].getValue();

        return super.doInBackground(entries);
    }

    protected void onPostExecute (JSONObject userJSON) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if (userJSON != null) {
            ApplicationUser applicationUser = ApplicationUser.getInstance();
            applicationUser.init(userJSON);
            applicationUser.setToken(token);
            Intent intent = new Intent(getActivity(), MagnetActivity.class);
            getActivity().startActivity(intent);
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Get User", Toast.LENGTH_SHORT).show();
    }
}
