package kei.magnet.fragments;

import kei.magnet.enumerations.NavigationDrawerType;

public class DrawerItem {
    Object item;
    String itemName;
    int imgResID;
    NavigationDrawerType type;

    public DrawerItem(Object object, NavigationDrawerType type) {
        this.item = object;
        this.itemName = object.toString();
        this.type = type;
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

}