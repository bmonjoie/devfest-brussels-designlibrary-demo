package be.xzan.demo.designlibrary.data;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created on 29/09/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
@DatabaseTable(tableName =  "talks")
public class Talk {

    public static final String DAY_ONE = "Day 1";
    public static final String DAY_TWO = "Day 2";
    public static final String TIME_COLUMN = "time";

    @SerializedName("id")
    @DatabaseField(id = true)
    public int id;
    @SerializedName("title")
    @DatabaseField
    public String name;
    @SerializedName("description")
    @DatabaseField
    public String description;
    @SerializedName("tags")
    @DatabaseField(dataType= DataType.SERIALIZABLE)
    public String[] tags;
    @SerializedName("time")
    @DatabaseField
    public String time;
    @SerializedName("language")
    @DatabaseField
    public String language;
    @SerializedName("complexity")
    @DatabaseField
    public String level;
    @SerializedName("presentation")
    @DatabaseField
    public String presentation;

    @SerializedName("speakers")
    public int[] speakers_id;

    @SerializedName("dummy")
    @ForeignCollectionField(eager = true)
    public Collection<TalkSpeaker> talkSpeakers;

    public Talk() {}

    public String getSpeakers() {
        String s = "";
        for (TalkSpeaker ts : talkSpeakers) {
            if (!s.isEmpty()) {
                s += ", ";
            }
            s+=ts.speaker.name;
        }
        return s;
    }

    public Speaker[] getSpeakerArray() {
        if (talkSpeakers == null || talkSpeakers.isEmpty()) {
            return new Speaker[0];
        }

        Speaker[] array = new Speaker[talkSpeakers.size()];
        int i = 0;
        for (TalkSpeaker ts : talkSpeakers) {
            array[i++] = ts.speaker;
        }
        return array;
    }
}
