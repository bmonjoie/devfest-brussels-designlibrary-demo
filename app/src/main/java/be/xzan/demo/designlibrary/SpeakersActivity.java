package be.xzan.demo.designlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import be.xzan.demo.designlibrary.common.activities.RefreshableDrawerActivity;
import be.xzan.demo.designlibrary.common.loaders.ATTalkAndSpeakerLoaderWithRefreshLayout;
import be.xzan.demo.designlibrary.data.Speaker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SpeakersActivity extends RefreshableDrawerActivity implements LoaderManager.LoaderCallbacks<List<Speaker>> {

    private static final int LOAD_SPEAKERS = 101;
    private static final String FORCE_REFRESH = "FORCE_REFRESH";
    private List<Speaker> mSpeakers;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setCheckedItem(R.id.menu_speakers);
        RecyclerView rvList = (RecyclerView) findViewById(R.id.rvList);
        rvList.setLayoutManager(getGridLayout());
        mAdapter = getAdapter();
        rvList.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(LOAD_SPEAKERS, null, this);
    }

    protected RecyclerView.Adapter getAdapter() {
        return new RecyclerView.Adapter<SpeakerViewHolder>() {
            @Override
            public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(SpeakersActivity.this)
                        .inflate(R.layout.list_speakers, parent, false);
                SpeakerViewHolder holder = new SpeakerViewHolder(v);
                v.findViewById(R.id.vwClick).setOnClickListener(holder);
                return holder;
            }

            @Override
            public void onBindViewHolder(SpeakerViewHolder holder, int position) {
                holder.setSpeaker(SpeakersActivity.this, mSpeakers.get(position));
            }

            @Override
            public int getItemCount() {
                if (mSpeakers == null) {
                    return 0;
                }
                return mSpeakers.size();
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_speakers;
    }

    @Override
    public void onRefresh() {
        Bundle options = new Bundle();
        options.putBoolean(FORCE_REFRESH, true);
        getSupportLoaderManager().restartLoader(LOAD_SPEAKERS, options, this);
    }

    @Override
    public Loader<List<Speaker>> onCreateLoader(int id, Bundle args) {
        return new ATTalkAndSpeakerLoaderWithRefreshLayout<List<Speaker>>(this, getRefreshLayout(), args != null && args.getBoolean(FORCE_REFRESH, false)) {
            @Override
            public List<Speaker> loadInBackground() {
                try {
                    List<Speaker> speakers = mHelper.getSpeakerDAO().queryForAll();
                    if (speakers == null || speakers.isEmpty() || isForceRefresh()) {
                        loadTalksAndSessionAndSave();
                        speakers = mHelper.getSpeakerDAO().queryForAll();
                    }
                    return speakers;
                } catch (SQLException|IOException e) {
                    e.printStackTrace();
                }
                return  null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Speaker>> loader, List<Speaker> data) {
        if (data == null || data.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.common_error)
                    .setMessage(R.string.common_error_couldnt_load_speaker)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();
        } else {
            mSpeakers = data;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Speaker>> loader) {

    }

    private static class SpeakerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Speaker mSpeaker;
        private Activity mActivity;

        private final TextView tvName;
        private final ImageView ivPicture;

        public SpeakerViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
        }

        public void setSpeaker(Activity activity, Speaker speaker) {
            mActivity = activity;
            mSpeaker = speaker;
            tvName.setText(speaker.name);
            Glide.with(mActivity)
                    .load(speaker.getPhotoUrl())
                    .into(ivPicture);
        }

        @Override
        public void onClick(View v) {
            SpeakerDetailActivity.startActivity(mActivity, ivPicture, mSpeaker);
        }
    }
}
