package org.odddev.fantlab.core.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import org.odddev.fantlab.author.AuthorResponse
import org.odddev.fantlab.author.models.AuthorPseudonym

@Dao
abstract class AuthorPseudonymDao {

	@Query("SELECT * FROM autor_pseudonyms WHERE autor_id = :authorId")
	abstract fun getAsFlowable(authorId: Int): Flowable<AuthorPseudonym>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun upsert(authorPseudonyms: Collection<AuthorPseudonym>)

	@Transaction
	open fun saveAuthorPseudonymsFromResponse(response: AuthorResponse) {
		val authorId = response.authorId
		val authorPseudonyms = ArrayList<AuthorPseudonym>()
		response.pseudonyms?.map {
			// todo где недостающие значения?
			val authorPseudonym = AuthorPseudonym(
					authorId = authorId,
					shortRusName = null,
					rusName = it.name,
					rusNameRp = null,
					name = it.nameOrig,
					isReal = it.isReal == 1
			)
			authorPseudonyms.add(authorPseudonym)
		}
		upsert(authorPseudonyms)
	}
}