package org.odddev.fantlab.autors.autor

import android.os.Bundle
import android.support.design.widget.Snackbar
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

	private val EXTRA_ID = "id"
	private val EXTRA_NAME = "name"

	private lateinit var binding: AutorFragmentBinding

	@InjectPresenter
	lateinit var presenter: AutorPresenter

	constructor(): super()

	constructor(id: Int, name: String): super() {
		val bundle = Bundle()
		bundle.putInt(EXTRA_ID, id)
		bundle.putString(EXTRA_NAME, name)
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

		presenter.getAutor(arguments.getInt(EXTRA_ID))
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> activity.onBackPressed()
		}
		return super.onOptionsItemSelected(item)
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		actionBar?.title = arguments.getString(EXTRA_NAME)
		actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun showAutor(autor: AutorFull) {
		binding.autor = autor
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}
}
