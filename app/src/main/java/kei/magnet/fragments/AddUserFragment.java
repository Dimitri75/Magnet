package kei.magnet.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import kei.magnet.R;
import kei.magnet.activities.MagnetActivity;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.classes.User;
import kei.magnet.classes.UserListAdapter;
import kei.magnet.task.AddUserToGroupTask;
import kei.magnet.task.SearchUserTask;

/**
 * Created by carlo_000 on 24/11/2015.
 */
public class AddUserFragment extends DialogFragment {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/";
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
        txtName = (EditText) v.findViewById(R.id.group_update_editText_GROUPNAME);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                SearchUserTask task = new SearchUserTask(getActivity(), userList);
                task.execute(new AbstractMap.SimpleEntry<>("login", txtName.getText().toString()));
            }
        });
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            onClickSubmit();
//                            JSONObject jsonObject = JSONTask.getTask().execute(
//                                    new AbstractMap.SimpleEntry<>("url", URL + "group/" + group.getId() + "/user/" + applicationUser.getToken()),
//                                    new AbstractMap.SimpleEntry<>("method", "POST"),
//                                    new AbstractMap.SimpleEntry<>("request", "body"),
//                                    new AbstractMap.SimpleEntry<>("login", txtName.getText().toString())
//                            ).get();
//
//                            if (jsonObject != null)
//                                AddUserFragment.this.getDialog().dismiss();
//                            else
//                                Toast.makeText(getActivity().getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddUserFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void onClickSubmit() {
        AddUserToGroupTask task = new AddUserToGroupTask(getActivity(), applicationUser.getToken(), group.getId());
        task.execute(new AbstractMap.SimpleEntry<>("login", txtName.getText().toString()));
    }

}
