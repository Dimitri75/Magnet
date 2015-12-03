package kei.magnet.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.AbstractMap;

import kei.magnet.R;
import kei.magnet.classes.ApplicationUser;
import kei.magnet.classes.Group;
import kei.magnet.task.AddUserToGroupTask;

/**
 * Created by carlo_000 on 24/11/2015.
 */
public class AddUserFragment extends DialogFragment {
    private static String URL = "http://bardin.sylvain.perso.sfr.fr/";
    private ApplicationUser applicationUser;
    private EditText txtName;
    private Group group;

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

        View v = inflater.inflate(R.layout.activity_group_update, null);
        txtName = (EditText) v.findViewById(R.id.group_update_editText_GROUPNAME);

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
