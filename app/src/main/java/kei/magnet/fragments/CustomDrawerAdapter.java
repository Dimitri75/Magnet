package kei.magnet.fragments;

/**
 * Created by Suiken on 20/11/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kei.magnet.R;
import kei.magnet.enumerations.NavigationDrawerType;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID,
                               List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);

            drawerHolder.userTitle = (TextView) view.findViewById(R.id.userTitle);
            drawerHolder.groupTitle = (TextView) view.findViewById(R.id.groupTitle);
            drawerHolder.titleTitle = (TextView) view.findViewById(R.id.titleTitle);

//            drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

            drawerHolder.groupLayout = (LinearLayout) view.findViewById(R.id.groupLayout);
            drawerHolder.userLayout = (LinearLayout) view.findViewById(R.id.userLayout);
            drawerHolder.titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }

        DrawerItem dItem = this.drawerItemList.get(position);


        if (dItem.getType() == NavigationDrawerType.TITLE){
            //TODO
            //            drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
//                    dItem.getImgResID()));
        } else if (dItem.getType() == NavigationDrawerType.USER) {
            System.out.println("user");
            drawerHolder.userLayout.setVisibility(LinearLayout.VISIBLE);
            //drawerHolder.userLayout.setBackgroundColor(Color.GREEN);

            drawerHolder.groupLayout.setVisibility(LinearLayout.INVISIBLE);

            drawerHolder.titleLayout.setVisibility(LinearLayout.INVISIBLE);

            drawerHolder.userTitle.setText(dItem.getTitle());
            drawerHolder.userTitle.setVisibility(View.VISIBLE);
        } else if(dItem.getType() == NavigationDrawerType.GROUP){
            System.out.println("group");
            drawerHolder.groupLayout.setVisibility(LinearLayout.VISIBLE);
            //drawerHolder.groupLayout.setBackgroundColor(Color.BLUE);

            drawerHolder.titleLayout.setVisibility(LinearLayout.INVISIBLE);

            drawerHolder.userLayout.setVisibility(LinearLayout.INVISIBLE);

            drawerHolder.groupTitle.setText(dItem.getTitle());
            drawerHolder.groupTitle.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private static class DrawerItemHolder {
        TextView userTitle, groupTitle, titleTitle;
        ImageView profilePicture;
        LinearLayout titleLayout, userLayout, groupLayout;
    }
}