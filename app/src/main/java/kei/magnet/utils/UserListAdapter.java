package kei.magnet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kei.magnet.R;
import kei.magnet.model.User;

/**
 * Created by carlo_000 on 04/12/2015.
 */
public class UserListAdapter extends ArrayAdapter<User> {
    public UserListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public UserListAdapter(Context context, int resource, List<User> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.activity_group_update_row, null);
        }
        Object object = getItem(position);
        if(object instanceof User){
            User user = (User)object;
            TextView textView = (TextView) view.findViewById(R.id.info_text);
            if(textView != null){

                textView.setText(user.getLogin());
            }
        }
        return view;
    }
}
