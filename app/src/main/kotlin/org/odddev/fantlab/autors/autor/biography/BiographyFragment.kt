package org.odddev.fantlab.autors.autor.biography

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.google.gson.Gson
import org.odddev.fantlab.R
import org.odddev.fantlab.autors.autor.AutorFull
import org.odddev.fantlab.core.utils.format
import org.odddev.fantlab.core.utils.formatText
import org.odddev.fantlab.databinding.AutorBiographyFragmentBinding


/**
 * @author kenrube
 * *
 * @since 10.12.16
 */

class BiographyFragment : MvpAppCompatFragment {

	private val EXTRA_BIO = "bio"

	private lateinit var binding: AutorBiographyFragmentBinding

	constructor() : super()

	// todo выпилить этот ужас после добавления базы
	constructor(bio: String) : super() {
		val bundle = Bundle()
		bundle.putString(EXTRA_BIO, bio)
		arguments = bundle
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = AutorBiographyFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		initToolbar()
		setHasOptionsMenu(true)

		val bio = Gson().fromJson(arguments.getString(EXTRA_BIO), AutorFull.Biography::class.java)
		binding.birthday = bio.birthday?.format(context)
		binding.deathday = bio.deathday?.format(context)
		binding.sex = if (bio.sex == "m") 0 else 1
		binding.text = bio.text.formatText()
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
		actionBar?.title = getString(R.string.autor_bio_toolbar_title)
		actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
		actionBar?.setDisplayHomeAsUpEnabled(true)
	}
}
