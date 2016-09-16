package org.odddev.fantlab.core.di;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author kenrube
 * @date 16.09.16
 */

@Module
public class PresentersModule {

    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }
}
