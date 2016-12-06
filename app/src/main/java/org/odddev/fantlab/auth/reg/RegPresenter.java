package org.odddev.fantlab.auth.reg;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.odddev.fantlab.auth.IAuthProvider;
import org.odddev.fantlab.core.di.Injector;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author kenrube
 * @since 20.09.16
 */

@InjectViewState
public class RegPresenter extends MvpPresenter<IRegView> {

    private Subscription regSubscription;

    @Inject
    CompositeSubscription compositeSubscription;

    @Inject
    IAuthProvider authProvider;

    RegPresenter() {
        Injector.getAppComponent().inject(this);
    }

    public void register(RegValidator validator) {
        regSubscription = authProvider
                .register(
                        validator.fields.get(RegValidator.FIELD.USERNAME),
                        validator.fields.get(RegValidator.FIELD.PASSWORD),
                        validator.fields.get(RegValidator.FIELD.EMAIL))
                .subscribe(
                        this::showResult,
                        throwable -> showError(throwable.getLocalizedMessage()));
        compositeSubscription.add(regSubscription);
    }

    private void showResult(boolean hasCookie) {
        getViewState().showRegResult(hasCookie);
    }

    private void showError(String error) {
        getViewState().showError(error);
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
