package ru.fantlab.android.old.authors

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AuthorsResponse(
		val list: List<Author>
) {
	@Keep
	data class Author(
			@SerializedName("autor_id")
			val authorId: Int,
			@SerializedName("is_fv")
			val isFv: Int,
			val name: String,
			@SerializedName("name_orig")
			val nameOrig: String,
			@SerializedName("name_rp")
			val nameRp: String,
			@SerializedName("name_short")
			val nameShort: String
	)
}