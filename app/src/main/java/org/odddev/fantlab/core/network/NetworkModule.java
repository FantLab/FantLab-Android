package org.odddev.fantlab.core.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Developer: Ivan Zolotarev
 * Date: 15.09.16
 */

@Module
public class NetworkModule {

    @Provides
    @Singleton
    IServerApi provideServerApi() {
        return ServerApiBuilder.createApi();
    }
}
