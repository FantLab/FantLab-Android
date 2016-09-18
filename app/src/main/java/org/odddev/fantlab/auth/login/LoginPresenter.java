package org.odddev.fantlab.auth.login;

import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.layers.presenter.Presenter;
import org.odddev.fantlab.auth.IAuthProvider;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author kenrube
 * @date 30.08.16
 */

public class LoginPresenter extends Presenter<ILoginView> {

    private Subscription mLoginSubscription;

    @Inject
    CompositeSubscription mCompositeSubscription;

    @Inject
    IAuthProvider mUserProvider;

    public LoginPresenter() {
        Injector.getAppComponent().inject(this);
    }

    public void login(String login, String password) {
        mLoginSubscription = mUserProvider
                .login(login, password)
                .subscribe(
                        aVoid -> showSuccess(),
                        throwable -> Timber.e(throwable.getLocalizedMessage()),
                        () -> mCompositeSubscription.remove(mLoginSubscription));
        mCompositeSubscription.add(mLoginSubscription);
    }

    private void showSuccess() {
        for (ILoginView view : getViews()) {
            view.showLoggedIn();
        }
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
