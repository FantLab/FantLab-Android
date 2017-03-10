package org.odddev.fantlab.award

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.AwardsFragmentBinding

/**
 * @author kenrube
 * *
 * @since 10.12.16
 */

class AwardsFragment : MvpAppCompatFragment(), IAwardsView {

	private lateinit var binding: AwardsFragmentBinding
	private lateinit var adapter: AwardsAdapter

	@InjectPresenter
	lateinit var presenter: AwardsPresenter

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = AwardsFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		presenter.getAwards()

		initToolbar()
		setHasOptionsMenu(true)

		initRecyclerView()
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		if (actionBar != null) {
			actionBar.setTitle(R.string.nav_awards)
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
			actionBar.setDisplayHomeAsUpEnabled(true)
		}
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context)
		binding.awards.layoutManager = layoutManager
		adapter = AwardsAdapter()
		binding.awards.adapter = adapter
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.action_bar_awards, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> (activity.findViewById(R.id.drawer_layout) as DrawerLayout)
					.openDrawer(GravityCompat.START)
			R.id.filter_by -> presenter.onFilterButtonClick()
		}
		return super.onOptionsItemSelected(item)
	}

	override fun showFilterDialog() {
		AlertDialog.Builder(context)
				.setTitle(getString(R.string.award_filter_dialog_title))
				.setView(R.layout.award_filter_dialog)
				.setPositiveButton(android.R.string.ok, null)
				.setNegativeButton(getString(R.string.award_filter_cancel), null)
				.show()
	}

	override fun showAwards(awards: List<Award>) {
		adapter.setAwards(awards)
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}
}
