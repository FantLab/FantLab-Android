package ru.fantlab.android.provider.scheme

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DEFAULT
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.award.AwardPagerActivity
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import timber.log.Timber

object SchemeParser {
	fun launchUri(context: Context, url: String, label: String) {
		val pattern = Regex("([a-z]+).*?(\\d+)(\\D|\\Z)")
		val results = pattern.matchEntire(url.substringAfterLast("/"))?.groupValues
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
				"author", "autor" -> {
					AuthorPagerActivity.startActivity(App.instance.applicationContext, id.toInt(), label, 0)
				}
				"award" -> {
					AwardPagerActivity.startActivity(App.instance.applicationContext, id.toInt(), label, 0)
				}
				"cycle" -> {
					CyclePagerActivity.startActivity(App.instance.applicationContext, id.toInt(), label, 0)
				}
				else -> {
					Timber.d("${context.getString(R.string.not_recognized)} ($type:$id)")
					openUrl(context, "$PROTOCOL_HTTPS://$HOST_DEFAULT$url")
				}
			}
		} else {
			if (url.contains(HOST_DEFAULT)) {
				launchUri(context, url.substringAfterLast("/"), label)
			} else openUrl(context, url)
		}
	}

	private fun openUrl(context: Context, url: String) {
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
		context.startActivity(browserIntent)
	}

}
