package be.xzan.demo.designlibrary.common.loaders;

import android.app.Activity;
import android.app.ProgressDialog;

import be.xzan.demo.designlibrary.R;

/**
 * Created on 4/10/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public abstract class ATTalkAndSpeakerLoaderWithProgressDialog<D> extends ATTalkAndSpeakerLoader<D> {

    private final Activity mActivity;
    private ProgressDialog mProgress;

    public ATTalkAndSpeakerLoaderWithProgressDialog(Activity activity) {
        super(activity);
        mActivity = activity;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mProgress = ProgressDialog.show(mActivity, getContext().getString(R.string.common_loading), getContext().getString(R.string.common_please_wait));
        mProgress.show();
    }

    @Override
    public void deliverResult(D data) {
        super.deliverResult(data);
        mProgress.dismiss();
    }

    @Override
    public boolean cancelLoad() {
        if (mProgress != null) {
            mProgress.dismiss();
        }
        return super.cancelLoad();
    }
}
