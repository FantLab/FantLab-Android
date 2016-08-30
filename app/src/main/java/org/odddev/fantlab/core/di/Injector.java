package org.odddev.fantlab.core.di;

import android.support.annotation.NonNull;

/**
 * Developer: Ivan Zolotarev
 * Date: 23.08.16
 */

public class Injector {

    private static AppComponent sAppComponent;

    public static void init(@NonNull AppComponent appComponent) {
        sAppComponent = appComponent;
    }

    @NonNull
    public static AppComponent getAppComponent() {
        if (sAppComponent == null) throw new RuntimeException("AppComponent not initialized yet!");
        return sAppComponent;
    }
}
