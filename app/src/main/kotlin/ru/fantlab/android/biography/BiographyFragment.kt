package ru.fantlab.android.biography

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.google.gson.Gson
import ru.fantlab.android.R
import ru.fantlab.android.author.AuthorFull
import ru.fantlab.android.core.utils.format
import ru.fantlab.android.core.utils.formatText
import ru.fantlab.android.databinding.BiographyFragmentBinding

class BiographyFragment : MvpAppCompatFragment {

	private val EXTRA_BIO = "bio"

	private lateinit var binding: BiographyFragmentBinding

	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(bio: String) : super() {
		val bundle = Bundle()
		bundle.putString(EXTRA_BIO, bio)
		arguments = bundle
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = BiographyFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)

		val bio = Gson().fromJson(arguments?.getString(EXTRA_BIO), AuthorFull.Biography::class.java)
		binding.birthday = bio.birthday?.format(context)
		binding.deathday = bio.deathday?.format(context)
		binding.sex = if (bio.sex == "m") 0 else 1
		binding.text = bio.text.formatText()
		binding.notes = bio.notes.formatText()
		binding.source = "[link=${bio.sourceLink}]${bio.source}[/link]".formatText()
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
			title = getString(R.string.author_bio_toolbar_title)
			setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
			setDisplayHomeAsUpEnabled(true)
		}
	}
}
