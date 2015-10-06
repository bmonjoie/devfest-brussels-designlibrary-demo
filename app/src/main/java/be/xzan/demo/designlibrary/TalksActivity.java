package be.xzan.demo.designlibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.xzan.demo.designlibrary.common.activities.RefreshableDrawerActivity;
import be.xzan.demo.designlibrary.common.loaders.ATTalkAndSpeakerLoaderWithRefreshLayout;
import be.xzan.demo.designlibrary.data.Talk;

public class TalksActivity extends RefreshableDrawerActivity implements TabLayout.OnTabSelectedListener, LoaderManager.LoaderCallbacks<List<Talk>> {

    private static final int LOAD_TALKS = 101;
    private static final String FORCE_REFRESH = "FORCE_REFRESH";
    private String mSelectedDay = Talk.DAY_ONE;
    private List<Talk> mAllTalks;
    private List<Talk> mTalks;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setTitle(R.string.menu_talks);
        setCheckedItem(R.id.menu_talks);
        RecyclerView rvList = (RecyclerView) findViewById(R.id.rvList);
        TabLayout tlTabs = (TabLayout) findViewById(R.id.tlTabs);
        tlTabs.addTab(tlTabs.newTab().setText(R.string.day_one));
        tlTabs.addTab(tlTabs.newTab().setText(R.string.day_two));
        tlTabs.setOnTabSelectedListener(this);
        mAdapter = getAdapter();
        rvList.setAdapter(mAdapter);
        rvList.setLayoutManager(getGridLayout());
        getSupportLoaderManager().initLoader(LOAD_TALKS, null, this);
    }

    protected RecyclerView.Adapter getAdapter() {
        return new RecyclerView.Adapter<TalkViewHolder>() {
            @Override
            public TalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(TalksActivity.this).inflate(R.layout.list_talks, parent, false);
                TalkViewHolder holder = new TalkViewHolder(v);
                v.setOnClickListener(holder);
                return holder;
            }

            @Override
            public void onBindViewHolder(TalkViewHolder holder, int position) {
                holder.setTalk(TalksActivity.this, mTalks.get(position));
            }

            @Override
            public int getItemCount() {
                if (mTalks == null) {
                    mTalks = new ArrayList<>();
                    if (mAllTalks != null) {
                        for (int i = 0; i < mAllTalks.size() ; i++) {
                            Talk t = mAllTalks.get(i);
                            if (Arrays.asList(t.tags).contains(mSelectedDay)) {
                                mTalks.add(t);
                            }
                        }
                    }
                }
                return mTalks.size();
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_talks;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 1:
                mSelectedDay = Talk.DAY_TWO;
                break;
            default:
                mSelectedDay = Talk.DAY_ONE;
                break;
        }
        mTalks = null;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onRefresh() {
        Bundle options = new Bundle();
        options.putBoolean(FORCE_REFRESH, true);
        getSupportLoaderManager().restartLoader(LOAD_TALKS, options, this);
    }

    @Override
    public Loader<List<Talk>> onCreateLoader(int id, Bundle args) {
        return new ATTalkAndSpeakerLoaderWithRefreshLayout<List<Talk>>(this, getRefreshLayout(), args != null && args.getBoolean(FORCE_REFRESH, false)) {
            public List<Talk> getTalks() throws SQLException{
                return mHelper.getTalkDAO().queryBuilder().orderBy(Talk.TIME_COLUMN, true).query();
            }

            @Override
            public List<Talk> loadInBackground() {
                try {
                    List<Talk> talks = getTalks();
                    if (talks == null || talks.isEmpty() || isForceRefresh()) {
                        loadTalksAndSessionAndSave();
                        talks = getTalks();
                    }
                    return talks;
                } catch (SQLException|IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Talk>> loader, List<Talk> data) {
        if (data == null || data.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.common_error)
                    .setMessage(R.string.common_error_couldnt_load_talks)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();
        } else {
            mAllTalks = data;
            mTalks = null;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Talk>> loader) {

    }

    private static class TalkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvName;
        private final TextView tvTime;
        private final TextView tvSpeaker;
        private Talk mTalk;
        private Context mContext;

        public TalkViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvSpeaker = (TextView) itemView.findViewById(R.id.tvSpeaker);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, TalkDetailActivity.class);
            intent.putExtra(TalkDetailActivity.TALK_ID, mTalk.id);
            mContext.startActivity(intent);
        }

        public void setTalk(Context context, Talk talk) {
            mTalk = talk;
            mContext = context;
            tvName.setText(talk.name);
            tvTime.setText(talk.time);
            tvSpeaker.setText(talk.getSpeakers());
        }
    }
}
