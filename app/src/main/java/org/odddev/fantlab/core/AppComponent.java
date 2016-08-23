package org.odddev.fantlab.core;

import org.odddev.fantlab.core.network.NetworkChecker;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Developer: Ivan Zolotarev
 * Date: 23.08.16
 */

@Singleton
@Component(modules = {
        AppModule.class
})
public interface AppComponent {

    void inject(NetworkChecker networkChecker);
}
