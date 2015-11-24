package kei.magnet.task;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONObject;

import kei.magnet.JSONTask;
import kei.magnet.activities.MagnetActivity;
import kei.magnet.classes.ApplicationUser;

/**
 * Created by .Sylvain on 23/11/2015.
 */
public class GetUserTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/user";

    public GetUserTask(Activity activity) {
        super(activity);
        setMethod("GET");
        setRequest("slash");
        setUrl(URL);
    }

    protected void onPostExecute (JSONObject userJSON) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if (userJSON != null) {
            ApplicationUser applicationUser = new ApplicationUser(userJSON);
            Intent intent = new Intent(getActivity(), MagnetActivity.class);
            intent.putExtra("applicationUser", applicationUser);
            getActivity().startActivity(intent);
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Get User", Toast.LENGTH_SHORT).show();
    }
}
