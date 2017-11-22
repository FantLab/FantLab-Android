package org.odddev.fantlab.author

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.gson.Gson
import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.AuthorFragmentBinding
import org.odddev.fantlab.home.IActionsHandler

class AuthorFragment : MvpAppCompatFragment, IAuthorView, IAuthorActions {

	private val EXTRA_ID = "id"
	private val EXTRA_NAME = "name"

	private lateinit var binding: AuthorFragmentBinding
	private lateinit var handler: IActionsHandler

	@InjectPresenter
	lateinit var presenter: AuthorPresenter

	private lateinit var bio: String

	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(id: Int, name: String) : super() {
		val bundle = Bundle()
		bundle.putInt(EXTRA_ID, id)
		bundle.putString(EXTRA_NAME, name)
		arguments = bundle
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = AuthorFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)
		initRecyclerView()

		presenter.getAuthor(arguments?.getInt(EXTRA_ID))
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)

		handler = context as IActionsHandler
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

	private fun initRecyclerView() {
		binding.content.layoutManager = LinearLayoutManager(context)
	}

	override fun showAuthor(author: AuthorFull) {
		binding.author = author
		bio = Gson().toJson(author.biography)
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}

	override fun showBiography() {
		handler.showBiography(bio)
	}

	override fun showAwards() {
		// открыть все награды автора
		Snackbar.make(binding.root, "Awards > Contests > Contest > Nomination", Snackbar.LENGTH_SHORT).show()
	}

	override fun showAward(award: AuthorFull.Award) {
		// открыть конкретное награждение
		Snackbar.make(binding.root, "Awards > Nomination ${award.id}", Snackbar.LENGTH_SHORT).show()
	}

	override fun showWorks() {
		// открыть все произведения автора
		Snackbar.make(binding.root, "Works", Snackbar.LENGTH_SHORT).show()
	}

	override fun showWork(work: AuthorFull.Work) {
		// открыть конкретное произведение автора
		Snackbar.make(binding.root, "Works > Work ${work.id}", Snackbar.LENGTH_SHORT).show()
	}
}
