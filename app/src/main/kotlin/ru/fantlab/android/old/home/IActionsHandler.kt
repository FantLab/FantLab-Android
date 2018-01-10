package ru.fantlab.android.old.home

interface IActionsHandler {

	fun openAuthor(id: Int, name: String)

	fun showBiography(bio: String)

	fun openWork(id: Int, name: String)

	fun openEdition(id: Int, name: String)
}