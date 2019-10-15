package ru.fantlab.android.helper

import java.io.Serializable

// контейнер для 4 элементов
data class Tuple4<out A, out B, out C, out D>(
		val first: A,
		val second: B,
		val third: C,
		val fourth: D
) : Serializable {
	override fun toString(): String = "($first, $second, $third, $fourth)"
}