package be.xzan.demo.designlibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import be.xzan.demo.designlibrary.data.TalkSpeaker;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import be.xzan.demo.designlibrary.data.Partner;
import be.xzan.demo.designlibrary.data.Speaker;
import be.xzan.demo.designlibrary.data.Talk;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "devfest.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Speaker, Integer> mSpeakerDAO = null;
    private Dao<Talk, Integer> mTalkDAO = null;
    private Dao<Partner, String> mPartnerDAO = null;
    private Dao<TalkSpeaker, String> mTalkSpeakerDAO;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Speaker.class);
            TableUtils.createTable(connectionSource, Speaker.SocialLink.class);
            TableUtils.createTable(connectionSource, Talk.class);
            TableUtils.createTable(connectionSource, TalkSpeaker.class);
            TableUtils.createTable(connectionSource, Partner.class);
            TableUtils.createTable(connectionSource, Partner.Logo.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Partner.Logo.class, true);
            TableUtils.dropTable(connectionSource, Speaker.SocialLink.class, true);
            TableUtils.dropTable(connectionSource, Partner.class, true);
            TableUtils.dropTable(connectionSource, Talk.class, true);
            TableUtils.dropTable(connectionSource, TalkSpeaker.class, true);
            TableUtils.dropTable(connectionSource, Speaker.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Speaker, Integer> getSpeakerDAO() throws SQLException {
        if (mSpeakerDAO == null) {
            mSpeakerDAO = getDao(Speaker.class);
        }
        return mSpeakerDAO;
    }

    public Dao<Talk, Integer> getTalkDAO() throws SQLException {
        if (mTalkDAO == null) {
            mTalkDAO = getDao(Talk.class);
        }
        return mTalkDAO;
    }

    public Dao<Partner, String> getPartnerDAO() throws SQLException {
        if (mPartnerDAO == null) {
            mPartnerDAO = getDao(Partner.class);
        }
        return mPartnerDAO;
    }

    public Dao<TalkSpeaker, String> getTalkSpeakerDAO() throws SQLException {
        if (mTalkSpeakerDAO == null) {
            mTalkSpeakerDAO = getDao(TalkSpeaker.class);
        }
        return mTalkSpeakerDAO;
    }

    @Override
    public void close() {
        super.close();
        mSpeakerDAO = null;
        mTalkDAO = null;
        mPartnerDAO = null;
    }
}