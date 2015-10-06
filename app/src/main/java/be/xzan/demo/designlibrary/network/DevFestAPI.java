package be.xzan.demo.designlibrary.network;

import be.xzan.demo.designlibrary.data.Partner;
import be.xzan.demo.designlibrary.data.Speaker;
import be.xzan.demo.designlibrary.data.Talk;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created on 4/10/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public interface DevFestAPI {
    @GET("/data/speakers.json")
    Call<List<Speaker>> listSpeakers();

    @GET("/data/sessions.json")
    Call<List<Talk>> listTalks();

    @GET("/data/partners.json")
    Call<List<Partner>> listPartners();
}
