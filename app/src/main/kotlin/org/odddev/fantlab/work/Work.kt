package org.odddev.fantlab.work

class Work(
		val id: Int,
		val name: String,
		val nameBonus: String,
		val nameOrig: String,
		val notes: String,
		val notFinished: Boolean,
		val parent: Boolean,
		val preparing: Boolean,
		val type: Int,
		val year: Int,
		val canDownload: Boolean) {
}