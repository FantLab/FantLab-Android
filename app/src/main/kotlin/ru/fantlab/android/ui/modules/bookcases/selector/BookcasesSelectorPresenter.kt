package ru.fantlab.android.ui.modules.bookcases.selector

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.data.dao.response.*
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getBookcaseInclusionsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class BookcasesSelectorPresenter : BasePresenter<BookcasesSelectorMvp.View>(),
        BookcasesSelectorMvp.Presenter {

    private var bookcaseType: String = ""
    private var entityId: Int = 0

    override fun onFragmentCreated(bundle: Bundle) {
        bookcaseType = bundle.getString(BundleConstant.EXTRA_TWO)
        entityId = bundle.getInt(BundleConstant.EXTRA_THREE)
        getBookcases(bookcaseType, entityId, false)
    }

    override fun getBookcases(bookcaseType: String, entityId: Int, force: Boolean) {
        makeRestCall(
                getBookcasesInternal(bookcaseType, entityId, force).toObservable(),
                Consumer { bookcasesInclusions ->
                    sendToView {
                        val inclusions: ArrayList<BookcaseSelection> = ArrayList()
                        bookcasesInclusions!!.map { inclusions.add(BookcaseSelection(it, it.itemAdded == 1)) }
                        it.onInitViews(inclusions)
                    }
                }
        )
    }

    private fun getBookcasesInternal(bookcaseType: String, entityId: Int, force: Boolean) =
            getBookcasesFromServer(bookcaseType, entityId)
                    .onErrorResumeNext { throwable ->
                        if (!force) {
                            getBookcasesFromDb(bookcaseType, entityId)
                        } else {
                            throw throwable
                        }
                    }

    private fun getBookcasesFromServer(bookcaseType: String, entityId: Int): Single<ArrayList<BookcaseInclusion>> =
            DataManager.getBookcaseInclusions(bookcaseType, entityId)
                    .map { getBookcases(it) }

    private fun getBookcasesFromDb(bookcaseType: String, entityId: Int): Single<ArrayList<BookcaseInclusion>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getBookcaseInclusionsPath(bookcaseType, entityId))
                    .map { it.response }
                    .map { BookcaseInclusionResponse.Deserializer().deserialize(it) }
                    .map { getBookcases(it) }

    private fun getBookcases(response: BookcaseInclusionResponse): ArrayList<BookcaseInclusion> {
        val inclusions = ArrayList<BookcaseInclusion>()
        response.items.map { inclusions.add(BookcaseInclusion(it.bookcaseId, it.bookcaseName, it.itemAdded, bookcaseType)) }
        return inclusions
    }

    override fun onItemClick(position: Int, v: View?, item: BookcaseSelection) {
        sendToView { it.onItemClicked(item, position) }
    }

    override fun onItemLongClick(position: Int, v: View?, item: BookcaseSelection) {
        // TODO("not implemented")
    }

    override fun onItemSelected(position: Int, v: View?, item: BookcaseSelection) {
        sendToView { it.onItemSelected(item, position) }
    }

    override fun includeItem(bookcaseId: Int, entityId: Int, include: Boolean) {
        makeRestCall(
                DataManager.includeItemToBookcase(bookcaseId, entityId, if (include) "add" else "delete").toObservable(),
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