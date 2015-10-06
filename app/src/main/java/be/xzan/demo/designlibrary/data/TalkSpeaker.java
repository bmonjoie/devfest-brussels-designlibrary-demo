package be.xzan.demo.designlibrary.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created on 6/10/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
@DatabaseTable(tableName = "talk_speaker")
public class TalkSpeaker {

    @DatabaseField(id = true, useGetSet = true)
    public String id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    public Speaker speaker;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    public Talk talk;

    public TalkSpeaker() {}

    public TalkSpeaker(Speaker speaker, Talk talk) {
        this.speaker = speaker;
        this.talk = talk;
    }

    public String getId() {
        return String.valueOf(speaker.id)+"%%"+String.valueOf(talk.id);
    }

    public void setId(String id) {
        this.id = id;
    }
}
