package ru.fantlab.android.data.dao

import android.support.annotation.DrawableRes
import java.io.Serializable

data class TabsCountStateModel(
		val count: Int = 0,
		val tabIndex: Int = 0,
		@DrawableRes
		val drawableId: Int = 0
) : Serializable