package org.odddev.fantlab.core.di;

import org.odddev.fantlab.core.network.NetworkChecker;
import org.odddev.fantlab.core.rx.ConfiguratorProvider;

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

    void inject(ConfiguratorProvider configurationProvider);
}
