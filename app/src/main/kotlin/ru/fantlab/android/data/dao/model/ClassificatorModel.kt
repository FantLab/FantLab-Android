package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ClassificatorModel(
		@SerializedName("id") val id: Int,
		@SerializedName("name") val name: String,
		@SerializedName("description") val descr: String?,
		@SerializedName("childs") val childs: ArrayList<ClassificatorModel>?
) : Parcelable