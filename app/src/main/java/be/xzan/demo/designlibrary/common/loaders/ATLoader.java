package be.xzan.demo.designlibrary.common.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * http://stackoverflow.com/questions/10524667/android-asynctaskloader-doesnt-start-loadinbackground
 */
public abstract class ATLoader<D> extends AsyncTaskLoader<D> {

    public ATLoader(Context context) {
        super(context);
        // run only once
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        // That's how we start every AsyncTaskLoader...
        // -  code snippet from  android.content.CursorLoader  (method  onStartLoading)
        if (takeContentChanged()) {
            forceLoad();
        }
    }
}
