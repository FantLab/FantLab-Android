package ru.fantlab.android.data.db.response

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.helper.PrefGetter
import java.util.concurrent.TimeUnit

@Dao
abstract class ResponseDao {

	private val month = TimeUnit.DAYS.toMillis(30)

	@Query("SELECT * FROM response WHERE url = :url AND userId = :userId AND api_version = '${BuildConfig.API_VERSION}' LIMIT 1")
	abstract fun getSync(url: String, userId: Int): Response?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun save(response: Response)

	fun get(url: String): Single<Response> {
		val userId = PrefGetter.getSessionUserId()
		val response = getSync(url, userId)
		return if (response != null) {
			if (System.currentTimeMillis() - response.timeStamp < month) {
				Single.just(response)
			} else {
				return Single.error(OutdatedCacheException(url))
			}
		} else {
			return Single.error(MissingCacheException(url))
		}
	}
}

class MissingCacheException(url: String) :
		IllegalStateException("Missing cache record in DB for url $url")

class OutdatedCacheException(url: String) :
		IllegalStateException("Outdated cache record in DB for url $url")