package org.odddev.fantlab.core.di;

import org.odddev.fantlab.auth.LoginPresenter;
import org.odddev.fantlab.core.network.NetworkChecker;
import org.odddev.fantlab.core.network.NetworkModule;
import org.odddev.fantlab.core.rx.ConfiguratorProvider;
import org.odddev.fantlab.profile.UserProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author kenrube
 * @date 23.08.16
 */

@Singleton
@Component(modules = {
        AppModule.class,
        ProvidersModule.class,
        PresentersModule.class,
        NetworkModule.class
})
public interface AppComponent {

    void inject(NetworkChecker networkChecker);

    void inject(ConfiguratorProvider configurationProvider);

    void inject(LoginPresenter loginPresenter);

    void inject(UserProvider userProvider);
}
