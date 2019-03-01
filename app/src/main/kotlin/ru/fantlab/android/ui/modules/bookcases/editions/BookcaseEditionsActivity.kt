package ru.fantlab.android.ui.modules.bookcases.editions

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.View
import android.view.MenuItem
import ru.fantlab.android.R
import butterknife.BindView
import com.evernote.android.state.State
import ru.fantlab.android.helper.*
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.ui.adapter.BookcaseEditionsAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class BookcaseEditionsActivity : BaseActivity<BookcaseEditionsMvp.View, BookcaseEditionsPresenter>(), BookcaseEditionsMvp.View {

    @BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
    @BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
    @BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
    @BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

    @State var bookcaseId: Int = 0

    private val adapter: BookcaseEditionsAdapter by lazy { BookcaseEditionsAdapter(arrayListOf()) }

    private lateinit var toolbarMenu: Menu

    override fun layout(): Int = R.layout.bookcase_editions_layout

    override fun isTransparent(): Boolean = false

    override fun canBack(): Boolean = true

    override fun providePresenter(): BookcaseEditionsPresenter = BookcaseEditionsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            bookcaseId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
        }
        if (bookcaseId == -1) {
            finish()
            return
        }

        //title = getString(R.string.awards_list)
        hideShowShadow(true)
        //selectMenuItem(R.id.awards, true)
        if (savedInstanceState == null) {
            stateLayout.hideProgress()
        }
        stateLayout.setEmptyText(R.string.no_results)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        adapter.listener = presenter
        recycler.adapter = adapter
        recycler.addDivider()
        presenter.getEditions(false, bookcaseId)
        fastScroller.attachRecyclerView(recycler)
    }

    fun showSortDialog() {
        /*val dialogView = ContextMenuDialogView()
        dialogView.initArguments("main", ContextMenuBuilder.buildForAwardsSorting(recycler.context))
        dialogView.show(supportFragmentManager, "ContextMenuDialogView")*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.awards_menu, menu)
        toolbarMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> {
                showSortDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(item: ContextMenus.MenuItem, listItem: Any, position: Int) {
        //presenter.sort = AwardsSortOption.valueOf(item.id)
    }

    override fun onNotifyAdapter(items: ArrayList<BookcaseEdition>) {
        hideProgress()
        if (items.isEmpty()) {
            adapter.clear()
            return
        }
        adapter.insertItems(items)
    }

    override fun onItemClicked(item: BookcaseEdition) {
        /*val title = if (!item.nameRus.isEmpty()) {
            if (!item.nameOrig.isEmpty()) {
                String.format("%s / %s", item.nameRus, item.nameOrig)
            } else {
                item.nameRus
            }
        } else {
            item.nameOrig
        }
        AwardPagerActivity.startActivity(this, item.id, title, 0)*/
    }

    override fun onRefresh() {
        presenter.getEditions(true, bookcaseId)
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

    override fun showMessage(titleRes: Int, msgRes: Int) {
        showReload()
        super.showMessage(titleRes, msgRes)
    }

    private fun showReload() {
        hideProgress()
        stateLayout.showReload(adapter.itemCount)
    }

    companion object {

        fun startActivity(context: Context, bookcaseId: Int) {
            val intent = Intent(context, BookcaseEditionsActivity::class.java)
            intent.putExtras(Bundler.start()
                    .put(BundleConstant.EXTRA, bookcaseId)
                    .end())
            if (context is Service || context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

}