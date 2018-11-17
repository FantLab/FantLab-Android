package ru.fantlab.android.data.db.response

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Single
import ru.fantlab.android.BuildConfig

@Dao
abstract class ResponseDao {

	@Query("SELECT * FROM response WHERE url LIKE :url AND api_version LIKE '${BuildConfig.API_VERSION}' LIMIT 1")
	abstract fun getSync(url: String): Response?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun save(response: Response)

	fun get(url: String): Single<Optional<Response>> = Single.just(getSync(url).toOptional())
}