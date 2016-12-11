package org.odddev.fantlab.core;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import org.odddev.fantlab.BuildConfig;
import org.odddev.fantlab.core.di.AppModule;
import org.odddev.fantlab.core.di.DaggerAppComponent;
import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.utils.TimberCrashlyticsTree;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class FantlabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initCrashlytics();
        initTimber();
        initStetho();
        initDagger();
    }

    private void initCrashlytics() {
        if (!BuildConfig.DEBUG) Fabric.with(this, new Crashlytics());
    }

    private void initTimber() {
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new TimberCrashlyticsTree());
    }

    private void initStetho() {
        Stetho.InitializerBuilder builder = Stetho.newInitializerBuilder(this);
        builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.Initializer initializer = builder.build();
        Stetho.initialize(initializer);
    }

    private void initDagger() {
        Injector.init(DaggerAppComponent.builder().appModule(new AppModule(this)).build());
    }
}
