package org.odddev.fantlab.core.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import org.odddev.fantlab.author.models.Author
import org.odddev.fantlab.authors.AuthorsResponse

@Dao
abstract class AuthorDao {

	@Query("SELECT * FROM authors")
	abstract fun get(): Flowable<List<Author>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun upsert(author: Collection<Author>)

	@Transaction
	open fun saveFromNetwork(response: AuthorsResponse): Flowable<List<Author>> {
		val authors = response.list
				.map { author -> Author(
						authorId = author.authorId.toInt(),
						isFv = author.isFv.toInt() == 1,
						rusName = author.name,
						name = author.nameOrig,
						rusNameRp = author.nameRp,
						shortRusName = author.nameShort
				) }
		upsert(authors)
		return get()
	}
}