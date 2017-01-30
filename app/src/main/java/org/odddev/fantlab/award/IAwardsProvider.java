package org.odddev.fantlab.award;

import java.util.List;

import rx.Single;

/**
 * @author kenrube
 * @since 11.12.16
 */

public interface IAwardsProvider {

	Single<List<Award>> getAwards();
}
