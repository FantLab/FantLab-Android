package ru.fantlab.android.provider.scheme

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import es.dmoral.toasty.Toasty
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity

object SchemeParser {
	fun launchUri(context: Context, url: String, label: String) {
		val pattern = Regex("/([a-z]+).*?(\\d+)")
		val results = pattern.matchEntire(url)?.groupValues
		if (results != null) {
			val type = results[1]
			val id = results[2]
			when (type) {
				"work" -> {
					WorkPagerActivity.startActivity(App.instance.applicationContext, id.toInt(), label, 0)
				}
				"edition" -> {
					EditionPagerActivity.startActivity(App.instance.applicationContext, id.toInt(), label, 0)
				}
				else -> {
					Toasty.error(App.instance, "${context.getString(R.string.not_recognized)} ($type:$id)", Toast.LENGTH_LONG).show()
				}
			}
		} else openUrl(context, url)
	}

	private fun openUrl(context: Context, url: String) {
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(browserIntent);
	}

}
