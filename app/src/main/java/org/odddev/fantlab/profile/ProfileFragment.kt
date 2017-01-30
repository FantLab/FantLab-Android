package org.odddev.fantlab.profile

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.arellomobile.mvp.MvpAppCompatFragment

import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.ProfileFragmentBinding

/**
 * @author kenrube
 * *
 * @since 10.12.16
 */

class ProfileFragment : MvpAppCompatFragment() {

	private lateinit var binding: ProfileFragmentBinding

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = ProfileFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		if (actionBar != null) {
			actionBar.setTitle(R.string.nav_profile)
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
			actionBar.setDisplayHomeAsUpEnabled(true)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item!!.itemId) {
			android.R.id.home -> {
				(activity.findViewById(R.id.drawer_layout) as DrawerLayout)
						.openDrawer(GravityCompat.START)
			}
		}
		return super.onOptionsItemSelected(item)
	}
}
