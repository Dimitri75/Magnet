package kei.magnet.task;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import kei.magnet.R;
import kei.magnet.activities.MagnetActivity;
import kei.magnet.classes.User;
import kei.magnet.classes.UserListAdapter;

/**
 * Created by .Sylvain on 05/12/2015.
 */
public class SearchUserTask extends JSONTask {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/search/user";
    private ListView userList;

    public SearchUserTask(Activity activity, ListView userList) {
        super(activity);
        setMethod("GET");
        setRequest("slash");
        setUrl(URL);
        this.userList = userList;
    }

    protected void onPostExecute (JSONObject result) {
        if(getException() != null) {
            Toast.makeText(getActivity(), getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        else if(getStatusCode() != 200) {
            this.handleHttpError(getStatusCode());
        }
        else if (result != null) {
            try {
                JSONArray usersJSON = result.getJSONArray("users");
                List<User> users = new ArrayList<>();
                for (int i = 0; i < usersJSON.length(); i++) {
                    JSONObject userJSON = usersJSON.getJSONObject(i);
                    users.add(new User(userJSON));
                }

                UserListAdapter adapter = new UserListAdapter(getActivity().getApplicationContext(), R.layout.activity_group_update_row,users);
                userList.setAdapter(adapter);
            }
            catch(Exception e) {}
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "Fail Search Users", Toast.LENGTH_SHORT).show();
        }
    }
}
