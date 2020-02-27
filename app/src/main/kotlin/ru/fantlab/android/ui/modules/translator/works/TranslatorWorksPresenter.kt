package ru.fantlab.android.ui.modules.translator.works

import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.data.dao.response.TranslatorResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getTranslatorInformationPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class TranslatorWorksPresenter : BasePresenter<TranslatorWorksMvp.View>(), TranslatorWorksMvp.Presenter {

    override fun getTranslatorWorks(translatorId: Int) {
        makeRestCall(
                retrieveTranslatorInfoInternal(translatorId).toObservable(),
                Consumer { translated ->
                    sendToView { it.onTranslatorInformationRetrieved(translated) }
                }
        )
    }

    private fun retrieveTranslatorInfoInternal(translatorId: Int) =
            retrieveTranslatorInfoFromServer(translatorId)
                    .onErrorResumeNext { retrieveTranslatorInfoFromDb(translatorId) }

    private fun retrieveTranslatorInfoFromServer(translatorId: Int): Single<HashMap<String, Translator.TranslatedWork>> =
            DataManager.getTranslatorInformation(translatorId, showTranslated = true)
                    .map { getTranslatorWorks(it) }

    private fun retrieveTranslatorInfoFromDb(translatorId: Int): Single<HashMap<String, Translator.TranslatedWork>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getTranslatorInformationPath(translatorId))
                    .map { it.response }
                    .map { TranslatorResponse.Deserializer().deserialize(it) }
                    .map { getTranslatorWorks(it) }

    private fun getTranslatorWorks(response: TranslatorResponse):
            HashMap<String, Translator.TranslatedWork> =
            response.translatedWorks
}