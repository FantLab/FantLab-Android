package org.odddev.fantlab.core.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.odddev.fantlab.BuildConfig;
import org.odddev.fantlab.award.Award;
import org.odddev.fantlab.award.AwardDeserializer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * @author kenrube
 * @since 15.09.16
 */

class ServerApiBuilder {

    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static final String HTTP_LOG_TAG = "OkHttp";

    static IServerApi createApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new StethoInterceptor());

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Timber.tag(HTTP_LOG_TAG).d(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        builder.followRedirects(false);

        OkHttpClient httpClient = builder.build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient);

        return retrofitBuilder.build().create(IServerApi.class);
    }

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Award.class, new AwardDeserializer())
                .create();
    }
}
