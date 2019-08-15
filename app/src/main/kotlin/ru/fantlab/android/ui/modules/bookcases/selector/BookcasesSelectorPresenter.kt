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
import ru.fantlab.android.data.dao.response.*
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getPersonalBookcasesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import java.util.concurrent.ConcurrentHashMap

class BookcasesSelectorPresenter : BasePresenter<BookcasesSelectorMvp.View>(),
        BookcasesSelectorMvp.Presenter {

    override fun onFragmentCreated(bundle: Bundle) {
        val bookcaseType = bundle.getString(BundleConstant.EXTRA_TWO)
        val entityId = bundle.getInt(BundleConstant.EXTRA_THREE)
        getBookcases(bookcaseType, entityId, false)
    }

    override fun getBookcases(bookcaseType: String, entityId: Int, force: Boolean) {
        makeRestCall(
                getBookcasesInternal(force).toObservable(),
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
            if (bookcase.bookcaseType == "edition") {
                /*val singleObservable: ArrayList<Observable<ArrayList<BookcaseEdition>>> = arrayListOf(getEditionsInternal(force, bookcase.bookcaseId).toObservable())
                requests.add(Observable.combineLatest(singleObservable) { items ->
                    if (!items.isEmpty()) {
                        bookcaseDetails[bookcase] = ArrayList((items[0] as ArrayList<BookcaseEdition>).map{ item -> item.editionId })
                    }
                })*/
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
            if (bookcase.bookcaseType == "work") {
                /*val singleObservable: ArrayList<Observable<ArrayList<BookcaseWork>>> = arrayListOf(getWorksInternal(force, bookcase.bookcaseId).toObservable())
                requests.add(Observable.combineLatest(singleObservable) { items ->
                    if (!items.isEmpty()) {
                        bookcaseDetails[bookcase] = ArrayList((items[0] as ArrayList<BookcaseWork>).map{ item -> item.itemId })
                    }
                })*/
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
            if (bookcase.bookcaseType == "film") {
                /*val singleObservable: ArrayList<Observable<ArrayList<BookcaseFilm>>> = arrayListOf(getFilmsInternal(force, bookcase.bookcaseId).toObservable())
                requests.add(Observable.combineLatest(singleObservable) { items ->
                    if (!items.isEmpty()) {
                        bookcaseDetails[bookcase] = ArrayList((items[0] as ArrayList<BookcaseFilm>).map{ item -> item.filmId })
                    }
                })*/
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

    private fun getBookcasesInternal(force: Boolean) =
            getBookcasesFromServer()
                    .onErrorResumeNext { throwable ->
                        if (!force) {
                            getBookcasesFromDb()
                        } else {
                            throw throwable
                        }
                    }

    private fun getBookcasesFromServer(): Single<Optional<ArrayList<Bookcase>>> =
            DataManager.getPersonalBookcases()
                    .map { getBookcases(it) }

    private fun getBookcasesFromDb(): Single<Optional<ArrayList<Bookcase>>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getPersonalBookcasesPath())
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

    override fun onItemSelected(position: Int, v: View?, item: BookcaseSelection) {
        sendToView { it.onItemSelected(item, position) }
    }

    override fun includeItem(bookcaseId: Int, entityId: Int, include: Boolean) {
        makeRestCall(
                DataManager.includeItemToBookcase(bookcaseId, entityId, if (include) "true" else "false").toObservable(),
                Consumer { response ->
                    val result = BookcaseItemIncludedResponse.Parser().parse(response)
                    if (result == null) {
                        sendToView { it.showErrorMessage(response) }
                    } else {
                        sendToView { it.onItemSelectionUpdated() }
                    }
                }
        )
    }

}