package ru.fantlab.android.helper

import android.app.Activity
import android.content.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.ShareCompat
import android.widget.Toast
import es.dmoral.toasty.Toasty
import ru.fantlab.android.App
import ru.fantlab.android.R

object ActivityHelper {

	fun getActivity(content: Context?): Activity? {
		return when (content) {
			null -> null
			is Activity -> content
			is ContextWrapper -> getActivity(content.baseContext)
			else -> null
		}
	}

	fun getVisibleFragment(manager: FragmentManager): Fragment? {
		val fragments = manager.fragments
		if (fragments != null && !fragments.isEmpty()) {
			fragments
					.filter { it != null && it.isVisible }
					.forEach { return it }
		}
		return null
	}

	fun shareUrl(context: Context, url: String) {
		val activity = getActivity(context)
				?: throw IllegalArgumentException("Context given is not an instance of activity ${context.javaClass.name}")
		try {
			ShareCompat.IntentBuilder.from(activity)
					.setChooserTitle(context.getString(R.string.share))
					.setType("text/plain")
					.setText(url)
					.startChooser()
		} catch (e: ActivityNotFoundException) {
			Toasty.error(App.instance, e.message!!, Toast.LENGTH_LONG).show()
		}
	}

	fun copyToClipboard(context: Context, uri: String) {
		val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip = ClipData.newPlainText(context.getString(R.string.app_name), uri)
		clipboard.primaryClip = clip
		Toasty.success(App.instance, context.getString(R.string.success_copied)).show()
	}
}