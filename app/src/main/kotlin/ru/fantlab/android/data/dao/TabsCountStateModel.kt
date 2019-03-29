package ru.fantlab.android.data.dao

import androidx.annotation.DrawableRes
import java.io.Serializable

data class TabsCountStateModel(
		var count: Int = 0,
		var tabIndex: Int = 0,
		@DrawableRes val drawableId: Int = 0
) : Serializable