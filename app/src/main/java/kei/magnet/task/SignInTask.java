package kei.magnet.task;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.AbstractMap;

/**
 * Created by .Sylvain on 23/11/2015.
 */
public class SignInTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/user";
    private static String FILENAME = "magnet_token";

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
        else if(getStatusCode() != 200) {
            this.handleHttpError(getStatusCode());
        }
        else if (tokenJSON != null) {
            try {
                String token = tokenJSON.getString("token");
                GetUserTask task = new GetUserTask(getActivity());
                task.execute(new AbstractMap.SimpleEntry<>("token", tokenJSON.getString("token")));
                FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(token.getBytes());
                fos.close();

            }
            catch(Exception e) {}
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Sign In", Toast.LENGTH_SHORT).show();
    }
}
