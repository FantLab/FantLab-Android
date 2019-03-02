package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Awards(
		@SerializedName("nom") val nominations: ArrayList<Nomination>,
		@SerializedName("win") val wins: ArrayList<Nomination>
) : Parcelable