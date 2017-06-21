package org.odddev.fantlab.autors.autor

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.gson.Gson
import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.AutorFragmentBinding
import org.odddev.fantlab.home.IActionsHandler

class AutorFragment : MvpAppCompatFragment, IAutorView, IAutorActions {

	private val EXTRA_ID = "id"
	private val EXTRA_NAME = "name"

	private lateinit var binding: AutorFragmentBinding
	private lateinit var handler: IActionsHandler

	@InjectPresenter
	lateinit var presenter: AutorPresenter

	private lateinit var bio: String

	constructor() : super()

	constructor(id: Int, name: String) : super() {
		val bundle = Bundle()
		bundle.putInt(EXTRA_ID, id)
		bundle.putString(EXTRA_NAME, name)
		arguments = bundle
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = AutorFragmentBinding.inflate(inflater, container, false)
		binding.handler = this
		binding.autorBio.visibility = View.GONE
		return binding.root
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)

		presenter.getAutor(arguments.getInt(EXTRA_ID))
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)

		handler = context as IActionsHandler
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
		actionBar?.apply {
			title = arguments.getString(EXTRA_NAME)
			setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
			setDisplayHomeAsUpEnabled(true)
		}
	}

	override fun showAutor(autor: AutorFull) {
		binding.autor = autor
		binding.autorBio.visibility = View.VISIBLE
		bio = Gson().toJson(autor.biography)
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}

	override fun showBiography() {
		handler.showBiography(bio)
	}
}
