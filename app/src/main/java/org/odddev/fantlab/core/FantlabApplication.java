package org.odddev.fantlab.core;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class FantlabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initFabric();
    }

    private void initFabric() {
        Fabric.with(this, new Crashlytics());
    }
}
