package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Statistics(
		@SerializedName("editioncount") val editionCount: Int
)