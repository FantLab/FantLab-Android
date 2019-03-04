package ru.fantlab.android.ui.modules.profile.bookcases

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import com.evernote.android.state.State
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.BookcasesAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.bookcases.editions.BookcaseEditionsActivity
import ru.fantlab.android.ui.modules.user.UserPagerMvp

class ProfileBookcasesFragment : BaseFragment<ProfileBookcasesMvp.View, ProfileBookcasesPresenter>(),
        ProfileBookcasesMvp.View {

    @State var userId: Int = -1
    private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, userId) }
    private val adapter: BookcasesAdapter by lazy { BookcasesAdapter(arrayListOf()) }
    private var countCallback: UserPagerMvp.View? = null

    override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

    override fun providePresenter(): ProfileBookcasesPresenter = ProfileBookcasesPresenter()

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            stateLayout.hideProgress()
        }
        stateLayout.setEmptyText(R.string.no_responses)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        adapter.listener = presenter
        recycler.adapter = adapter
        recycler.addKeyLineDivider()
        userId = arguments!!.getInt(BundleConstant.EXTRA)
        getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
        recycler.addOnScrollListener(getLoadMore())
        presenter.getBookcases(userId, false)
        fastScroller.attachRecyclerView(recycler)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is UserPagerMvp.View) {
            countCallback = context
        }
    }

    override fun onDetach() {
        countCallback = null
        super.onDetach()
    }

    override fun onDestroyView() {
        recycler.removeOnScrollListener(getLoadMore())
        super.onDestroyView()
    }

    override fun onNotifyAdapter(items: ArrayList<Bookcase>, page: Int) {
        hideProgress()
        if (items.isEmpty()) {
            adapter.clear()
            return
        }
        if (page <= 1) {
            adapter.insertItems(items)
        } else {
            adapter.addItems(items)
        }
    }

    override fun getLoadMore() = onLoadMore

    override fun onSetTabCount(count: Int) {
        countCallback?.onSetBadge(2, count)
    }

    override fun onItemClicked(item: Bookcase) {
        /// TODO: replace with the real call
        BookcaseEditionsActivity.startActivity(context!!, 3056)
        //BookcaseEditionsActivity.startActivity(context!!, item.id)
    }

    override fun onRefresh() {
        presenter.getBookcases(userId, true)
    }

    override fun onClick(v: View?) {
        onRefresh()
    }

    override fun hideProgress() {
        refresh.isRefreshing = false
        stateLayout.hideProgress()
    }

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        refresh.isRefreshing = true
        stateLayout.showProgress()
    }

    override fun showErrorMessage(msgRes: String?) {
        callback?.showErrorMessage(msgRes)
    }

    override fun showMessage(titleRes: Int, msgRes: Int) {
        showReload()
        super.showMessage(titleRes, msgRes)
    }

    private fun showReload() {
        hideProgress()
        stateLayout.showReload(adapter.itemCount)
    }

    companion object {

        fun newInstance(userId: Int): ProfileBookcasesFragment {
            val view = ProfileBookcasesFragment()
            view.arguments = Bundler.start().put(BundleConstant.EXTRA, userId).end()
            return view
        }
    }

    override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
        //if (presenter != null) adapter.setOnContextMenuListener(this)
        super.onStart()
    }

}