package org.odddev.fantlab.award;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.odddev.fantlab.core.di.Injector;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author kenrube
 * @since 11.12.16
 */

@InjectViewState
public class AwardsPresenter extends MvpPresenter<IAwardsView> {

	private List<Award> awards;

	@Inject
	CompositeSubscription compositeSubscription;

	@Inject
	IAwardsProvider awardsProvider;

	public AwardsPresenter() {
		Injector.INSTANCE.getAppComponent().inject(this);
	}

	void getAwards() {
		if (awards != null) {
			showAwards(awards);
		} else {
			compositeSubscription.add(awardsProvider
					.getAwards()
					.subscribe(
							awards -> {
								this.awards = awards;
								showAwards(awards);
							},
							throwable -> showError(throwable.getLocalizedMessage())));
		}
	}

	private void showAwards(List<Award> awards) {
		getViewState().showAwards(awards);
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
