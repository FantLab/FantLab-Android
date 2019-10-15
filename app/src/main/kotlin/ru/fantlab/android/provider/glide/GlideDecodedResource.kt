package ru.fantlab.android.provider.glide

import android.graphics.Bitmap
import com.caverock.androidsvg.SVG

data class GlideDecodedResource(
		val svg: SVG? = null,
		val bitmap: Bitmap? = null)