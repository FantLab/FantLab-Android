package ru.fantlab.android.ui.modules.blogs.articles.overview

import ru.fantlab.android.data.dao.model.BlogArticle
import ru.fantlab.android.data.dao.model.BlogArticles
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

interface BlogArticleOverviewMvp {

	interface View : BaseMvp.View {

		fun onInitViews(article: BlogArticles.Article)

		fun onInitArticleText(article: BlogArticle.Article)

		fun onSetLike(likesCount: String, likeType: Boolean)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onSendVote(articleId: Int, likeType: Boolean)

		fun onGetArticle(articleId: String)
	}
}