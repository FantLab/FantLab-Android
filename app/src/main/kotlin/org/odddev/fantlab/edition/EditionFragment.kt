package org.odddev.fantlab.edition

import android.annotation.SuppressLint
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
import org.odddev.fantlab.author.AuthorPresenter
import org.odddev.fantlab.databinding.EditionFragmentBinding
import org.odddev.fantlab.home.IActionsHandler

class EditionFragment : MvpAppCompatFragment, IEditionView {

	companion object {
		val EXTRA_ID = "id"
		val EXTRA_NAME = "name"
	}

	private val binding: EditionFragmentBinding by lazy {
		EditionFragmentBinding.inflate(LayoutInflater.from(context))
	}

	private val handler: IActionsHandler by lazy {
		context as IActionsHandler
	}

	@InjectPresenter
	lateinit var presenter: AuthorPresenter

	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(id: Int, name: String) : super() {
		val bundle = Bundle()
		bundle.putInt(EXTRA_ID, id)
		bundle.putString(EXTRA_NAME, name)
		arguments = bundle
	}

	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? = binding.root

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)

		presenter.getAuthor(arguments?.getInt(EXTRA_ID))
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> activity?.onBackPressed()
		}
		return super.onOptionsItemSelected(item)
	}

	private fun initToolbar() {
		val activity = activity as AppCompatActivity
		activity.setSupportActionBar(binding.toolbar)
		val actionBar = activity.supportActionBar
		actionBar?.apply {
			title = arguments?.getString(EXTRA_NAME)
			setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
			setDisplayHomeAsUpEnabled(true)
		}
	}

	override fun showEdition(edition: Edition) {
		//binding.country.text = author.country.name
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}
}
