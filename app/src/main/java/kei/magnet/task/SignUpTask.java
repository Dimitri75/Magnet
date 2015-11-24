package kei.magnet.task;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.AbstractMap;

/**
 * Created by .Sylvain on 23/11/2015.
 */
public class SignUpTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/user/";
    private String login;
    private String password;

    public SignUpTask(Activity activity) {
        super(activity);
        setMethod("POST");
        setRequest("body");
        setUrl(URL);
    }

    protected JSONObject doInBackground(AbstractMap.SimpleEntry<String, String>... entries) {
        login = entries[0].getValue();
        password = entries[1].getValue();

        return super.doInBackground(entries);
    }

    protected void onPostExecute (JSONObject jsonUser) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if (jsonUser != null) {
            SignInTask task = new SignInTask(getActivity());
            task.execute(new AbstractMap.SimpleEntry<>("login", login),
                    new AbstractMap.SimpleEntry<>("password", password));
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Sign Up", Toast.LENGTH_SHORT).show();
    }
}
