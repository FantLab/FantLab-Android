package ru.fantlab.android.search

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.fantlab.android.R
import ru.fantlab.android.databinding.SearchFragmentBinding
import ru.fantlab.android.home.IActionsHandler

class SearchFragment : MvpAppCompatFragment(), ISearchView, ISearchActions {

	private val binding: SearchFragmentBinding by lazy {
		SearchFragmentBinding.inflate(LayoutInflater.from(context))
	}

	private val handler: IActionsHandler by lazy {
		context as IActionsHandler
	}

	private val authorResultsAdapter: AuthorResultsAdapter by lazy {
		AuthorResultsAdapter(this)
	}

	private val workResultsAdapter: WorkResultsAdapter by lazy {
		WorkResultsAdapter(this)
	}

	private val editionResultsAdapter: EditionResultsAdapter by lazy {
		EditionResultsAdapter(this)
	}

	@InjectPresenter
	lateinit var presenter: SearchPresenter

	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? = binding.root

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)
		initRecyclerViews()
		binding.authorResults.visibility = View.GONE
		binding.allAuthorResults.visibility = View.GONE
		binding.workResults.visibility = View.GONE
		binding.allWorkResults.visibility = View.GONE
		binding.editionResults.visibility = View.GONE
		binding.allEditionResults.visibility = View.GONE
		binding.progress.visibility = View.GONE
		binding.search.setOnClickListener {
			binding.authorResults.visibility = View.GONE
			binding.allAuthorResults.visibility = View.GONE
			binding.workResults.visibility = View.GONE
			binding.allWorkResults.visibility = View.GONE
			binding.editionResults.visibility = View.GONE
			binding.allEditionResults.visibility = View.GONE
			binding.progress.visibility = View.VISIBLE
			presenter.search(binding.query.text.toString())
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home ->
				(activity?.findViewById(R.id.drawer_layout) as DrawerLayout)
						.openDrawer(GravityCompat.START)
		}
		return super.onOptionsItemSelected(item)
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		actionBar?.apply {
			title = getString(R.string.search_toolbar_title)
			setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
			setDisplayHomeAsUpEnabled(true)
		}
	}

	private fun initRecyclerViews() {
		binding.authorResults.adapter = authorResultsAdapter
		ViewCompat.setNestedScrollingEnabled(binding.authorResults, false)
		binding.workResults.adapter = workResultsAdapter
		ViewCompat.setNestedScrollingEnabled(binding.workResults, false)
		binding.editionResults.adapter = editionResultsAdapter
		ViewCompat.setNestedScrollingEnabled(binding.editionResults, false)
	}

	override fun showResults(results: SearchResult) {
		binding.progress.visibility = View.GONE
		binding.authorResults.visibility =
				if (results.authorsSearchResult.authorsSearchResult.isNotEmpty()) View.VISIBLE
				else View.GONE
		authorResultsAdapter.setAuthors(results.authorsSearchResult.authorsSearchResult)
		binding.allAuthorResults.visibility =
				if (results.authorsSearchResult.total > 10) View.VISIBLE
				else View.GONE

		binding.workResults.visibility =
				if (results.worksSearchResult.worksSearchResult.isNotEmpty()) View.VISIBLE
				else View.GONE
		workResultsAdapter.setWorks(results.worksSearchResult.worksSearchResult)
		binding.allWorkResults.visibility =
				if (results.worksSearchResult.total > 10) View.VISIBLE
				else View.GONE

		binding.editionResults.visibility =
				if (results.editionsSearchResult.editionsSearchResult.isNotEmpty()) View.VISIBLE
				else View.GONE
		editionResultsAdapter.setEditions(results.editionsSearchResult.editionsSearchResult)
		binding.allEditionResults.visibility =
				if (results.editionsSearchResult.total > 10) View.VISIBLE
				else View.GONE
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}

	override fun onAuthorClicked(authorId: Int, name: String) {
		handler.openAuthor(authorId, name)
	}

	override fun onWorkClicked(workId: Int, name: String) {
		handler.openWork(workId, name)
	}

	override fun onEditionClicked(editionId: Int, name: String) {
		handler.openEdition(editionId, name)
	}
}
