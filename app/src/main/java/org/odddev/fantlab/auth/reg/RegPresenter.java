package org.odddev.fantlab.auth.reg;

import org.odddev.fantlab.auth.IAuthProvider;
import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.layers.presenter.Presenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author kenrube
 * @since 20.09.16
 */

public class RegPresenter extends Presenter<IRegView> {

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
                        throwable -> showError(throwable.getLocalizedMessage()),
                        () -> compositeSubscription.remove(regSubscription));
        compositeSubscription.add(regSubscription);
    }

    private void showResult(boolean hasCookie) {
        for (IRegView view : getViews()) {
            view.showRegResult(hasCookie);
        }
    }

    private void showError(String error) {
        for (IRegView view : getViews()) {
            view.showError(error);
        }
    }

    @Override
    protected void onDestroy() {
        compositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
