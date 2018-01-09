package ru.fantlab.android.search

interface ISearchActions {

	fun onAuthorClicked(authorId: Int, name: String)

	fun onWorkClicked(workId: Int, name: String)

	fun onEditionClicked(editionId: Int, name: String)
}