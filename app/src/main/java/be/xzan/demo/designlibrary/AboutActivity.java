package be.xzan.demo.designlibrary;

import android.os.Bundle;

import be.xzan.demo.designlibrary.common.activities.DrawerActivity;

/**
 * Created on 29/09/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public class AboutActivity extends DrawerActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setCheckedItem(R.id.menu_about);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }
}
