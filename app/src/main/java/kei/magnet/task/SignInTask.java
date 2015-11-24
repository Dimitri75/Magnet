package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

import kei.magnet.JSONTask;

/**
 * Created by .Sylvain on 23/11/2015.
 */
public class SignInTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/user/";

    public SignInTask(Activity activity) {
        super(activity);
        setMethod("GET");
        setRequest("slash");
        setUrl(URL);
    }

    protected void onPostExecute (JSONObject tokenJSON) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if (tokenJSON != null) {
            try {
                GetUserTask task = new GetUserTask(getActivity());
                task.execute(new AbstractMap.SimpleEntry<>("token", tokenJSON.getString("token")));
            }
            catch(Exception e) {}
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Sign In", Toast.LENGTH_SHORT).show();
    }
}
