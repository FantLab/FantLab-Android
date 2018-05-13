package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Awards(
		@SerializedName("nom") val nominations: ArrayList<Nomination>,
		@SerializedName("win") val wins: ArrayList<Nomination>
)