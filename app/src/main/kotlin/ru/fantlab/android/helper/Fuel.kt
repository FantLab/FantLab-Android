package ru.fantlab.android.helper

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.google.gson.Gson
import com.google.gson.JsonParser

private val jsonParser = JsonParser()
private val gson = Gson().newBuilder().setPrettyPrinting().create()

fun Request.format(): String = buildString {
	val url = url.toString()
	val safeUrl = if (url.contains("password")) {
		url.substring(0, url.indexOf("password") - 1)
	} else {
		url
	}
	appendln("--> $method $safeUrl")
	appendln("Headers : (${headers.size})")
	headers.transformIterate({ key: String, value: String -> appendln("\t$key : $value") })
	appendln("Body :\n${String(body.toByteArray()).prettify()}")
}

fun Response.format(): String = buildString {
	appendln("<-- $statusCode")
	appendln("Response : $responseMessage")
	appendln("Length : $contentLength")
	appendln("Headers : (${headers.size})")
	headers.transformIterate({ key: String, value: String -> appendln("\t$key : $value") })
	appendln("Duration : ${getRequestDuration()}ms")
	appendln("Body :\n${String(body().toByteArray()).prettify()}")
}

fun Response.getRequestDuration(): String {
	val sentMillisHeaderValue = header("X-Android-Sent-Millis").lastOrNull()
	val receivedMillisHeaderValue = header("X-Android-Received-Millis").lastOrNull()
	return if (sentMillisHeaderValue == null || receivedMillisHeaderValue == null) {
		"Unknown"
	} else {
		(receivedMillisHeaderValue.toLong() - sentMillisHeaderValue.toLong()).toString()
	}
}

private fun String.prettify(): String = gson.toJson(jsonParser.parse(this))