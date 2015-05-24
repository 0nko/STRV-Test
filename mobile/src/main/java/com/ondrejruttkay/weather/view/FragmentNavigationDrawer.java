package com.ondrejruttkay.weather.view;

/**
 * Created by Onko on 5/22/2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ondrejruttkay.weather.R;
import com.ondrejruttkay.weather.adapter.DrawerListAdapter;
import com.ondrejruttkay.weather.entity.DrawerListItem;

import java.util.ArrayList;

public class FragmentNavigationDrawer extends DrawerLayout {
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private LinearLayout mDrawerLinearLayout;
    private Toolbar mToolbar;
    private int drawerContainerRes;

    private DrawerListAdapter mDrawerAdapter;
    private ArrayList<DrawerListItem> mDrawerItems;
    private ArrayList<FragmentNavItem> mDrawerNavigationItems;


    public FragmentNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public FragmentNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public FragmentNavigationDrawer(Context context) {
        super(context);
    }


    public void setupDrawerConfiguration(ListView drawerListView, Toolbar drawerToolbar, LinearLayout drawerLinearLayout, int drawerContainerResId) {
        // Setup navigation items array
        mDrawerNavigationItems = new ArrayList<>();
        mDrawerItems = new ArrayList<>();
        mDrawerLinearLayout = drawerLinearLayout;

        drawerContainerRes = drawerContainerResId;
        // Setup drawer list view
        mDrawerList = drawerListView;
        mToolbar = drawerToolbar;
        // Setup item listener
        mDrawerList.setOnItemClickListener(new FragmentDrawerItemListener());
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = setupDrawerToggle();
        setDrawerListener(mDrawerToggle);
        // Setup action buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle.syncState();
    }

    // addNavItem("First", R.mipmap.ic_one, "First Fragment", FirstFragment.class)
    public void addNavItem(String navTitle, int icon, String windowTitle, Class<? extends Fragment> fragmentClass) {
        // adding nav drawer items to array
        mDrawerItems.add(new DrawerListItem(navTitle, icon));
        // Set the adapter for the list view
        mDrawerAdapter = new DrawerListAdapter(getActivity(), mDrawerItems);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerNavigationItems.add(new FragmentNavItem(windowTitle, fragmentClass));
    }


    /**
     * Swaps fragments in the main content view
     */
    public void selectDrawerItem(int position) {
        // Create a new fragment and specify the planet to show based on
        // position
        FragmentNavItem navItem = mDrawerNavigationItems.get(position);
        Fragment fragment = null;
        try {
            fragment = navItem.getFragmentClass().newInstance();
            Bundle args = navItem.getFragmentArgs();
            if (args != null) {
                fragment.setArguments(args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(drawerContainerRes, fragment).commit();

        // Highlight the selected item, update the mTitle, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(navItem.getTitle());
        closeDrawer(mDrawerLinearLayout);
    }

    public void syncState() {
        mDrawerToggle.syncState();
    }


    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }


    private FragmentActivity getActivity() {
        return (FragmentActivity) getContext();
    }


    private ActionBar getSupportActionBar() {
        return ((AppCompatActivity)getActivity()).getSupportActionBar();
    }


    private void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    private class FragmentDrawerItemListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectDrawerItem(position);
        }
    }

    private class FragmentNavItem {
        private Class<? extends Fragment> mFragmentClass;
        private String mTitle;
        private Bundle mFragmentArgs;


        public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass) {
            this(title, fragmentClass, null);
        }


        public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass, Bundle args) {
            this.mFragmentClass = fragmentClass;
            this.mFragmentArgs = args;
            this.mTitle = title;
        }


        public Class<? extends Fragment> getFragmentClass() {
            return mFragmentClass;
        }


        public String getTitle() {
            return mTitle;
        }


        public Bundle getFragmentArgs() {
            return mFragmentArgs;
        }
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(getActivity(), this, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }


    public boolean isDrawerOpen() {
        return isDrawerOpen(mDrawerList);
    }
}
