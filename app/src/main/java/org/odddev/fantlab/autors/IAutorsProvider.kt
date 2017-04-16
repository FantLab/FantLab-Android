package org.odddev.fantlab.autors

import rx.Single

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IAutorsProvider {

	fun getAutors(): Single<AutorsResponse>
}
