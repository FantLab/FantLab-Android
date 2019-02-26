package ru.fantlab.android.provider.scheme

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant.EXTRA
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DEFAULT
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.authors.AuthorsActivity
import ru.fantlab.android.ui.modules.award.AwardPagerActivity
import ru.fantlab.android.ui.modules.awards.AwardsActivity
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity
import ru.fantlab.android.ui.modules.plans.PlansPagerActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import timber.log.Timber

object SchemeParser {
	fun launchUri(context: Context, url: String, label: String) {
		val pattern = Regex("([a-z]+).*?(\\d+)(\\D|\\Z)")
		val results = pattern.matchEntire(url.substringAfterLast("/").substringBefore("?").substringBefore(":"))?.groupValues
		if (results != null) {
			val type = results[1]
			val id = results[2]
			when (type) {
				"work" -> {
					WorkPagerActivity.startActivity(context, id.toInt(), label, 0)
				}
				"edition" -> {
					EditionPagerActivity.startActivity(context, id.toInt(), label, 0)
				}
				"author", "autor" -> {
					AuthorPagerActivity.startActivity(context, id.toInt(), label, 0)
				}
				"autors" -> {
					context.startActivity(Intent(context, AuthorsActivity::class.java).putExtra(EXTRA, id))
				}
				"award" -> {
					AwardPagerActivity.startActivity(context, id.toInt(), label, 0)
				}
				"cycle" -> {
					CyclePagerActivity.startActivity(context, id.toInt(), label, 0)
				}
				"user" -> {
					UserPagerActivity.startActivity(context, label, id.toInt(), 0)
				}
				"pub" -> {
					// TODO изменить после появления возможности открыть издательство
					val link = "${LinkParserHelper.PROTOCOL_HTTPS}://${LinkParserHelper.HOST_DEFAULT}/${url.replace("pub", "publisher").substringBefore(":")}"
					openUrl(context, link)
				}
				else -> notRecognizedUrl(context, "$PROTOCOL_HTTPS://$HOST_DEFAULT/$type$id", type, id)
			}
		} else {
			if (url.contains(HOST_DEFAULT)) {
				val type = url.substringAfterLast("/").substringBefore("?").substringBefore(":")
				when (type) {
					"autors" -> {
						context.startActivity(Intent(context, AuthorsActivity::class.java).putExtra(EXTRA, "all"))
					}
					"awards" -> {
						context.startActivity(Intent(context, AwardsActivity::class.java))
					}
					"pubnews" -> {
						PlansPagerActivity.startActivity(context, 0)
					}
					"pubplans" -> {
						PlansPagerActivity.startActivity(context, 1)
					}
					"autplans" -> {
						PlansPagerActivity.startActivity(context, 2)
					}
					else -> openUrl(context, url)
				}

			} else openUrl(context, url)
		}
	}

	fun openUrl(context: Context, url: String) {
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
		context.startActivity(Intent.createChooser(browserIntent, context.getString(R.string.open)))
	}

	fun notRecognizedUrl(context: Context, url: String, type: String, id: String) {
		Timber.d("${context.getString(R.string.not_recognized)} ($type:$id)")
		openUrl(context, url)
	}

}
