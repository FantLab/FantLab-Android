package ru.fantlab.android.authors

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.fantlab.android.R
import ru.fantlab.android.databinding.AuthorsFragmentBinding
import ru.fantlab.android.home.IActionsHandler

class AuthorsFragment : MvpAppCompatFragment(), IAuthorsView, IAuthorsActions {

	private val binding: AuthorsFragmentBinding by lazy {
		AuthorsFragmentBinding.inflate(LayoutInflater.from(context))
	}

	private val handler: IActionsHandler by lazy {
		context as IActionsHandler
	}

	private val adapter: AuthorsAdapter by lazy {
		AuthorsAdapter(this)
	}

	@InjectPresenter
	lateinit var presenter: AuthorsPresenter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)
		initRecyclerView()

		binding.progress.visibility = View.VISIBLE
		presenter.getAuthors()
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.search_menu, menu)

		val searchItem = menu?.findItem(R.id.action_search)
		val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String): Boolean {
				presenter.filter(query)
				return true
			}

			override fun onQueryTextChange(newText: String): Boolean {
				presenter.filter(newText)
				return true
			}
		})
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		actionBar?.apply {
			title = getString(R.string.authors_toolbar_title)
			setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
			setDisplayHomeAsUpEnabled(true)
		}
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context)
		binding.authors.layoutManager = layoutManager
		binding.authors.adapter = adapter
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home ->
				(activity?.findViewById<DrawerLayout>(R.id.drawer_layout) as DrawerLayout)
					.openDrawer(GravityCompat.START)
		}
		return super.onOptionsItemSelected(item)
	}

	override fun showAuthors(authors: List<AuthorInList>, scrollToTop: Boolean) {
		binding.progress.visibility = View.GONE
		adapter.setAuthors(authors)
		if (scrollToTop) {
			binding.authors.scrollToPosition(0)
		}
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}

	override fun onAuthorClicked(author: AuthorInList) {
		handler.openAuthor(author.authorId, author.rusName!!)
	}
}
