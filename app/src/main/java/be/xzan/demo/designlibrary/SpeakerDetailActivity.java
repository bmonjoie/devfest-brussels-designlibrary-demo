package be.xzan.demo.designlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.sql.SQLException;

import be.xzan.demo.designlibrary.common.activities.BackEnabledActivity;
import be.xzan.demo.designlibrary.common.loaders.ATTalkAndSpeakerLoaderWithProgressDialog;
import be.xzan.demo.designlibrary.data.Speaker;

/**
 * Created on 29/09/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public class SpeakerDetailActivity extends BackEnabledActivity implements LoaderManager.LoaderCallbacks<Speaker> {

    private static final String SPEAKER_ID = "SPEAKER_ID";
    private static final String TRANSITION_NAME = "IMAGE";
    private static final int LOAD_SPEAKER = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOAD_SPEAKER, null, this);
        ActivityCompat.postponeEnterTransition(this);
    }

    private void bindData(Speaker speaker) {
        CollapsingToolbarLayout ctToolbar = (CollapsingToolbarLayout) findViewById(R.id.ctToolbar);
        if (ctToolbar != null) {
            ctToolbar.setTitle(speaker.name);
        }
        ImageView ivPicture = (ImageView) findViewById(R.id.ivPicture);
        ViewCompat.setTransitionName(ivPicture, TRANSITION_NAME);
        Glide.with(this).load(speaker.getPhotoUrl()).into(ivPicture);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(speaker.description);

        // At this stage, it's possible the image is not loaded and the animation won't show
        // We prefer this way not to block the UI until the image is loaded which might be
        // very slow ! Since this is called after the call to the database and potentially the call
        // to the "webservice", it's already too slow
        ActivityCompat.startPostponedEnterTransition(this);
    }

    public static void startActivity(Activity activity, @NonNull ImageView view, Speaker speaker) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, TRANSITION_NAME);
        Intent intent = new Intent(activity, SpeakerDetailActivity.class);
        intent.putExtra(SPEAKER_ID, speaker.id);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail_speaker;
    }

    @Override
    public Loader<Speaker> onCreateLoader(int id, Bundle args) {
        return new ATTalkAndSpeakerLoaderWithProgressDialog<Speaker>(this) {

            @Override
            public Speaker loadInBackground() {
                try {
                    Speaker speaker = mHelper.getSpeakerDAO().queryForId(getIntent().getIntExtra(SPEAKER_ID, -1));
                    if (speaker == null) {
                        loadTalksAndSessionAndSave();
                        speaker = mHelper.getSpeakerDAO().queryForId(getIntent().getIntExtra(SPEAKER_ID, -1));
                    }
                    return speaker;
                } catch (SQLException|IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Speaker> loader, Speaker data) {
        if (data != null) {
            bindData(data);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.common_error)
                    .setMessage(R.string.common_error_couldnt_find_speaker)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Speaker> loader) {

    }
}
