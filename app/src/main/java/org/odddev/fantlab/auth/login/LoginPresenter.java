package org.odddev.fantlab.auth.login;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.odddev.fantlab.auth.IAuthProvider;
import org.odddev.fantlab.core.di.Injector;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author kenrube
 * @since 30.08.16
 */

@InjectViewState
public class LoginPresenter extends MvpPresenter<ILoginView> {

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
            showFieldsInvalid();
        }
    }

    private void showResult(boolean hasCookie) {
        getViewState().showLoginResult(hasCookie);
    }

    private void showError(String error) {
        getViewState().showError(error);
    }

    private void showFieldsInvalid() {
        getViewState().showFieldsInvalid();
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
