package kei.magnet.task;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONObject;

import kei.magnet.activities.MagnetActivity;

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

    protected void onPostExecute (JSONObject jsonGroup) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if(getStatusCode() != 200) {
            this.handleHttpError(getStatusCode());
        }
        else if (jsonGroup != null) {
            Intent data = new Intent();
            data.setData(Uri.parse(jsonGroup.toString()));
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Fail Create Group", Toast.LENGTH_SHORT).show();
    }
}
