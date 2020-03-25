package ru.fantlab.android.ui.modules.awards.item

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Awards
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.data.dao.response.AuthorResponse
import ru.fantlab.android.data.dao.response.TranslatorResponse
import ru.fantlab.android.data.dao.response.WorkResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getAuthorPath
import ru.fantlab.android.provider.rest.getTranslatorInformationPath
import ru.fantlab.android.provider.rest.getWorkPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ItemAwardsPresenter : BasePresenter<ItemAwardsMvp.View>(),
		ItemAwardsMvp.Presenter {

	override fun getWorkAwards(workId: Int) {
		makeRestCall(
				getWorkAwardsInternal(workId).toObservable(),
				Consumer { nominations ->
					sendToView { it.onInitViews(nominations) }
				}
		)
	}

	private fun getWorkAwardsInternal(workId: Int) =
			getWorkAwardsFromServer(workId)
					.onErrorResumeNext {
						getWorkAwardsFromDb(workId)
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.showErrorMessage(err.message) } }

	private fun getWorkAwardsFromServer(workId: Int):
			Single<Awards> =
			DataManager.getWork(workId, showAwards = true)
					.map { getAwardsFromWork(it) }

	private fun getWorkAwardsFromDb(workId: Int):
			Single<Awards> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showAwards = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getAwardsFromWork(it) }

	private fun getAwardsFromWork(response: WorkResponse): Awards? = response.awards

	override fun getAuthorAwards(authorId: Int) {
		makeRestCall(
				getAuthorAwardsInternal(authorId).toObservable(),
				Consumer { nominations ->
					sendToView { it.onInitViews(nominations) }
				}
		)
	}

	private fun getAuthorAwardsInternal(authorId: Int) =
			getAuthorAwardsFromServer(authorId)
					.onErrorResumeNext {
						getAuthorAwardsFromDb(authorId)
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.showErrorMessage(err.message) } }

	private fun getAuthorAwardsFromServer(authorId: Int):
			Single<Awards> =
			DataManager.getAuthor(authorId, showAwards = true)
					.map { getAwardsFromAuthor(it) }

	private fun getAuthorAwardsFromDb(authorId: Int):
			Single<Awards> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAuthorPath(authorId, showAwards = true))
					.map { it.response }
					.map { AuthorResponse.Deserializer().deserialize(it) }
					.map { getAwardsFromAuthor(it) }

	private fun getAwardsFromAuthor(response: AuthorResponse): Awards? = response.awards

	override fun getTranslatorAwards(translatorId: Int) {
		makeRestCall(
				getTranslatorAwardsInternal(translatorId).toObservable(),
				Consumer { nominations ->
					sendToView { it.onInitViews(nominations) }
				}
		)
	}

	private fun getTranslatorAwardsInternal(translatorId: Int) =
			getTranslatorAwardsFromServer(translatorId)
					.onErrorResumeNext {
						getTranslatorAwardsFromDb(translatorId)
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.showErrorMessage(err.message) } }

	private fun getTranslatorAwardsFromServer(translatorId: Int):
			Single<Awards> =
			DataManager.getTranslatorInformation(translatorId, showAwards = true)
					.map { getAwardsFromTranslator(it) }

	private fun getTranslatorAwardsFromDb(translatorId: Int):
			Single<Awards> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getTranslatorInformationPath(translatorId, showAwards = true))
					.map { it.response }
					.map { TranslatorResponse.Deserializer().deserialize(it) }
					.map { getAwardsFromTranslator(it) }

	private fun getAwardsFromTranslator(response: TranslatorResponse): Awards? = Awards(arrayListOf(), Translator.AwardsConverter().convert(response.awards))
}