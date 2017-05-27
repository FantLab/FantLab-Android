package org.odddev.fantlab.autors.autor

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.AutorFragmentBinding


/**
 * @author kenrube
 * *
 * @since 10.12.16
 */

class AutorFragment : MvpAppCompatFragment, IAutorView {

	private lateinit var binding: AutorFragmentBinding

	@InjectPresenter
	lateinit var presenter: AutorPresenter

	constructor(): super()

	constructor(id: Int): super() {
		val bundle = Bundle()
		bundle.putInt("xxx", id)
		arguments = bundle
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = AutorFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)

		presenter.getAutor(arguments.getInt("xxx"))
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		actionBar?.setTitle(R.string.autors_toolbar_title)
		actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> (activity.findViewById(R.id.drawer_layout) as DrawerLayout)
					.openDrawer(GravityCompat.START)
		}
		return super.onOptionsItemSelected(item)
	}

	override fun showAutor(autor: AutorFull) {
		Snackbar.make(binding.root, "success!", Snackbar.LENGTH_LONG).show()
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}
}
