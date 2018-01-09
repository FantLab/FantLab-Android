package ru.fantlab.android.award

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.fantlab.android.R
import ru.fantlab.android.databinding.AwardsFragmentBinding

class AwardsFragment : MvpAppCompatFragment(), IAwardsView {

	private lateinit var binding: AwardsFragmentBinding
	private lateinit var adapter: AwardsAdapter

	@InjectPresenter
	lateinit var presenter: AwardsPresenter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = AwardsFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		presenter.getAwards()

		initToolbar()
		setHasOptionsMenu(true)
		initRecyclerView()
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		actionBar?.apply {
			title = getString(R.string.awards_toolbar_title)
			setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
			setDisplayHomeAsUpEnabled(true)
		}
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context)
		binding.awards.layoutManager = layoutManager
		adapter = AwardsAdapter()
		binding.awards.adapter = adapter
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home ->
				(activity?.findViewById<DrawerLayout>(R.id.drawer_layout) as DrawerLayout)
					.openDrawer(GravityCompat.START)
		}
		return super.onOptionsItemSelected(item)
	}

	override fun showAwards(awards: List<Award>) {
		adapter.setAwards(awards)
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}
}
