package ru.fantlab.android.core.validation

import android.databinding.BaseObservable
import android.databinding.ObservableArrayMap
import android.databinding.ObservableMap

abstract class Validator protected constructor() : BaseObservable() {

	var fields = ObservableArrayMap<Int, String>()
	var fieldErrors = ObservableArrayMap<Int, String>()

	init {
		fields.addOnMapChangedCallback(
				object : ObservableMap.OnMapChangedCallback<ObservableMap<Int, String>, Int, String>() {
					override fun onMapChanged(integerStringObservableMap: ObservableMap<Int, String>, integer: Int) {
						validate(integer)
					}
				})
	}

	protected abstract fun validate(field: Int)

	protected abstract fun areFieldsValid(): Boolean
}