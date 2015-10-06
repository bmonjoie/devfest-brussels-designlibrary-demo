package be.xzan.demo.designlibrary.data;

import be.xzan.demo.designlibrary.common.config.AppConstants;
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
@DatabaseTable(tableName = "speakers")
public class Speaker {
    @SerializedName("id")
    @DatabaseField(id = true)
    public int id;
    @SerializedName("name")
    @DatabaseField
    public String name;
    @SerializedName("title")
    @DatabaseField
    public String title;
    @SerializedName("bio")
    @DatabaseField
    public String description;
    @SerializedName("company")
    @DatabaseField
    public String company;
    @SerializedName("country")
    @DatabaseField
    public String country;
    @SerializedName("photoUrl")
    @DatabaseField
    public String photoUrl;
    @SerializedName("tags")
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public String[] tags;
    @SerializedName("social")
    @ForeignCollectionField(eager = true)
    public Collection<SocialLink> socialLinks;
    @ForeignCollectionField(eager = true)
    public Collection<TalkSpeaker> talkSpeakers;

    public String getPhotoUrl() {
        if (photoUrl == null || photoUrl.startsWith("http")) {
            return photoUrl;
        }
        else {
            return AppConstants.BASE_URL+"/"+photoUrl;
        }
    }

    public Speaker() {}

    @DatabaseTable(tableName = "speaker_social_links")
    public static class SocialLink {
        @SerializedName("name")
        @DatabaseField(id = true)
        public String name;
        @SerializedName("link")
        @DatabaseField
        public String url;
        @DatabaseField(foreign = true)
        public Speaker speaker;

        public SocialLink() {}
    }

}
