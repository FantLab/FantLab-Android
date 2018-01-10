package ru.fantlab.android.old.core.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import ru.fantlab.android.old.author.AuthorResponse
import ru.fantlab.android.old.author.models.Author
import ru.fantlab.android.old.authors.AuthorInList
import ru.fantlab.android.old.authors.AuthorsResponse

@Dao
abstract class AuthorDao {

	@Query("SELECT autor_id, shortrusname, rusname, name FROM autors WHERE is_opened = 1 ORDER BY shortrusname")
	abstract fun getAllAsFlowable(): Flowable<List<AuthorInList>>

	@Query("SELECT * FROM autors WHERE autor_id = :authorId")
	abstract fun get(authorId: Int): Author?

	@Query("SELECT * FROM autors WHERE autor_id = :authorId")
	abstract fun getAsFlowable(authorId: Int): Flowable<Author>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	abstract fun upsert(authors: Collection<Author>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun upsert(author: Author)

	@Transaction
	open fun saveAuthorsFromResponse(response: AuthorsResponse) {
		val authors = response.list
				.map { author -> Author(
						authorId = author.authorId,
						shortRusName = author.nameShort,
						rusName = author.name,
						rusNameRp = author.nameRp,
						name = author.nameOrig,
						isFv = author.isFv == 1,
						isOpened = true
				) }
		upsert(authors)
	}

	@Transaction
	open fun saveWorksAuthorsFromResponse(response: AuthorResponse) {
		val workAuthors = response.getWorkAuthors()
		val detailedWorkAuthors = HashSet<Author>()
		for ((id, author) in workAuthors) {
			detailedWorkAuthors.add(get(id) ?: author)
		}
		upsert(detailedWorkAuthors)
	}

	@Transaction
	open fun saveAuthorFromResponse(response: AuthorResponse) {
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
				isFv = shortAuthor?.isFv,
				isOpened = response.isOpened == 1,
				curator = response.curator,
				compiler = response.compiler,
				fantastic = response.fantastic,
				lastModified = response.lastModified
		)
		upsert(author)
	}
}