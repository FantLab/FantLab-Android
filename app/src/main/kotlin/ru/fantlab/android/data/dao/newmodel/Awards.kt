package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Awards(
		@SerializedName("nom") val nominations: ArrayList<Nomination>,
		@SerializedName("win") val wins: ArrayList<Nomination>
) : Parcelable