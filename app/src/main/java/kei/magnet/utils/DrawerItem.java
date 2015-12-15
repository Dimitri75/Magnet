package kei.magnet.utils;

import kei.magnet.enumerations.NavigationDrawerType;
import kei.magnet.model.Group;

public class DrawerItem {
    Object item;
    String itemName;
    int imgResID;
    NavigationDrawerType type;
    private Group group;

    public DrawerItem(Object object, NavigationDrawerType type, Group group) {
        this.item = object;
        this.itemName = object.toString();
        this.type = type;
        this.group = group;
    }

    public Object getItem() {
        return item;
    }

    public String getItemName() {
        return itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public NavigationDrawerType getType(){
        return type;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}