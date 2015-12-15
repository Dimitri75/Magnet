package kei.magnet.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.AbstractMap;
import java.util.ArrayList;

import kei.magnet.R;
import kei.magnet.model.ApplicationUser;
import kei.magnet.model.Group;
import kei.magnet.model.User;
import kei.magnet.utils.UserListAdapter;
import kei.magnet.task.AddUserToGroupTask;
import kei.magnet.task.SearchUserTask;

/**
 * Created by carlo_000 on 24/11/2015.
 */
public class AddUserFragment extends DialogFragment {
    private ApplicationUser applicationUser;
    private EditText txtName;
    private Group group;
    private ListView userList;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Bundle bundle = getArguments();
        if ((group = (Group) bundle.get("group")) == null) {
            dismiss();
        }

        applicationUser = ApplicationUser.getInstance();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final View v = inflater.inflate(R.layout.activity_group_update, null);

        userList = (ListView)v.findViewById(R.id.applicationUsers);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = userList.getAdapter().getItem(position);
                if(item instanceof User) {
                    User user = (User) item;
                    AddUserToGroupTask task = new AddUserToGroupTask(getActivity(), applicationUser.getToken(), group.getId());
                    task.execute(new AbstractMap.SimpleEntry<>("login", user.getLogin()));
                    dismiss();
                }
            }
        });

        txtName = (EditText) v.findViewById(R.id.group_update_editText_GROUPNAME);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    UserListAdapter adapter = new UserListAdapter(getActivity().getApplicationContext(), R.layout.activity_group_update_row, new ArrayList<User>());
                    userList.setAdapter(adapter);
                } else {
                    SearchUserTask task = new SearchUserTask(getActivity(), userList);
                    task.execute(new AbstractMap.SimpleEntry<>("login", s.toString()));
                }
            }
        });

        builder.setView(v);

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
