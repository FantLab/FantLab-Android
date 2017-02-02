package org.odddev.fantlab.core.utils

import android.content.Context
import android.graphics.*

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

/**
 * @author kenrube
 * *
 * @since 29.09.16
 */

internal class CircleTransform(context: Context) : BitmapTransformation(context) {

	override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? = circleCrop(pool, toTransform)

	private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
		if (source == null) return null

		val size = Math.min(source.width, source.height)
		val x = (source.width - size) / 2
		val y = (source.height - size) / 2

		val squared = Bitmap.createBitmap(source, x, y, size, size)

		var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
		if (result == null) {
			result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
		}

		val canvas = Canvas(result!!)
		val paint = Paint()
		paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
		paint.isAntiAlias = true
		val r = size / 2f
		canvas.drawCircle(r, r, r, paint)
		return result
	}

	override fun getId(): String {
		return javaClass.name
	}
}
