package org.odddev.fantlab.core.di;

import org.odddev.fantlab.auth.AuthProvider;
import org.odddev.fantlab.auth.login.LoginPresenter;
import org.odddev.fantlab.auth.reg.RegPresenter;
import org.odddev.fantlab.award.AwardsPresenter;
import org.odddev.fantlab.award.AwardsProvider;
import org.odddev.fantlab.core.network.HeaderInterceptor;
import org.odddev.fantlab.core.network.NetworkChecker;
import org.odddev.fantlab.core.network.NetworkModule;
import org.odddev.fantlab.core.storage.StorageModule;
import org.odddev.fantlab.launch.LaunchActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author kenrube
 * @since 23.08.16
 */

@Singleton
@Component(modules = {
        AppModule.class,
        ProvidersModule.class,
        PresentersModule.class,
        NetworkModule.class,
        StorageModule.class
})
public interface AppComponent {

    void inject(NetworkChecker networkChecker);

    void inject(LoginPresenter loginPresenter);

    void inject(AuthProvider userProvider);

    void inject(RegPresenter regPresenter);

    void inject(LaunchActivity launchActivity);

    void inject(HeaderInterceptor headerInterceptor);

    void inject(AwardsPresenter awardsPresenter);

    void inject(AwardsProvider awardsProvider);
}
