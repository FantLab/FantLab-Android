package org.odddev.fantlab.award

import rx.Single

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IAwardsProvider {

	val awards: Single<List<Award>>
}
