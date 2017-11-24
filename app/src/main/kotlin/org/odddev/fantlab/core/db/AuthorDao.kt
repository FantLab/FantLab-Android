package org.odddev.fantlab.core.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import org.odddev.fantlab.author.models.Author
import org.odddev.fantlab.authors.AuthorsResponse

@Dao
abstract class AuthorDao {

	// todo возвращать не всего автора, а только отображаемую в списке информацию
	@Query("SELECT * FROM authors ORDER BY _index")
	abstract fun getByOrder(): Flowable<List<Author>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun upsert(author: Collection<Author>)

	@Transaction
	open fun saveFromNetwork(response: AuthorsResponse): Flowable<List<Author>> {
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
}