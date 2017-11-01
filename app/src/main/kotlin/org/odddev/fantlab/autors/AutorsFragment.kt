package org.odddev.fantlab.autors

import android.content.Context
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
import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.AutorsFragmentBinding
import org.odddev.fantlab.home.IActionsHandler

class AutorsFragment : MvpAppCompatFragment(), IAutorsView, IAuthorsActions {

	private lateinit var binding: AutorsFragmentBinding
	private lateinit var handler: IActionsHandler

	@InjectPresenter
	lateinit var presenter: AutorsPresenter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = AutorsFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)
		initRecyclerView()

		presenter.getAutors()
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

	override fun onAttach(context: Context?) {
		super.onAttach(context)

		handler = context as IActionsHandler
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		actionBar?.apply {
			title = getString(R.string.autors_toolbar_title)
			setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
			setDisplayHomeAsUpEnabled(true)
		}
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context)
		binding.autors.layoutManager = layoutManager
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home ->
				(activity?.findViewById<DrawerLayout>(R.id.drawer_layout) as DrawerLayout)
					.openDrawer(GravityCompat.START)
		}
		return super.onOptionsItemSelected(item)
	}

	override fun showAutors(autors: List<Autor>, scrollToTop: Boolean) {
		if (scrollToTop) binding.autors.scrollToPosition(0)
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}

	override fun onAuthorClicked(author: Autor) {
		handler.openAutor(author.id, author.name)
	}
}
