package org.odddev.fantlab.autors.autor

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import org.odddev.fantlab.core.db.AuthorRecord
import org.odddev.fantlab.core.db.AuthorRecordEntity
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.models.Author
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

class AuthorProvider : IAuthorProvider {

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var requery: KotlinReactiveEntityStore<Persistable>

	init {
		Injector.getAppComponent().inject(this)
	}

	/**
	 * После получения данных алгоритм такой:
	 * 1. Для каждой записи из модели считываем из таблицы уже существующую запись с таким же primary_key
	 * (если уже загрузили, берем из памяти)
	 * 2. Поочередно сравниваем записи и выбираем наиболее полный/актуальный вариант
	 * 3. Записываем актуальный результат в базу
	 * 4. Считываем из базы записи, необходимые для отображения информации на экране
	 */
	override fun getAuthor(id: Int): Observable<Iterable<AuthorRecord>> =
			serverApi.getAuthor(id)
					.flatMap { authorPageInfo -> writeToDb(authorPageInfo) }
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())

	private fun writeToDb(authorPageInfo: AuthorPageInfo): Observable<Iterable<AuthorRecord>> {
		val authorRecords = arrayListOf<AuthorRecord>()
		val authors = hashMapOf<Int, Author>()
		for (author in authorPageInfo.authors) {
			if (isMoreComplete(author, authors[author.authorId])) {
				authors.put(author.authorId, author)

				val authorRecord = AuthorRecordEntity()
				authorRecord.apply {
					anons = author.anons
					authorId = author.authorId
					biography = author.biography
					biographyNotes = author.biographyNotes
					biographySource = author.biographySource
					biographySourceUrl = author.biographySourceUrl
					compiler = author.compiler
					countryId = author.countryId
					countryName = author.countryName
					curator = author.curator
					fantastic = author.fantastic
					isOpened = author.isOpened
					name = author.name
					nameOrig = author.nameOrig
					nameRp = author.nameRp
					nameShort = author.nameShort
					registeredUserId = author.registeredUserId
					registeredUserLogin = author.registeredUserLogin
					registeredUserSex = author.registeredUserSex
					sex = author.sex
					statAwardCount = author.statAwardCount
					statEditionCount = author.statEditionCount
					statMarkCount = author.statMarkCount
					statMovieCount = author.statMovieCount
					statResponseCount = author.statResponseCount
					statWorkCount = author.statWorkCount
				}
				authorRecords.add(authorRecord)
			}
		}
		return requery.insert(authorRecords).toObservable()
	}

	private fun isMoreComplete(newItem: Author, oldItem: Author?): Boolean {
		return oldItem == null
				|| (newItem.anons != null && oldItem.anons == null)
				|| (newItem.biography != null && oldItem.biography == null)
				|| (newItem.biographyNotes != null && oldItem.biographyNotes == null)
				|| (newItem.biographySource != null && oldItem.biographySource == null)
				|| (newItem.biographySourceUrl != null && oldItem.biographySourceUrl == null)
				|| (newItem.compiler != null && oldItem.compiler == null)
				|| (newItem.countryId != null && oldItem.countryId == null)
				|| (newItem.countryName != null && oldItem.countryName == null)
				|| (newItem.curator != null && oldItem.curator == null)
				|| (newItem.fantastic != null && oldItem.fantastic == null)
				|| (newItem.isOpened != null && oldItem.isOpened == null)
				|| (newItem.name != null && oldItem.name == null)
				|| (newItem.nameOrig != null && oldItem.nameOrig == null)
				|| (newItem.nameRp != null && oldItem.nameRp == null)
				|| (newItem.nameShort != null && oldItem.nameShort == null)
				|| (newItem.registeredUserId != null && oldItem.registeredUserId == null)
				|| (newItem.registeredUserLogin != null && oldItem.registeredUserLogin == null)
				|| (newItem.registeredUserSex != null && oldItem.registeredUserSex == null)
				|| (newItem.sex != null && oldItem.sex == null)
				|| (newItem.statAwardCount != null && oldItem.statAwardCount == null)
				|| (newItem.statEditionCount != null && oldItem.statEditionCount == null)
				|| (newItem.statMarkCount != null && oldItem.statMarkCount == null)
				|| (newItem.statMovieCount != null && oldItem.statMovieCount == null)
				|| (newItem.statResponseCount != null && oldItem.statResponseCount == null)
				|| (newItem.statWorkCount != null && oldItem.statWorkCount == null)
	}
}
