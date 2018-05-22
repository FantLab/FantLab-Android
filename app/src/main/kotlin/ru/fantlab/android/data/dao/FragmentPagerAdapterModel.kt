package ru.fantlab.android.data.dao

import android.content.Context
import android.support.v4.app.Fragment
import ru.fantlab.android.R
import ru.fantlab.android.ui.modules.author.editions.AuthorEditionsFragment
import ru.fantlab.android.ui.modules.author.overview.AuthorOverviewFragment
import ru.fantlab.android.ui.modules.author.responses.AuthorResponsesFragment
import ru.fantlab.android.ui.modules.author.works.AuthorWorksFragment
import ru.fantlab.android.ui.modules.edition.content.EditionContentFragment
import ru.fantlab.android.ui.modules.edition.overview.EditionOverviewFragment
import ru.fantlab.android.ui.modules.edition.photos.EditionPhotosFragment
import ru.fantlab.android.ui.modules.profile.marks.ProfileMarksFragment
import ru.fantlab.android.ui.modules.profile.overview.ProfileOverviewFragment
import ru.fantlab.android.ui.modules.profile.responses.ProfileResponsesFragment
import ru.fantlab.android.ui.modules.search.authors.SearchAuthorsFragment
import ru.fantlab.android.ui.modules.search.editions.SearchEditionsFragment
import ru.fantlab.android.ui.modules.search.works.SearchWorksFragment
import ru.fantlab.android.ui.modules.work.analogs.WorkAnalogsFragment
import ru.fantlab.android.ui.modules.work.classification.WorkClassificationFragment
import ru.fantlab.android.ui.modules.work.editions.WorkEditionsFragment
import ru.fantlab.android.ui.modules.work.overview.WorkOverviewFragment
import ru.fantlab.android.ui.modules.work.responses.WorkResponsesFragment

data class FragmentPagerAdapterModel(
		val title: String,
		val fragment: Fragment,
		val key: String? = null
) {

	companion object {

		fun buildForProfile(context: Context, userId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.overview), ProfileOverviewFragment.newInstance(userId)),
					FragmentPagerAdapterModel(context.getString(R.string.marks), ProfileMarksFragment.newInstance(userId)),
					FragmentPagerAdapterModel(context.getString(R.string.responses), ProfileResponsesFragment.newInstance(userId))
			)
		}

		fun buildForSearch(context: Context): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.authors), SearchAuthorsFragment()),
					FragmentPagerAdapterModel(context.getString(R.string.works), SearchWorksFragment()),
					FragmentPagerAdapterModel(context.getString(R.string.editions), SearchEditionsFragment())/*,
					FragmentPagerAdapterModel(context.getString(R.string.awards), SearchAwardsFragment())*/
			)
		}

		fun buildForAuthor(context: Context, authorId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.overview), AuthorOverviewFragment.newInstance(authorId)),
					FragmentPagerAdapterModel(context.getString(R.string.works), AuthorWorksFragment.newInstance(authorId)),
					FragmentPagerAdapterModel(context.getString(R.string.editions), AuthorEditionsFragment.newInstance(authorId)),
					FragmentPagerAdapterModel(context.getString(R.string.responses), AuthorResponsesFragment.newInstance(authorId))
			)
		}

		fun buildForWork(context: Context, workId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.overview), WorkOverviewFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.classification), WorkClassificationFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.responses), WorkResponsesFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.editions), WorkEditionsFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.analogs), WorkAnalogsFragment.newInstance(workId))
			)
		}

		fun buildForEdition(context: Context, editionId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.overview), EditionOverviewFragment.newInstance(editionId)),
					FragmentPagerAdapterModel(context.getString(R.string.content), EditionContentFragment.newInstance(editionId)),
					FragmentPagerAdapterModel(context.getString(R.string.photos), EditionPhotosFragment.newInstance(editionId))
			)
		}
	}
}