package ru.fantlab.android.data.dao

import android.content.Context
import android.support.v4.app.Fragment
import ru.fantlab.android.R
import ru.fantlab.android.ui.modules.author.bibliography.AuthorBibliographyFragment
import ru.fantlab.android.ui.modules.author.editions.AuthorEditionsFragment
import ru.fantlab.android.ui.modules.author.overview.AuthorOverviewFragment
import ru.fantlab.android.ui.modules.author.responses.AuthorResponsesFragment
import ru.fantlab.android.ui.modules.award.contests.AwardContestsFragment
import ru.fantlab.android.ui.modules.award.nominations.AwardNominationsFragment
import ru.fantlab.android.ui.modules.award.overview.AwardOverviewFragment
import ru.fantlab.android.ui.modules.classificator.age.ClassificationAgeFragment
import ru.fantlab.android.ui.modules.classificator.characteristics.ClassificationCharacteristicsFragment
import ru.fantlab.android.ui.modules.classificator.genres.ClassificationGenreFragment
import ru.fantlab.android.ui.modules.classificator.linearity.ClassificationLinearityFragment
import ru.fantlab.android.ui.modules.classificator.locate.ClassificationLocateFragment
import ru.fantlab.android.ui.modules.classificator.story.ClassificationStoryFragment
import ru.fantlab.android.ui.modules.classificator.time.ClassificationTimeFragment
import ru.fantlab.android.ui.modules.edition.content.EditionContentFragment
import ru.fantlab.android.ui.modules.edition.overview.EditionOverviewFragment
import ru.fantlab.android.ui.modules.profile.marks.ProfileMarksFragment
import ru.fantlab.android.ui.modules.profile.overview.ProfileOverviewFragment
import ru.fantlab.android.ui.modules.profile.responses.ProfileResponsesFragment
import ru.fantlab.android.ui.modules.search.authors.SearchAuthorsFragment
import ru.fantlab.android.ui.modules.search.awards.SearchAwardsFragment
import ru.fantlab.android.ui.modules.search.editions.SearchEditionsFragment
import ru.fantlab.android.ui.modules.search.works.SearchWorksFragment
import ru.fantlab.android.ui.modules.theme.fragment.ThemeFragment
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
					FragmentPagerAdapterModel(context.getString(R.string.editions), SearchEditionsFragment()),
					FragmentPagerAdapterModel(context.getString(R.string.awards), SearchAwardsFragment())
			)
		}

		fun buildForAuthor(context: Context, authorId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.overview), AuthorOverviewFragment.newInstance(authorId)),
					FragmentPagerAdapterModel(context.getString(R.string.bibiography), AuthorBibliographyFragment.newInstance(authorId)),
					FragmentPagerAdapterModel(context.getString(R.string.editions), AuthorEditionsFragment.newInstance(authorId)),
					FragmentPagerAdapterModel(context.getString(R.string.responses), AuthorResponsesFragment.newInstance(authorId))
			)
		}

		fun buildForAward(context: Context, awardId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.overview), AwardOverviewFragment.newInstance(awardId)),
					FragmentPagerAdapterModel(context.getString(R.string.contests), AwardContestsFragment.newInstance(awardId)),
					FragmentPagerAdapterModel(context.getString(R.string.nominations), AwardNominationsFragment.newInstance(awardId))
			)
		}

		fun buildForWork(context: Context, workId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.overview), WorkOverviewFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.classification), WorkClassificationFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.responses), WorkResponsesFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.editions), WorkEditionsFragment.newInstance(workId))
			)
		}

		fun buildForEdition(context: Context, editionId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.overview), EditionOverviewFragment.newInstance(editionId)),
					FragmentPagerAdapterModel(context.getString(R.string.content), EditionContentFragment.newInstance(editionId))
			)
		}

		fun buildForTheme(): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(FragmentPagerAdapterModel("", ThemeFragment.newInstance(R.style.ThemeLight)),
					FragmentPagerAdapterModel("", ThemeFragment.newInstance(R.style.ThemeDark))
			)
		}

		fun buildForClassificator(context: Context, workId: Int): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.genre), ClassificationGenreFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.characteristics), ClassificationCharacteristicsFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.locate), ClassificationLocateFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.time), ClassificationTimeFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.story), ClassificationStoryFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.linearity), ClassificationLinearityFragment.newInstance(workId)),
					FragmentPagerAdapterModel(context.getString(R.string.age), ClassificationAgeFragment.newInstance(workId))
			)
		}
	}
}