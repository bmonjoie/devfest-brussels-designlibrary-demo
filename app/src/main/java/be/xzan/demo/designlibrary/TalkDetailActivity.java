package be.xzan.demo.designlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;

import be.xzan.demo.designlibrary.adapters.SpeakerListAdapter;
import be.xzan.demo.designlibrary.common.activities.BackEnabledActivity;
import be.xzan.demo.designlibrary.common.loaders.ATTalkAndSpeakerLoaderWithProgressDialog;
import be.xzan.demo.designlibrary.data.Speaker;
import be.xzan.demo.designlibrary.data.Talk;

/**
 * Created on 29/09/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public class TalkDetailActivity extends BackEnabledActivity implements View.OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Talk> {

    public static final String TALK_ID = "TALK_ID";
    private static final int LOAD_TALK = 101;

    private Talk mTalk;
    private FloatingActionButton mFabAction;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail_talk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOAD_TALK, null, this);
    }

    private void bindData(Talk talk) {
        mTalk = talk;
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTime = (TextView) findViewById(R.id.tvTime);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        ListView lvSpeakers = (ListView) findViewById(R.id.lvSpeakers);

        tvName.setText(mTalk.name);
        tvTime.setText(mTalk.time);
        tvDescription.setText(mTalk.description);
        lvSpeakers.setAdapter(new SpeakerListAdapter(this, mTalk.getSpeakerArray()));
        lvSpeakers.setOnItemClickListener(this);
        mFabAction = (FloatingActionButton) findViewById(R.id.fabAction);
        mFabAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(mFabAction, R.string.added_to_favorite, Snackbar.LENGTH_SHORT)
                .setAction(R.string.common_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(mFabAction, R.string.removed_from_favorite, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SpeakerDetailActivity.startActivity(this, (ImageView) view.findViewById(R.id.ivPicture), (Speaker) parent.getAdapter().getItem(position));
    }

    @Override
    public Loader<Talk> onCreateLoader(int id, Bundle args) {
        return new TalkDetailLoader(this, getIntent().getIntExtra(TALK_ID, -1));
    }

    @Override
    public void onLoadFinished(Loader<Talk> loader, Talk data) {
        if (data != null) {
            bindData(data);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.common_error)
                    .setMessage(R.string.common_error_couldnt_find_talk)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Talk> loader) {

    }

    private static class TalkDetailLoader extends ATTalkAndSpeakerLoaderWithProgressDialog<Talk> {

        private final int mTalkId;

        public TalkDetailLoader(Activity activity, int talkId) {
            super(activity);
            mTalkId = talkId;
        }

        @Override
        public Talk loadInBackground() {
            try {
                Talk talk = mHelper.getTalkDAO().queryForId(mTalkId);
                if (talk == null) {
                    loadTalksAndSessionAndSave();
                    talk = mHelper.getTalkDAO().queryForId(mTalkId);
                }
                return talk;
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
