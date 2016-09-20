package org.odddev.fantlab.auth.reg;

import org.odddev.fantlab.auth.IAuthProvider;
import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.layers.presenter.Presenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author kenrube
 * @date 20.09.16
 */

public class RegPresenter extends Presenter<IRegView> {

    private Subscription mRegSubscription;

    @Inject
    CompositeSubscription mCompositeSubscription;

    @Inject
    IAuthProvider mAuthProvider;

    public RegPresenter() {
        Injector.getAppComponent().inject(this);
    }

    public void register(String login, String password, String email) {
        mRegSubscription = mAuthProvider
                .register(login, password, email)
                .subscribe(
                        aVoid -> showSuccess(),
                        throwable -> Timber.e(throwable.getLocalizedMessage()),
                        () -> mCompositeSubscription.remove(mRegSubscription));
        mCompositeSubscription.add(mRegSubscription);
    }

    private void showSuccess() {
        for (IRegView view : getViews()) {
            view.showRegistered();
        }
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
