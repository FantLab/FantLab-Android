package org.odddev.fantlab.author

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.AuthorFragmentBinding
import org.odddev.fantlab.home.IActionsHandler

class AuthorFragment : MvpAppCompatFragment, IAuthorView, IAuthorActions {

	companion object {
		val EXTRA_ID = "id"
		val EXTRA_NAME = "name"
	}

	private val binding: AuthorFragmentBinding by lazy {
		AuthorFragmentBinding.inflate(LayoutInflater.from(context))
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

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)
		initRecyclerView()

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
		Glide.with(context)
				.load("https://fantlab.ru/images/autors/${arguments?.getInt(EXTRA_ID)}")
				.placeholder(R.color.blue)
				.error(R.color.blue)
				.into(binding.photo)
	}

	private fun initRecyclerView() {
		binding.content.layoutManager = LinearLayoutManager(context)
	}

	override fun showAuthor(author: AuthorFull) {
		binding.country.text = author.country.name
	}

	override fun showError(message: String) {
		Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
	}

	override fun showBiography() {
		handler.showBiography("")
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
