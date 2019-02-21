package ru.fantlab.android.provider.parser

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import ru.fantlab.android.provider.scheme.SchemeParser


class LinksParserActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		onCreate(intent)
	}

	override fun onStart() {
		super.onStart()
		setVisible(true)
	}

	private fun onCreate(intent: Intent?) {
		if (intent == null || intent.action == null) {
			finish()
			return
		}
		if (intent.action == Intent.ACTION_VIEW) {
			if (intent.data != null) {
				onUriReceived(intent.data)
			}
		}
		finish()
	}

	private fun onUriReceived(uri: Uri?) {
		SchemeParser.launchUri(this, uri.toString().substringAfterLast("/").substringBefore("?"), "")
	}
}