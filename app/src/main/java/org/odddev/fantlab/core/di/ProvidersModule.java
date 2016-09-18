package org.odddev.fantlab.core.di;

import org.odddev.fantlab.auth.AuthProvider;
import org.odddev.fantlab.auth.IAuthProvider;

import dagger.Module;
import dagger.Provides;

/**
 * @author kenrube
 * @date 15.09.16
 */

@Module
public class ProvidersModule {

    @Provides
    IAuthProvider provideUserProvider() {
        return new AuthProvider();
    }
}
