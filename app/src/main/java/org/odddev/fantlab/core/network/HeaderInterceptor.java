package org.odddev.fantlab.core.network;

import org.odddev.fantlab.BuildConfig;
import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.storage.StorageManager;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author kenrube
 * @since 15.09.16
 */

public class HeaderInterceptor implements Interceptor {

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String APP_VERSION = "Android/" + BuildConfig.VERSION_NAME;

    @Inject
    StorageManager storageManager;

    public HeaderInterceptor() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        String cookie = storageManager.loadCookie();

        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Content-Type", CONTENT_TYPE)
                .header("User-Agent", APP_VERSION);
        if (cookie != null) {
            builder.header("Cookie", cookie);
        }
        builder.method(original.method(), original.body());
        return chain.proceed(builder.build());
    }
}
