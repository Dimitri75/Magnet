package kei.magnet.fragments;


import kei.magnet.enumerations.NavigationDrawerType;

public class DrawerItem {

    String ItemName;
    int imgResID;
    String title;
    boolean isSpinner;
    NavigationDrawerType type;

    public DrawerItem(String itemName, int imgResID) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
    }

    public DrawerItem(boolean isSpinner) {
        this(null, 0);
        this.isSpinner = isSpinner;
    }

    public DrawerItem(Object object, NavigationDrawerType type) {
        this(null, 0);
        this.title = object.toString();
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSpinner() {
        return isSpinner;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
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