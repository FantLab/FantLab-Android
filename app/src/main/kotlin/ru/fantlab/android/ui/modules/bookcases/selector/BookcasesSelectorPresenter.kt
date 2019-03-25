package ru.fantlab.android.ui.modules.bookcases.selector

import android.os.Bundle
import android.view.View
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.data.dao.response.BookcaseEditionsResponse
import ru.fantlab.android.data.dao.response.BookcaseFilmsResponse
import ru.fantlab.android.data.dao.response.BookcaseWorksResponse
import ru.fantlab.android.data.dao.response.BookcasesResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getBookcasePath
import ru.fantlab.android.provider.rest.getUserBookcasesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import java.util.concurrent.ConcurrentHashMap

class BookcasesSelectorPresenter : BasePresenter<BookcasesSelectorMvp.View>(),
        BookcasesSelectorMvp.Presenter {

    override fun onFragmentCreated(bundle: Bundle) {
        val userId = bundle.getInt(BundleConstant.EXTRA)
        val bookcaseType = bundle.getString(BundleConstant.EXTRA_TWO)
        val entityId = bundle.getInt(BundleConstant.EXTRA_THREE)
        getBookcases(userId, bookcaseType, entityId, false)
    }

    override fun getBookcases(userId: Int, bookcaseType: String, entityId: Int, force: Boolean) {
        makeRestCall(
                getBookcasesInternal(userId, force).toObservable(),
                Consumer { bookcases ->
                    if (bookcases == null) {
                        sendToView { it.onInitViews(null) }
                    } else {
                        when (bookcaseType) {
                            "edition" -> {
                                getBookcaseEditionsDetails(bookcases.toNullable(), entityId, force)
                            }
                            "work" -> {
                                getBookcaseWorksDetails(bookcases.toNullable(), entityId, force)
                            }
                            "film" -> {
                                getBookcaseFilmsDetails(bookcases.toNullable(), entityId, force)
                            }
                        }
                    }
                }
        )
    }

    private fun getBookcaseEditionsDetails(bookcases: ArrayList<Bookcase>?, entityId: Int, force: Boolean) {
        val bookcaseDetails: ConcurrentHashMap<Bookcase, ArrayList<Int>> = ConcurrentHashMap()
        val requests: ArrayList<Observable<Any>> = arrayListOf()
        bookcases!!.forEach { bookcase ->
            if (bookcase.type == "edition") {
                val singleObservable: ArrayList<Observable<ArrayList<BookcaseEdition>>> = arrayListOf(getEditionsInternal(force, bookcase.id).toObservable())
                requests.add(Observable.combineLatest(singleObservable) { items ->
                    if (!items.isEmpty()) {
                        bookcaseDetails[bookcase] = ArrayList((items[0] as ArrayList<BookcaseEdition>).map{ item -> item.editionId })
                    }
                })
            }
        }
        manageDisposable(
            Observable
                .combineLatest(requests) {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<Any>() {
                    override fun onNext(data: Any) {
                    }
                    override fun onError(e: Throwable) {
                        sendToView { it.showErrorMessage(e.message) }
                    }
                    override fun onComplete() {
                        val selection: ArrayList<BookcaseSelection> = arrayListOf()
                        for (key in bookcaseDetails.keys()) {
                            selection.add(BookcaseSelection(key, bookcaseDetails[key]!!.contains(entityId)))
                        }
                        sendToView { it.onInitViews(selection) }
                    }
                })
        )
    }

    private fun getBookcaseWorksDetails(bookcases: ArrayList<Bookcase>?, entityId: Int, force: Boolean) {
        val bookcaseDetails: ConcurrentHashMap<Bookcase, ArrayList<Int>> = ConcurrentHashMap()
        val requests: ArrayList<Observable<Any>> = arrayListOf()
        bookcases!!.forEach { bookcase ->
            if (bookcase.type == "work") {
                val singleObservable: ArrayList<Observable<ArrayList<BookcaseWork>>> = arrayListOf(getWorksInternal(force, bookcase.id).toObservable())
                requests.add(Observable.combineLatest(singleObservable) { items ->
                    if (!items.isEmpty()) {
                        bookcaseDetails[bookcase] = ArrayList((items[0] as ArrayList<BookcaseWork>).map{ item -> item.itemId })
                    }
                })
            }
        }
        manageDisposable(
            Observable
                .combineLatest(requests) {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<Any>() {
                    override fun onNext(data: Any) {
                    }
                    override fun onError(e: Throwable) {
                        sendToView { it.showErrorMessage(e.message) }
                    }
                    override fun onComplete() {
                        val selection: ArrayList<BookcaseSelection> = arrayListOf()
                        for (key in bookcaseDetails.keys()) {
                            selection.add(BookcaseSelection(key, bookcaseDetails[key]!!.contains(entityId)))
                        }
                        sendToView { it.onInitViews(selection) }
                    }
                })
        )

    }

    private fun getBookcaseFilmsDetails(bookcases: ArrayList<Bookcase>?, entityId: Int, force: Boolean) {
        val bookcaseDetails: ConcurrentHashMap<Bookcase, ArrayList<Int>> = ConcurrentHashMap()
        val requests: ArrayList<Observable<Any>> = arrayListOf()
        bookcases!!.forEach { bookcase ->
            if (bookcase.type == "film") {
                val singleObservable: ArrayList<Observable<ArrayList<BookcaseFilm>>> = arrayListOf(getFilmsInternal(force, bookcase.id).toObservable())
                requests.add(Observable.combineLatest(singleObservable) { items ->
                    if (!items.isEmpty()) {
                        bookcaseDetails[bookcase] = ArrayList((items[0] as ArrayList<BookcaseFilm>).map{ item -> item.filmId })
                    }
                })
            }
        }
        manageDisposable(
            Observable
                .combineLatest(requests) {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<Any>() {
                    override fun onNext(data: Any) {
                    }
                    override fun onError(e: Throwable) {
                        sendToView { it.showErrorMessage(e.message) }
                    }
                    override fun onComplete() {
                        val selection: ArrayList<BookcaseSelection> = arrayListOf()
                        for (key in bookcaseDetails.keys()) {
                            selection.add(BookcaseSelection(key, bookcaseDetails[key]!!.contains(entityId)))
                        }
                        sendToView { it.onInitViews(selection) }
                    }
                })
        )

    }

    private fun getBookcasesInternal(userId: Int, force: Boolean) =
            getBookcasesFromServer(userId)
                    .onErrorResumeNext { throwable ->
                        if (!force) {
                            getBookcasesFromDb(userId)
                        } else {
                            throw throwable
                        }
                    }

    private fun getBookcasesFromServer(userId: Int): Single<Optional<ArrayList<Bookcase>>> =
            DataManager.getUserBookcases(userId)
                    .map { getBookcases(it) }

    private fun getBookcasesFromDb(userId: Int): Single<Optional<ArrayList<Bookcase>>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getUserBookcasesPath(userId))
                    .map { it.response }
                    .map { BookcasesResponse.Deserializer().deserialize(it) }
                    .map { getBookcases(it) }

    private fun getBookcases(response: BookcasesResponse): Optional<ArrayList<Bookcase>> =
            response.items.toOptional()

    override fun onItemClick(position: Int, v: View?, item: BookcaseSelection) {
        sendToView { it.onItemClicked(item, position) }
    }

    override fun onItemLongClick(position: Int, v: View?, item: BookcaseSelection) {
        TODO("not implemented")
    }

    // TODO: The next code is copypasted from Viewer presenter, should be replaced with adequate API call

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