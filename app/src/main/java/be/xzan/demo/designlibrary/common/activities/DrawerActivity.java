package be.xzan.demo.designlibrary.common.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import be.xzan.demo.designlibrary.AboutActivity;
import be.xzan.demo.designlibrary.MapActivity;
import be.xzan.demo.designlibrary.R;
import be.xzan.demo.designlibrary.SpeakersActivity;
import be.xzan.demo.designlibrary.TalksActivity;

/**
 * Created on 29/09/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public abstract class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNvNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        setupDrawerAndNavigation();
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    private void setupDrawerAndNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dlDrawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNvNavigation = (NavigationView) findViewById(R.id.nvNavigation);
        mNvNavigation.setNavigationItemSelectedListener(this);
    }

    protected void setCheckedItem(int item) {
        mNvNavigation.setCheckedItem(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Class c = null;
        switch (menuItem.getItemId()) {
            case R.id.menu_talks:
                if (!(this instanceof TalksActivity)) {
                    c = TalksActivity.class;
                }
                break;
            case R.id.menu_speakers:
                if (!(this instanceof SpeakersActivity)) {
                    c = SpeakersActivity.class;
                }
                break;
            case R.id.menu_map:
                if (!(this instanceof MapActivity)) {
                    c = MapActivity.class;
                }
                break;
            case R.id.menu_about:
                if (!(this instanceof AboutActivity)) {
                    c = AboutActivity.class;
                }
                break;
        }
        if (c != null) {
            startActivity(new Intent(this, c));
            mDrawerLayout.closeDrawers();
            finish();
            return true;
        }
        return false;
    }
}
