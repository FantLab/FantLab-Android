package org.odddev.fantlab.core.di;

import org.odddev.fantlab.profile.IUserProvider;
import org.odddev.fantlab.profile.UserProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Developer: Ivan Zolotarev
 * Date: 15.09.16
 */

@Module
public class ProvidersModule {

    @Provides
    IUserProvider provideUserProvider() {
        return new UserProvider();
    }
}
