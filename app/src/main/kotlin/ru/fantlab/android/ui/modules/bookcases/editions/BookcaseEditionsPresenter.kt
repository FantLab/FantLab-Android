package ru.fantlab.android.ui.modules.bookcases.editions

import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import io.reactivex.functions.Consumer
import ru.fantlab.android.provider.rest.DataManager
import io.reactivex.Single
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.data.dao.model.BookcaseFilm
import ru.fantlab.android.data.dao.model.BookcaseWork
import ru.fantlab.android.data.dao.response.BookcaseEditionsResponse
import ru.fantlab.android.data.dao.response.BookcaseFilmsResponse
import ru.fantlab.android.data.dao.response.BookcaseWorksResponse
import ru.fantlab.android.data.dao.response.DeleteBookcaseResponse
import ru.fantlab.android.provider.rest.getBookcasePath
import ru.fantlab.android.provider.storage.DbProvider

class BookcaseEditionsPresenter : BasePresenter<BookcaseEditionsMvp.View>(), BookcaseEditionsMvp.Presenter {
    override fun getEditions(force: Boolean, bookcaseId: Int) {
        makeRestCall(
                getEditionsInternal(force, bookcaseId).toObservable(),
                Consumer { editions -> sendToView { it.onNotifyEditionsAdapter(editions) } }
        )
    }

    override fun getWorks(force: Boolean, bookcaseId: Int) {
        makeRestCall(
                getWorksInternal(force, bookcaseId).toObservable(),
                Consumer { works -> sendToView { it.onNotifyWorksAdapter(works) } }
        )
    }

    override fun getFilms(force: Boolean, bookcaseId: Int) {
        makeRestCall(
                getFilmsInternal(force, bookcaseId).toObservable(),
                Consumer { films -> sendToView { it.onNotifyFilmsAdapter(films) } }
        )
    }

    override fun deleteBookcase(bookcaseId: Int, userId: Int) {
        makeRestCall(
                DataManager.deleteBookcase(bookcaseId, userId).toObservable(),
                Consumer { response ->
                    val result = DeleteBookcaseResponse.Parser().parse(response)
                    if (result != null) {
                        sendToView { it.onSuccessfullyDeleted() }
                    } else {
                        sendToView { it.showErrorMessage(response) }
                    }
                }
        )
    }

    private fun getEditionsInternal(force: Boolean, bookcaseId: Int) =
            getEditionsFromServer(bookcaseId)
                    .onErrorResumeNext { throwable ->
                        if (!force) {
                            getEditionsFromDb(bookcaseId)
                        } else {
                            throw throwable
                        }
                    }

    private fun getEditionsFromServer(bookcaseId: Int): Single<ArrayList<BookcaseEdition>> =
            DataManager.getBookcaseEditions(bookcaseId)
                    .map { getEditions(it) }

    private fun getEditionsFromDb(bookcaseId: Int): Single<ArrayList<BookcaseEdition>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getBookcasePath(bookcaseId, "edition"))
                    .map { it.response }
                    .map { BookcaseEditionsResponse.Deserializer(perPage = 50).deserialize(it) }
                    .map { getEditions(it) }

    private fun getEditions(response: BookcaseEditionsResponse): ArrayList<BookcaseEdition> = response.editions

    private fun getWorksInternal(force: Boolean, bookcaseId: Int) =
            getWorksFromServer(bookcaseId)
                    .onErrorResumeNext { throwable ->
                        if (!force) {
                            getWorksFromDb(bookcaseId)
                        } else {
                            throw throwable
                        }
                    }

    private fun getWorksFromServer(bookcaseId: Int): Single<ArrayList<BookcaseWork>> =
            DataManager.getBookcaseWorks(bookcaseId)
                    .map { getWorks(it) }

    private fun getWorksFromDb(bookcaseId: Int): Single<ArrayList<BookcaseWork>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getBookcasePath(bookcaseId, "work"))
                    .map { it.response }
                    .map { BookcaseWorksResponse.Deserializer(perPage = 50).deserialize(it) }
                    .map { getWorks(it) }

    private fun getWorks(response: BookcaseWorksResponse): ArrayList<BookcaseWork> = response.works

    private fun getFilmsInternal(force: Boolean, bookcaseId: Int) =
            getFilmsFromServer(bookcaseId)
                    .onErrorResumeNext { throwable ->
                        if (!force) {
                            getFilmsFromDb(bookcaseId)
                        } else {
                            throw throwable
                        }
                    }

    private fun getFilmsFromServer(bookcaseId: Int): Single<ArrayList<BookcaseFilm>> =
            DataManager.getBookcaseFilms(bookcaseId)
                    .map { getFilms(it) }

    private fun getFilmsFromDb(bookcaseId: Int): Single<ArrayList<BookcaseFilm>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getBookcasePath(bookcaseId, "film"))
                    .map { it.response }
                    .map { BookcaseFilmsResponse.Deserializer(perPage = 50).deserialize(it) }
                    .map { getFilms(it) }

    private fun getFilms(response: BookcaseFilmsResponse): ArrayList<BookcaseFilm> = response.films
}