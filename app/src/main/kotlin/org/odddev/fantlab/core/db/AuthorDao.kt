package org.odddev.fantlab.core.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import org.odddev.fantlab.author.AuthorResponse
import org.odddev.fantlab.author.models.Author
import org.odddev.fantlab.authors.AuthorInList
import org.odddev.fantlab.authors.AuthorsResponse

@Dao
abstract class AuthorDao {

	@Query("SELECT autor_id, shortrusname, rusname, name FROM authors ORDER BY _index")
	abstract fun getByOrder(): List<AuthorInList>

	@Query("SELECT autor_id, shortrusname, rusname, name FROM authors ORDER BY _index")
	abstract fun getByOrderInBg(): Flowable<List<AuthorInList>>

	@Query("SELECT * FROM authors WHERE autor_id = :authorId")
	abstract fun get(authorId: Int): Author

	@Query("SELECT * FROM authors WHERE autor_id = :authorId")
	abstract fun getInBg(authorId: Int): Flowable<Author>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	abstract fun upsert(authors: Collection<Author>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun upsert(author: Author)

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
		return getByOrderInBg()
	}

	@Transaction
	open fun saveAuthorFromResponse(response: AuthorResponse): Flowable<Author> {
		val authorId = response.authorId
		val shortAuthor = get(authorId)
		val author = Author(
				authorId = authorId,
				shortRusName = response.nameShort,
				rusName = response.name,
				rusNameRp = response.nameRp,
				name = response.nameOrig,
				sex = response.sex == "m",
				countryId = response.countryId,
				birthDay = response.birthDay,
				deathDay = response.deathDay,
				homePage = response.sites?.get(0)?.site,
				blog = null,
				anons = response.anons,
				biography = response.biography,
				biographyComment = response.biographyNotes,
				source = response.source,
				sourceLink = response.sourceLink,
				addInfo = null,
				isFv = shortAuthor.isFv,
				isOpened = response.isOpened == 1,
				curator = response.curator,
				compiler = response.compiler,
				fantastic = response.fantastic,
				lastModified = response.lastModified,
				index = shortAuthor.index
		)
		upsert(author)
		return getInBg(authorId)
	}
}