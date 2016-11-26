package org.odddev.fantlab.auth.login;

import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.layers.presenter.Presenter;
import org.odddev.fantlab.auth.IAuthProvider;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author kenrube
 * @since 30.08.16
 */

public class LoginPresenter extends Presenter<ILoginView> {

    private Subscription loginSubscription;

    @Inject
    CompositeSubscription compositeSubscription;

    @Inject
    IAuthProvider authProvider;

    LoginPresenter() {
        Injector.getAppComponent().inject(this);
    }

    public void login(LoginValidator validator) {
        if (validator.areFieldsValid()) {
            loginSubscription = authProvider
                    .login(validator.fields.get(LoginValidator.FIELD.USERNAME),
                            validator.fields.get(LoginValidator.FIELD.PASSWORD))
                    .subscribe(
                            this::showResult,
                            throwable -> showError(throwable.getLocalizedMessage()),
                            () -> compositeSubscription.remove(loginSubscription));
            compositeSubscription.add(loginSubscription);
        } else {
            showFieldsValid();
        }
    }

    private void showResult(boolean hasCookie) {
        for (ILoginView view : getViews()) {
            view.showLoginResult(hasCookie);
        }
    }

    private void showError(String error) {
        for (ILoginView view : getViews()) {
            view.showError(error);
        }
    }

    private void showFieldsValid() {
        for (ILoginView view : getViews()) {
            view.showFieldsValid();
        }
    }

    @Override
    protected void onDestroy() {
        compositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
