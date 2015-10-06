package be.xzan.demo.designlibrary.data;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created on 4/10/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
@DatabaseTable(tableName = "partners")
public class Partner {

    @SerializedName("title")
    @DatabaseField(id = true)
    public String name;
    @SerializedName("logos")
    @ForeignCollectionField(eager = true)
    public Collection<Logo> logos;

    public Partner() {}

    @DatabaseTable(tableName = "partner_logos")
    public static class Logo {
        @SerializedName("name")
        @DatabaseField(id = true)
        public String name;
        @SerializedName("url")
        @DatabaseField
        public String url;
        @SerializedName("logoUrl")
        @DatabaseField
        public String logoUrl;
        @DatabaseField(foreign = true)
        public Partner partner;

        public Logo() {}
    }
}
