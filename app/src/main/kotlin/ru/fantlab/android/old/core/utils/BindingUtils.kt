package ru.fantlab.android.old.core.utils

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

import ru.fantlab.android.R

object BindingUtils {

	// todo удалить
	@BindingAdapter("onClick")
	@JvmStatic
	fun bindOnClick(view: View, runnable: Runnable) {
		view.setOnClickListener { runnable.run() }
	}

	// todo удалить
	@BindingAdapter("error")
	@JvmStatic
	fun bindError(textInputLayout: TextInputLayout, error: String?) {
		with (textInputLayout) {
			isErrorEnabled = error != null
			this.error = error
		}
	}

	// todo удалить
	@BindingAdapter("srcUri", "isCircle", "placeholder")
	@JvmStatic
	fun bindSrcUri(imageView: ImageView, uri: String, isCircle: Boolean,
				   placeholder: Drawable) {
		val builder = Glide
				.with(imageView.context)
				.load(uri)
				.placeholder(placeholder)
				.error(placeholder)
		if (isCircle) {
			builder.transform(CircleTransform(imageView.context))
		}
		builder.into(imageView)
	}

	// todo удалить
	@BindingAdapter("srcUri")
	@JvmStatic
	fun bindSrcUri(imageView: ImageView, uri: String) {
		Glide
				.with(imageView.context)
				.load(uri)
				.into(imageView)
	}

	// todo перейти на саппортовские шрифты (https://developer.android.com/guide/topics/ui/look-and-feel/fonts-in-xml.html)
	@BindingAdapter("font")
	@JvmStatic
	fun bindFont(textView: TextView, fontName: String) {
		val robotoMedium = textView.context.getString(R.string.roboto_medium)
		if (fontName == robotoMedium && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			textView.typeface = Typeface.createFromAsset(
					textView.context.assets, "fonts/$robotoMedium.ttf")
		} else {
			textView.typeface = Typeface.create(fontName, Typeface.NORMAL)
		}
	}

	// todo удалить
	@BindingConversion
	@JvmStatic
	fun convertConditionToVisibility(condition: Boolean) = if (condition) View.VISIBLE else View.GONE
}
