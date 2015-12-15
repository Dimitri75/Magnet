package kei.magnet.task;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.AbstractMap;

import kei.magnet.activities.MagnetActivity;
import kei.magnet.activities.SignInActivity;
import kei.magnet.model.ApplicationUser;

/**
 * Created by .Sylvain on 23/11/2015.
 */
public class GetUserTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/user";
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
        else if(getStatusCode() != 200) {
            this.handleHttpError(getStatusCode());
        }
        else if (userJSON != null) {
            ApplicationUser applicationUser = ApplicationUser.getInstance();
            applicationUser.init(userJSON);
            applicationUser.setToken(token);

            if(getActivity() instanceof SignInActivity) {
                getActivity().finish();
            }
            else if(getActivity() instanceof MagnetActivity) {
                MagnetActivity magnetActivity = (MagnetActivity)getActivity();
                if(!magnetActivity.isInitialised) {
                    magnetActivity.init();
                }
                else {
                    magnetActivity.updateMenu();
                }
            }
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Get User", Toast.LENGTH_SHORT).show();
    }
}
