package org.odddev.fantlab.award;

import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.network.IServerApi;
import org.odddev.fantlab.core.rx.ISchedulersResolver;

import java.util.ArrayList;
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
        Injector.getAppComponent().inject(this);
    }

    @Override
    public Single<List<AwardDto>> getAwards() {
        return serverApi.getAwards()
                .map(resList -> {
                    List<AwardDto> result = new ArrayList<>();
                    for (AwardRes res : resList) {
                        result.add(new AwardDto(res));
                    }
                    return result;
                })
                .compose(schedulersResolver.applyDefaultSchedulers());
    }
}
