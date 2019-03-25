package ru.fantlab.android.ui.modules.bookcases.selector

import android.os.Bundle
import android.support.annotation.StringRes
import ru.fantlab.android.R
import ru.fantlab.android.ui.base.BaseFragment
import android.view.View
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.data.dao.model.BookcaseSelection
import ru.fantlab.android.ui.adapter.BookcaseSelectorAdapter

class BookcaseSelectorFragment : BaseFragment<BookcasesSelectorMvp.View, BookcasesSelectorPresenter>(),
        BookcasesSelectorMvp.View {

    @State var userId: Int = -1
    @State var bookcaseType: String = ""
    @State var entityId: Int = -1

    private val adapter: BookcaseSelectorAdapter by lazy { BookcaseSelectorAdapter(arrayListOf()) }

    override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

    override fun providePresenter(): BookcasesSelectorPresenter = BookcasesSelectorPresenter()

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            stateLayout.hideProgress()
        }
        userId = arguments!!.getInt(BundleConstant.EXTRA)
        bookcaseType = arguments!!.getString(BundleConstant.EXTRA_TWO)
        entityId = arguments!!.getInt(BundleConstant.EXTRA_THREE)
        stateLayout.setEmptyText(R.string.no_bookcases)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        adapter.listener = presenter
        adapter.selectionListener = presenter
        recycler.adapter = adapter
        presenter.onFragmentCreated(arguments!!)
    }

    override fun onInitViews(items: ArrayList<BookcaseSelection>?) {
        hideProgress()
        if (items != null) {
            onSetTabCount(items.size)
            initAdapter(items)
        }
    }

    private fun initAdapter(selections: ArrayList<BookcaseSelection>) {
        adapter.addItems(selections)
    }

    override fun onItemClicked(item: BookcaseSelection, position: Int) {
        // TODO: not implemented
    }

    override fun onItemSelected(item: BookcaseSelection, position: Int) {
        presenter.includeItem(item.bookcase.id, entityId, !item.included)
    }

    override fun onItemSelectionUpdated() {
        hideProgress()
    }

    override fun onRefresh() {
        presenter.getBookcases(userId, bookcaseType, entityId, true)
    }

    override fun onClick(v: View?) {
        onRefresh()
    }

    override fun onSetTabCount(allCount: Int) {
        // TODO: not implemented
    }

    override fun hideProgress() {
        refresh.isRefreshing = false
        stateLayout.showReload(recycler.adapter?.itemCount ?: 0)
    }

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        refresh.isRefreshing = true
        stateLayout.showProgress()
    }

    override fun showErrorMessage(msgRes: String?) {
        hideProgress()
        super.showErrorMessage(msgRes)
    }

    override fun showMessage(titleRes: Int, msgRes: Int) {
        hideProgress()
        super.showMessage(titleRes, msgRes)
    }

    companion object {

        fun newInstance(userId: Int, bookcaseType: String, id: Int): BookcaseSelectorFragment {
            val view = BookcaseSelectorFragment()
            view.arguments = Bundler.start()
                    .put(BundleConstant.EXTRA, userId)
                    .put(BundleConstant.EXTRA_TWO, bookcaseType)
                    .put(BundleConstant.EXTRA_THREE, id)
                    .end()
            return view
        }
    }

    override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
        TODO("not implemented")
    }
}