package ru.fantlab.android.provider.glide

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream

@GlideModule
class AppGlideModule : AppGlideModule() {
	override fun registerComponents(
			context: Context, glide: Glide, registry: Registry
	) {
		registry.register(GlideDecodedResource::class.java, PictureDrawable::class.java, GlideDrawableTranscoder())
				.append(InputStream::class.java, GlideDecodedResource::class.java, GlideDecoder())
	}

	override fun isManifestParsingEnabled(): Boolean = false

}