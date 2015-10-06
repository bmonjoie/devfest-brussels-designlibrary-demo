package be.xzan.demo.designlibrary.common.loaders;

import android.content.Context;

import be.xzan.demo.designlibrary.common.config.AppConstants;
import be.xzan.demo.designlibrary.data.Speaker;
import be.xzan.demo.designlibrary.data.Talk;
import be.xzan.demo.designlibrary.data.TalkSpeaker;
import be.xzan.demo.designlibrary.database.DatabaseHelper;
import be.xzan.demo.designlibrary.network.DevFestAPI;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created on 4/10/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public abstract class ATTalkAndSpeakerLoader<D> extends ATLoader<D> {

    private static final Object LOCK = new Object();
    protected final DatabaseHelper mHelper;

    public ATTalkAndSpeakerLoader(Context context) {
        super(context);
        mHelper = new DatabaseHelper(context);
    }

    protected void loadTalksAndSessionAndSave() throws SQLException, IOException {
        synchronized (LOCK) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .baseUrl(AppConstants.BASE_URL)
                    .build();
            mHelper.getSpeakerDAO().deleteBuilder().delete();
            mHelper.getTalkDAO().deleteBuilder().delete();
            mHelper.getTalkSpeakerDAO().deleteBuilder().delete();
            DevFestAPI service = retrofit.create(DevFestAPI.class);
            List<Speaker> speakers = service.listSpeakers().execute().body();
            List<Talk> talks = service.listTalks().execute().body();
            for (int i = 0; i < talks.size(); i++) {
                Talk talk = talks.get(i);
                for (int j = 0; j < talk.speakers_id.length; j++) {
                    int id = talk.speakers_id[j];
                    for (int k = 0; k < speakers.size(); k++) {
                        Speaker speaker = speakers.get(k);
                        if (speaker.id == id) {
                            mHelper.getTalkSpeakerDAO().create(new TalkSpeaker(speaker, talk));
                            break;
                        }
                    }
                }
                mHelper.getTalkDAO().create(talks.get(i));
            }
            for (int i = 0; i < speakers.size(); i++) {
                mHelper.getSpeakerDAO().create(speakers.get(i));
            }
        }
    }
}
