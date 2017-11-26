package org.odddev.fantlab.core.db

import android.arch.persistence.room.*
import com.google.gson.Gson
import io.reactivex.Flowable
import org.odddev.fantlab.author.AuthorResponse
import org.odddev.fantlab.author.models.Author
import org.odddev.fantlab.authors.AuthorInList
import org.odddev.fantlab.authors.AuthorsResponse
import timber.log.Timber

@Dao
abstract class AuthorDao {

	@Query("SELECT autor_id, shortrusname, rusname, name FROM authors ORDER BY _index")
	abstract fun getByOrder(): Flowable<List<AuthorInList>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun upsert(author: Collection<Author>)

	@Transaction
	open fun saveAuthorsFromResponse(response: AuthorsResponse): Flowable<List<AuthorInList>> {
		val authors = response.list
				.withIndex()
				.map { author -> Author(
						authorId = author.value.authorId,
						isFv = author.value.isFv == 1,
						rusName = author.value.name,
						name = author.value.nameOrig,
						rusNameRp = author.value.nameRp,
						shortRusName = author.value.nameShort,
						index = author.index
				) }
		upsert(authors)
		return getByOrder()
	}

	@Transaction
	open fun saveAuthorFromResponse(response: AuthorResponse): Flowable<Void> {
		Timber.d(Gson().toJson(response))
		return Flowable.just(null)
	}
}