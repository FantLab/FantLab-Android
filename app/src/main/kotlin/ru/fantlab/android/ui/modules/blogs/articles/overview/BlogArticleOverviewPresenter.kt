package ru.fantlab.android.ui.modules.blogs.articles.overview

import io.reactivex.functions.Consumer
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class BlogArticleOverviewPresenter : BasePresenter<BlogArticleOverviewMvp.View>(),
		BlogArticleOverviewMvp.Presenter {

	override fun onSendVote(articleId: Int, likeType: Boolean) = makeRestCall(
			DataManager.sendArticleLike(articleId, likeType).toObservable(),
			Consumer { response ->
				sendToView { it.onSetLike(response.likesCount, likeType) }
			})

	override fun onGetArticle(articleId: String) = makeRestCall(
			DataManager.getBlogArticle(articleId.toInt()).toObservable(),
			Consumer { response ->
				sendToView { it.onInitArticleText(response.article.article) }
			})
}