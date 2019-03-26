package ru.fantlab.android.ui.modules.bookcases.viewer

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
import timber.log.Timber

class BookcaseViewerPresenter : BasePresenter<BookcaseViewerMvp.View>(), BookcaseViewerMvp.Presenter {
    private var page: Int = 1
    private var previousTotal: Int = 0
    private var lastPage: Int = Integer.MAX_VALUE
    private var bookcaseType: String = ""

    override fun setBookcaseType(type: String) {
        bookcaseType = type
    }

    override fun onCallApi(page: Int, bookcaseId: Int?): Boolean {
        Timber.d("onCallApi with page=$page")
        if (page == 1) {
            lastPage = Integer.MAX_VALUE
            sendToView { it.getLoadMore().reset() }
        }
        setCurrentPage(page)
        if (page > lastPage || lastPage == 0) {
            sendToView { it.hideProgress() }
            return false
        }
        when (bookcaseType) {
            "edition" -> {
                getEditions(false, bookcaseId!!, page)
            }
            "work" -> {
                getWorks(false, bookcaseId!!, page)
            }
            "film" -> {
                getFilms(false, bookcaseId!!, page)
            }
        }

        return true
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

    override fun getEditions(force: Boolean, bookcaseId: Int, page: Int) {
        makeRestCall(
                getEditionsInternal(force, bookcaseId, page - 1).toObservable(),
                Consumer { (editions, totalCount, lastPage) ->
                    Timber.d("Editions received=${editions}, totalCount=${totalCount}, lastPage=${lastPage}")
                    this.lastPage = lastPage
                    sendToView {
                        with (it) {
                            onNotifyEditionsAdapter(editions, page)
                        }
                    }
                }
        )
    }

    private fun getEditionsInternal(force: Boolean, bookcaseId: Int, page: Int) =
            getEditionsFromServer(bookcaseId, page)
                    .onErrorResumeNext { throwable ->
                        if (page == 1 && !force) {
                            getEditionsFromDb(bookcaseId, page)
                        } else {
                            throw throwable
                        }
                    }

    private fun getEditionsFromServer(bookcaseId: Int, page: Int): Single<Triple<ArrayList<BookcaseEdition>, Int, Int>> =
            DataManager.getBookcaseEditions(bookcaseId, page * 10)
                    .map { getEditions(it) }

    private fun getEditionsFromDb(bookcaseId: Int, page: Int): Single<Triple<ArrayList<BookcaseEdition>, Int, Int>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getBookcasePath(bookcaseId, "edition", page * 10))
                    .map { it.response }
                    .map { BookcaseEditionsResponse.Deserializer(perPage = 10).deserialize(it) }
                    .map { getEditions(it) }

    private fun getEditions(response: BookcaseEditionsResponse): Triple<ArrayList<BookcaseEdition>, Int, Int> =
            Triple(response.editions.items, response.editions.totalCount, response.editions.last)

    override fun getWorks(force: Boolean, bookcaseId: Int, page: Int) {
        makeRestCall(
                getWorksInternal(force, bookcaseId, page - 1).toObservable(),
                Consumer { (works, totalCount, lastPage) ->
                    this.lastPage = lastPage
                    sendToView {
                        with (it) {
                            onNotifyWorksAdapter(works, page)
                        }
                    }
                }
        )
    }

    private fun getWorksInternal(force: Boolean, bookcaseId: Int, page: Int) =
            getWorksFromServer(bookcaseId, page * 10)
                    .onErrorResumeNext { throwable ->
                        if (page == 1 && !force) {
                            getWorksFromDb(bookcaseId, page)
                        } else {
                            throw throwable
                        }
                    }

    private fun getWorksFromServer(bookcaseId: Int, page: Int): Single<Triple<ArrayList<BookcaseWork>, Int, Int>> =
            DataManager.getBookcaseWorks(bookcaseId, page)
                    .map { getWorks(it) }

    private fun getWorksFromDb(bookcaseId: Int, page: Int): Single<Triple<ArrayList<BookcaseWork>, Int, Int>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getBookcasePath(bookcaseId, "work", page * 10))
                    .map { it.response }
                    .map { BookcaseWorksResponse.Deserializer(perPage = 10).deserialize(it) }
                    .map { getWorks(it) }

    private fun getWorks(response: BookcaseWorksResponse): Triple<ArrayList<BookcaseWork>, Int, Int> =
            Triple(response.works.items, response.works.totalCount, response.works.last)

    override fun getFilms(force: Boolean, bookcaseId: Int, page: Int) {
        makeRestCall(
                getFilmsInternal(force, bookcaseId, page - 1).toObservable(),
                    Consumer { (films, totalCount, lastPage) ->
                    this.lastPage = lastPage
                    sendToView {
                        with (it) {
                            onNotifyFilmsAdapter(films, page)
                        }
                    }
                }
        )
    }

    private fun getFilmsInternal(force: Boolean, bookcaseId: Int, page: Int) =
            getFilmsFromServer(bookcaseId, page)
                    .onErrorResumeNext { throwable ->
                        if (page == 1 && !force) {
                            getFilmsFromDb(bookcaseId, page)
                        } else {
                            throw throwable
                        }
                    }

    private fun getFilmsFromServer(bookcaseId: Int, page: Int): Single<Triple<ArrayList<BookcaseFilm>, Int, Int>> =
            DataManager.getBookcaseFilms(bookcaseId, page * 10)
                    .map { getFilms(it) }

    private fun getFilmsFromDb(bookcaseId: Int, page: Int): Single<Triple<ArrayList<BookcaseFilm>, Int, Int>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getBookcasePath(bookcaseId, "film", page * 10))
                    .map { it.response }
                    .map { BookcaseFilmsResponse.Deserializer(perPage = 10).deserialize(it) }
                    .map { getFilms(it) }

    private fun getFilms(response: BookcaseFilmsResponse): Triple<ArrayList<BookcaseFilm>, Int, Int> =
            Triple(response.films.items, response.films.totalCount, response.films.last)

    override fun getCurrentPage(): Int = page

    override fun getPreviousTotal(): Int = previousTotal

    override fun setCurrentPage(page: Int) {
        this.page = page
    }

    override fun setPreviousTotal(previousTotal: Int) {
        this.previousTotal = previousTotal
    }
}