package org.odddev.fantlab.core.network;

import org.odddev.fantlab.profile.User;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Developer: Ivan Zolotarev
 * Date: 15.09.16
 */

public interface IServerApi {

    @POST("/login")
    Observable<ResponseBody> login(@Body User request);
}
