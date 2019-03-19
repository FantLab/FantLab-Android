package ru.fantlab.android.ui.modules.bookcases.editions

import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import io.reactivex.functions.Consumer
import ru.fantlab.android.provider.rest.DataManager
import android.view.View
import io.reactivex.Single
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.data.dao.response.BookcaseEditionsResponse
import ru.fantlab.android.data.dao.response.DeleteBookcaseResponse
import ru.fantlab.android.provider.rest.getBookcaseEditionsPath
import ru.fantlab.android.provider.storage.DbProvider

class BookcaseEditionsPresenter : BasePresenter<BookcaseEditionsMvp.View>(), BookcaseEditionsMvp.Presenter {
    override fun getEditions(force: Boolean, bookcaseId: Int) {
        makeRestCall(
                getEditionsInternal(force, bookcaseId).toObservable(),
                Consumer { editions -> sendToView { it.onNotifyAdapter(editions) } }
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
                    .get(getBookcaseEditionsPath(bookcaseId))
                    .map { it.response }
                    .map { BookcaseEditionsResponse.Deserializer(perPage = 50).deserialize(it) }
                    .map { getEditions(it) }

    private fun getEditions(response: BookcaseEditionsResponse): ArrayList<BookcaseEdition> = response.editions

    override fun onItemClick(position: Int, v: View?, item: BookcaseEdition) {
        sendToView { it.onItemClicked(item) }
    }

    override fun onItemLongClick(position: Int, v: View?, item: BookcaseEdition) {
        TODO("not implemented")
    }

}