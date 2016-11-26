package org.odddev.fantlab.core.di;

import android.support.annotation.NonNull;

/**
 * @author kenrube
 * @since 23.08.16
 */

public class Injector {

    private static AppComponent appComponent;

    public static void init(@NonNull AppComponent appComponent) {
        Injector.appComponent = appComponent;
    }

    @NonNull
    public static AppComponent getAppComponent() {
        if (appComponent == null) throw new RuntimeException("AppComponent not initialized yet!");
        return appComponent;
    }
}
