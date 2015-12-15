package kei.magnet.utils;

/**
 * Created by Suiken on 20/11/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        DrawerItemHolder drawerHolder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);

            drawerHolder.userTitle = (TextView) view.findViewById(R.id.userTitle);
            drawerHolder.groupTitle = (TextView) view.findViewById(R.id.groupTitle);
            drawerHolder.titleTitle = (TextView) view.findViewById(R.id.titleTitle);

            // drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

            drawerHolder.groupButtonLayout = (LinearLayout) view.findViewById(R.id.menu_buttonLayout);
            drawerHolder.groupLayout = (LinearLayout) view.findViewById(R.id.groupLayout);
            drawerHolder.userLayout = (LinearLayout) view.findViewById(R.id.userLayout);
            drawerHolder.titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);

            view.setTag(drawerHolder);
        } else
            drawerHolder = (DrawerItemHolder) view.getTag();

        DrawerItem dItem = this.drawerItemList.get(position);


        if (dItem.getType() == NavigationDrawerType.TITLE) {

        } else if (dItem.getType() == NavigationDrawerType.USER) {
            drawerHolder.userLayout.setVisibility(LinearLayout.VISIBLE);
            drawerHolder.groupLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.titleLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.groupButtonLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.userTitle.setText(dItem.getItemName());
            drawerHolder.userTitle.setVisibility(View.VISIBLE);
        } else if (dItem.getType() == NavigationDrawerType.GROUP) {
            drawerHolder.groupLayout.setVisibility(LinearLayout.VISIBLE);
            drawerHolder.titleLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.userLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.groupButtonLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.groupTitle.setText(dItem.getItemName());
            drawerHolder.groupTitle.setVisibility(View.VISIBLE);
        } else if (dItem.getType().equals(NavigationDrawerType.BUTTONGROUP.BUTTONGROUP)) {
            drawerHolder.groupButtonLayout.setVisibility(LinearLayout.VISIBLE);
            drawerHolder.groupLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.titleLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.userLayout.setVisibility(LinearLayout.INVISIBLE);
        }
        return view;
    }

    private static class DrawerItemHolder {
        TextView userTitle, groupTitle, titleTitle;
        ImageView profilePicture;
        LinearLayout titleLayout, userLayout, groupLayout, groupButtonLayout;
    }
}