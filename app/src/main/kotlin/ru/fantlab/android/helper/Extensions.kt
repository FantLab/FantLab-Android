package ru.fantlab.android.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.github.kittinunf.fuel.core.FuelError
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ErrorResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.BaseMvp
import java.io.IOException
import java.util.concurrent.TimeoutException
import kotlin.math.round


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
	this.addTextChangedListener(object : TextWatcher {
		override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

		override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

		override fun afterTextChanged(editable: Editable?) {
			afterTextChanged.invoke(editable.toString())
		}
	})
}

fun Throwable.implementError(context: BaseMvp.View?): Unit? {
	return if (this is FuelError) {
		if (this.response.data.toString(Charsets.UTF_8).isJson()) {
			val error = DataManager.gson.fromJson(this.response.data.toString(Charsets.UTF_8), ErrorResponse::class.java)
			if (error != null) {
				if (error.status == "AUTH_REQUIRED" || error.status == "REFRESH_TOKEN_EXPIRED")
					context?.onRequireLogin()
				else
					context?.showErrorMessage(error.context ?: error.status)
			} else
				context?.showMessage(R.string.error, getPrettifiedErrorMessage(this))
		} else
			context?.showMessage(R.string.error, getPrettifiedErrorMessage(this))
	} else
		context?.showMessage(R.string.error, getPrettifiedErrorMessage(this))
}

fun String?.isJson(): Boolean {
	try {
		JSONObject(this)
	} catch (ex: JSONException) {
		try {
			JSONArray(this)
		} catch (ex1: JSONException) {
			return false
		}
	}
	return true
}

private fun getPrettifiedErrorMessage(throwable: Throwable?): Int {
	return when (throwable) {
		is IOException -> R.string.request_error
		is TimeoutException -> R.string.unexpected_error
		else -> R.string.network_error
	}
}

fun Double.round(decimals: Int): Double {
	var multiplier = 1.0
	repeat(decimals) { multiplier *= 10 }
	return round(this * multiplier) / multiplier
}