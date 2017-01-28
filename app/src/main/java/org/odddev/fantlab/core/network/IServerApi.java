package org.odddev.fantlab.core.network;

import org.odddev.fantlab.award.AwardRes;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Single;

/**
 * @author kenrube
 * @since 15.09.16
 */

public interface IServerApi {

    @FormUrlEncoded
    @POST("/login")
    Single<Response<ResponseBody>> login(
            @Field("login") String login,
            @Field("password") String password);

    @GET("/awards.json")
    Single<List<AwardRes>> getAwards();

    @GET("/award{id}.json")
    Single<AwardRes> getAward(@Path("id") int id);
}
