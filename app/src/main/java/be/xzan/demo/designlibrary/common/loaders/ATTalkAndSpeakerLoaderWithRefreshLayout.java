package be.xzan.demo.designlibrary.common.loaders;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created on 4/10/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public abstract class ATTalkAndSpeakerLoaderWithRefreshLayout<D> extends ATTalkAndSpeakerLoader<D> {


    private final SwipeRefreshLayout mRefreshLayout;
    private final boolean mForceRefresh;

    public ATTalkAndSpeakerLoaderWithRefreshLayout(Context context, SwipeRefreshLayout refreshLayout, boolean forceRefresh) {
        super(context);
        mForceRefresh = forceRefresh;
        mRefreshLayout = refreshLayout;
    }

    public boolean isForceRefresh() {
        return mForceRefresh;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        // Otherwise it doesn't show the first time
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void deliverResult(D data) {
        super.deliverResult(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean cancelLoad() {
        mRefreshLayout.setRefreshing(false);
        return super.cancelLoad();
    }
}
