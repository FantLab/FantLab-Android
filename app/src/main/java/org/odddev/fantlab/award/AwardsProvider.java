package org.odddev.fantlab.award;

import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.network.IServerApi;
import org.odddev.fantlab.core.rx.ISchedulersResolver;

import java.util.List;

import javax.inject.Inject;

import rx.Single;

/**
 * @author kenrube
 * @since 11.12.16
 */

public class AwardsProvider implements IAwardsProvider {

	@Inject
	ISchedulersResolver schedulersResolver;

	@Inject
	IServerApi serverApi;

	public AwardsProvider() {
		Injector.INSTANCE.getAppComponent().inject(this);
	}

	@Override
	public Single<List<Award>> getAwards() {
		return serverApi.getAwards()
				.compose(schedulersResolver.applyDefaultSchedulers());
	}
}
