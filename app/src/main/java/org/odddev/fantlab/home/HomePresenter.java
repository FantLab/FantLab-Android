package org.odddev.fantlab.home;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.odddev.fantlab.core.di.Injector;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author kenrube
 * @since 11.12.16
 */

@InjectViewState
public class HomePresenter extends MvpPresenter<IHomeView> {

    @Inject
    IHomeProvider provider;

    @Inject
    CompositeSubscription compositeSubscription;

    public HomePresenter() {
        Injector.getAppComponent().inject(this);
    }

    void getUserName() {
        compositeSubscription.add(provider.getUserName()
                .subscribe(
                        this::showUserName,
                        throwable -> Timber.e(throwable, throwable.getLocalizedMessage())));
    }

    void clearCookie() {
        provider.clearCookie();
    }

    private void showUserName(String userName) {
        getViewState().showUserName(userName);
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
