package org.odddev.fantlab.core.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import org.odddev.fantlab.author.AuthorResponse
import org.odddev.fantlab.author.models.AuthorStat

@Dao
abstract class AuthorStatDao {

	@Query("SELECT * FROM autor_stats WHERE autor_id = :authorId")
	abstract fun getInBg(authorId: Int): Flowable<AuthorStat>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun upsert(authorStat: AuthorStat)

	@Transaction
	open fun saveAuthorStatFromResponse(response: AuthorResponse) {
		// todo где недостающие значения?
		val authorStat = AuthorStat(
				authorId = response.authorId,
				midMark = null,
				marking = null,
				markCount = response.stat?.markCount,
				userCount = null,
				responseCount = response.stat?.responseCount,
				editionCount = response.stat?.editionCount,
				movieCount = response.stat?.movieCount,
				awardCount = response.stat?.awardCount,
				nominationCount = null
		)
		upsert(authorStat)
	}
}