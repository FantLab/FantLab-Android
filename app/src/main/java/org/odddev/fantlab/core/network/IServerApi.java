package org.odddev.fantlab.core.network;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author kenrube
 * @date 15.09.16
 */

public interface IServerApi {

    @FormUrlEncoded
    @POST("login")
    Observable<ResponseBody> login(
            @Field("login") String login,
            @Field("password") String password);
}
