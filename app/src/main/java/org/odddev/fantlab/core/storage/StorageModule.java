package org.odddev.fantlab.core.storage;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author kenrube
 * @since 29.09.16
 */

@Module
public class StorageModule {

    @Provides
    @Singleton
    StorageManager getStorageManager(Context context) {
        return new StorageManager(context);
    }
}
