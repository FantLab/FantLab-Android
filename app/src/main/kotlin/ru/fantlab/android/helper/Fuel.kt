package ru.fantlab.android.helper

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.google.gson.Gson
import com.google.gson.JsonParser

private val jsonParser = JsonParser()
private val gson = Gson().newBuilder().setPrettyPrinting().create()

fun Request.format(): String = buildString {
	appendln("--> $method $url")
	appendln("Headers : (${headers.size})")
	headers.transformIterate({ key: String, value: String -> appendln("\t$key : $value") })
	val bodyAsString = String(body.toByteArray())
	try {
		appendln("Body :\n${bodyAsString.prettify()}")
	} catch (e: Exception) {
		appendln("Body :\n$bodyAsString")
	}
}

fun Response.format(): String = buildString {
	appendln("<-- $statusCode")
	appendln("Response : $responseMessage")
	appendln("Length : $contentLength")
	appendln("Headers : (${headers.size})")
	headers.transformIterate({ key: String, value: String -> appendln("\t$key : $value") })
	appendln("Duration : ${getRequestDuration()}ms")
	val bodyAsString = String(body().toByteArray())
	try {
		appendln("Body :\n${bodyAsString.prettify()}")
	} catch (e: Exception) {
		appendln("Body :\n$bodyAsString")
	}
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