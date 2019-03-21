package ru.fantlab.android.ui.modules.author.editions

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.EditionsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerMvp
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity

class AuthorEditionsFragment : BaseFragment<AuthorEditionsMvp.View, AuthorEditionsPresenter>(),
		AuthorEditionsMvp.View {

	private val adapter: EditionsAdapter by lazy { EditionsAdapter(arrayListOf()) }
	private var countCallback: AuthorPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = AuthorEditionsPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.setEmptyText(R.string.no_editions)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		presenter.onFragmentCreated(arguments!!)
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onInitViews(editionsBlocks: ArrayList<EditionsBlocks.EditionsBlock>?, count: Int) {
		hideProgress()
		onSetTabCount(count)
		adapter.clear()
		editionsBlocks?.forEach { adapter.addItems(it.list) }
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.showReload(recycler.adapter?.itemCount ?: 0)
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

		fun newInstance(authorId: Int): AuthorEditionsFragment {
			val view = AuthorEditionsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}

	override fun onRefresh() {
		presenter.getEditions(true)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onSetTabCount(allCount: Int) {
		countCallback?.onSetBadge(2, allCount)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is AuthorPagerMvp.View) {
			countCallback = context
		}
	}

	override fun onDetach() {
		countCallback = null
		super.onDetach()
	}

	override fun onItemClicked(item: EditionsBlocks.Edition) {
		EditionPagerActivity.startActivity(context!!, item.editionId, item.name, 0)
	}
}