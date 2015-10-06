package be.xzan.demo.designlibrary.common.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import be.xzan.demo.designlibrary.R;
import be.xzan.demo.designlibrary.common.config.AppConstants;

/**
 * Created on 4/10/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public abstract class RefreshableDrawerActivity extends DrawerActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSrfRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSrfRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
        mSrfRefresh.setOnRefreshListener(this);
    }

    protected SwipeRefreshLayout getRefreshLayout() {
        return mSrfRefresh;
    }

    @Override
    public abstract void onRefresh();

    protected RecyclerView.LayoutManager getGridLayout() {
        return new GridLayoutManager(this,
                isLandscape() ? AppConstants.GRID_COLUMN_LANDSCAPE : AppConstants.GRID_COLUMN_PORTRAIT);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void setRefreshing(boolean refreshing) {
        mSrfRefresh.setRefreshing(refreshing);
    }
}
