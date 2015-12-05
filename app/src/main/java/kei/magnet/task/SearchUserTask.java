package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.AbstractMap;

/**
 * Created by .Sylvain on 05/12/2015.
 */
public class SearchUserTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/search/user";

    public SearchUserTask(Activity activity) {
        super(activity);
        setMethod("POST");
        setRequest("body");
        setUrl(URL);
    }

    protected void onPostExecute (JSONObject result) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if (result != null) {
            try {
                JSONArray users = result.getJSONArray("users");
            }
            catch(Exception e) {}
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Sign In", Toast.LENGTH_SHORT).show();
    }
}
