package com.ondrejruttkay.weather.android.entity;

/**
 * Created by Onko on 5/22/2015.
 */
public class DrawerListItem {
    private String mTitle;
    private int mIcon;


    public DrawerListItem() {
    }


    public DrawerListItem(String title, int icon) {
        this.mTitle = title;
        this.mIcon = icon;
    }


    public String getTitle() {
        return this.mTitle;
    }


    public void setTitle(String title) {
        this.mTitle = title;
    }


    public int getIcon() {
        return this.mIcon;
    }


    public void setIcon(int icon) {
        this.mIcon = icon;
    }
}