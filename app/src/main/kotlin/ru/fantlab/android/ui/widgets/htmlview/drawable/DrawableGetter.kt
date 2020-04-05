package ru.fantlab.android.ui.widgets.htmlview.drawable

import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.fantlab.android.R
import java.lang.ref.WeakReference
import java.util.*

class DrawableGetter(tv: TextView, val width: Int) : Html.ImageGetter {

	private var cachedTargets: MutableSet<GlideDrawableTarget>? = null
	private var container: WeakReference<TextView>? = null

	init {
		tv.setTag(R.id.drawable_callback, this)
		this.container = WeakReference(tv)
		this.cachedTargets = HashSet()
	}

	override fun getDrawable(url: String?): Drawable {
		val urlDrawable = UrlDrawable()
		if (container != null && container?.get() != null) {
			val context = container?.get()!!.context
			val target = GlideDrawableTarget(urlDrawable, container, width)
			Glide.with(context)
					.load(if (url!!.startsWith("//")) "http:$url" else url)
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image))
					.error(ContextCompat.getDrawable(context, R.drawable.ic_image))
					.into(target)
			cachedTargets?.add(target)
		}
		return urlDrawable
	}

}