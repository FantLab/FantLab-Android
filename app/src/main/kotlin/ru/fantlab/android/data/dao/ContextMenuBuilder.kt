package ru.fantlab.android.data.dao

import android.content.Context
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Pubnews
import ru.fantlab.android.data.dao.model.Pubplans
import ru.fantlab.android.provider.rest.*

object ContextMenuBuilder {

	fun buildForMarks(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.change_mark), R.drawable.ic_star, "revote"),
						ContextMenus.MenuItem(context.getString(R.string.delete_mark), R.drawable.ic_delete, "delete")
				)
		items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

		return items
	}

	fun buildForResponses(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.vote), R.drawable.ic_star, "votes", true)
				)
		items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

		val marks =
				arrayListOf(
						ContextMenus.MenuItem("+1", R.drawable.ic_thumb_up, "vote"),
						ContextMenus.MenuItem("-1", R.drawable.ic_thumb_down, "vote")
				)
		items.add(ContextMenus(context.getString(R.string.select_mark), marks, "votes"))

		return items
	}

	fun buildForResponse(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem("+1", R.drawable.ic_thumb_up, "vote"),
						ContextMenus.MenuItem("-1", R.drawable.ic_thumb_down, "vote")
				)
		items.add(ContextMenus(context.getString(R.string.select_mark), actions, "votes"))

		return items
	}

	fun buildForProfile(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.show_profile), R.drawable.ic_profile, "profile"),
						ContextMenus.MenuItem(context.getString(R.string.send_message), R.drawable.ic_message, "message", true)
				)
		items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

		return items
	}

	fun buildForResponseSorting(context: Context, sortBy: ResponsesSortOption): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_date), R.drawable.ic_time_small, "BY_DATE", selected = ResponsesSortOption.values()[0] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_rating), R.drawable.ic_thumb_up_small, "BY_RATING", selected = ResponsesSortOption.values()[1] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_mark), R.drawable.ic_star_small, "BY_MARK", selected = ResponsesSortOption.values()[2] == sortBy)
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "main"))

		return items
	}

	fun buildForAwardsSorting(context: Context, sortBy: AwardsSortOption): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "BY_NAME", selected = AwardsSortOption.values()[0] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_country), R.drawable.ic_location, "BY_COUNTRY", selected = AwardsSortOption.values()[1] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_type), R.drawable.ic_type_small, "BY_TYPE", selected = AwardsSortOption.values()[2] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_lang), R.drawable.ic_language, "BY_LANG", selected = AwardsSortOption.values()[3] == sortBy)
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "main"))

		return items
	}

	fun buildForMarksCharts(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.markchart_1), R.drawable.ic_chart, "chart"),
						ContextMenus.MenuItem(context.getString(R.string.markchart_2), R.drawable.ic_chart, "chart")
				)
		items.add(ContextMenus(context.getString(R.string.select_chart), actions, "main"))

		return items
	}

	fun buildForAuthorsSorting(context: Context, sort: Int): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions = arrayListOf<ContextMenus.MenuItem>()
		val alphabetIdsArray = context.resources.getIntArray(R.array.alphabet_array_id)
		context.resources.getStringArray(R.array.alphabet_array).mapIndexed { index, title ->
			actions.add(ContextMenus.MenuItem(title, null, alphabetIdsArray[index].toString(), selected = sort == alphabetIdsArray[index]))
		}

		items.add(ContextMenus(context.getString(R.string.alphabet_authors), actions, "main"))

		return items
	}

	fun buildForPublishersSorting(context: Context, sortBy: PublishersSortOption): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "sort", selected = PublishersSortOption.values()[0] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_editions_count), R.drawable.ic_edition, "sort", selected = PublishersSortOption.values()[1] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_country), R.drawable.ic_location, "sort", selected = PublishersSortOption.values()[2] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_city), R.drawable.ic_home, "sort", selected = PublishersSortOption.values()[3] == sortBy)
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "sort"))

		return items
	}

	fun buildForPublishersFilter(context: Context, filterCategory: Int, filterCountry: Int): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_country), R.drawable.ic_location, "countries"),
						ContextMenus.MenuItem(context.getString(R.string.sort_category), R.drawable.ic_type_small, "category")
				)
		items.add(ContextMenus(context.getString(R.string.select_filter), actions, "filter"))

		val countries = ArrayList<ContextMenus.MenuItem>()
		val countriesIdsArray = context.resources.getStringArray(R.array.countries_array_id)
		context.resources.getStringArray(R.array.countries_array).mapIndexed { index, title ->
			countries.add(ContextMenus.MenuItem(title, null, countriesIdsArray[index], selected = filterCountry == countriesIdsArray[index].toInt()))
		}

		val categories = ArrayList<ContextMenus.MenuItem>()
		val categoriesIdsArray = context.resources.getStringArray(R.array.categories_array_id)
		context.resources.getStringArray(R.array.categories_array).mapIndexed { index, title ->
			categories.add(ContextMenus.MenuItem(title, null, categoriesIdsArray[index], selected = filterCategory == categoriesIdsArray[index].toInt()))
		}

		items.add(ContextMenus(context.getString(R.string.select_country), countries, "countries"))
		items.add(ContextMenus(context.getString(R.string.select_category), categories, "category"))

		return items
	}

	fun buildForPubnewsSorting(context: Context, sortBy: PubnewsSortOption): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_date), R.drawable.ic_time_small, "sort", selected = PubnewsSortOption.values()[0] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_type), R.drawable.ic_type_small, "sort", selected = PubnewsSortOption.values()[1] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_publisher), R.drawable.ic_publishers, "sort", selected = PubnewsSortOption.values()[2] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_author), R.drawable.ic_person, "sort", selected = PubnewsSortOption.values()[3] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "sort", selected = PubnewsSortOption.values()[4] == sortBy)
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "sort"))

		return items
	}

	fun buildForPubnewsFilter(context: Context, publishersList: List<Pubnews.Publisher>, filterLang: Int, filterPublisher: Int): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_lang), R.drawable.ic_language, "lang"),
						ContextMenus.MenuItem(context.getString(R.string.sort_publisher), R.drawable.ic_publishers, "publisher")
				)
		items.add(ContextMenus(context.getString(R.string.select_filter), actions, "filter"))

		val langs = ArrayList<ContextMenus.MenuItem>()
		val langIdsArray = context.resources.getStringArray(R.array.lang_array_id)
		context.resources.getStringArray(R.array.lang_array).mapIndexed { index, title ->
			langs.add(ContextMenus.MenuItem(title, null, langIdsArray[index], selected = filterLang == langIdsArray[index].toInt()))
		}

		val categories = ArrayList<ContextMenus.MenuItem>()
		categories.add(ContextMenus.MenuItem("Все", null, "0", selected = 0 == filterPublisher))
		publishersList.mapIndexed { index, publisher ->
			categories.add(ContextMenus.MenuItem(publisher.name, null, publisher.publisherId, selected = filterPublisher == publisher.publisherId.toInt()))
		}

		items.add(ContextMenus(context.getString(R.string.select_country), langs, "lang"))
		items.add(ContextMenus(context.getString(R.string.select_category), categories, "publisher"))

		return items
	}

	fun buildForPubplansSorting(context: Context, sortBy: PubplansSortOption): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_correct), R.drawable.ic_correct, "sort", selected = PubplansSortOption.values()[0] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_date), R.drawable.ic_time_small, "sort", selected = PubplansSortOption.values()[1] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_type), R.drawable.ic_type_small, "sort", selected = PubplansSortOption.values()[2] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_publisher), R.drawable.ic_publishers, "sort", selected = PubplansSortOption.values()[3] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_author), R.drawable.ic_person, "sort", selected = PubplansSortOption.values()[4] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "sort", selected = PubplansSortOption.values()[5] == sortBy)
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "sort"))

		return items
	}

	fun buildForPubplansFilter(context: Context, publishersList: List<Pubplans.Publisher>, filterLang: Int, filterPublisher: Int): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_lang), R.drawable.ic_language, "lang"),
						ContextMenus.MenuItem(context.getString(R.string.sort_publisher), R.drawable.ic_publishers, "publisher")
				)
		items.add(ContextMenus(context.getString(R.string.select_filter), actions, "filter"))

		val langs = ArrayList<ContextMenus.MenuItem>()
		val langIdsArray = context.resources.getStringArray(R.array.lang_array_id)
		context.resources.getStringArray(R.array.lang_array).mapIndexed { index, title ->
			langs.add(ContextMenus.MenuItem(title, null, langIdsArray[index], selected = filterLang == langIdsArray[index].toInt()))
		}

		val categories = ArrayList<ContextMenus.MenuItem>()
		categories.add(ContextMenus.MenuItem("Все", null, "0", selected = 0 == filterPublisher))
		publishersList.mapIndexed { index, publisher ->
			categories.add(ContextMenus.MenuItem(publisher.name, null, publisher.publisherId, selected = filterPublisher == publisher.publisherId.toInt()))
		}

		items.add(ContextMenus(context.getString(R.string.select_country), langs, "lang"))
		items.add(ContextMenus(context.getString(R.string.select_category), categories, "publisher"))

		return items
	}

	fun buildForAutplansSorting(context: Context, sortBy: AutplansSortOption): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_correct), R.drawable.ic_correct, "sort", selected = AutplansSortOption.values()[0] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_author), R.drawable.ic_person, "sort", selected = AutplansSortOption.values()[1] == sortBy),
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "sort", selected = AutplansSortOption.values()[2] == sortBy)
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "sort"))

		return items
	}

	fun buildForAutplansFilter(context: Context, filterLang: Int): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_lang), R.drawable.ic_language, "lang")
				)
		items.add(ContextMenus(context.getString(R.string.select_filter), actions, "filter"))

		val langs = ArrayList<ContextMenus.MenuItem>()
		val langIdsArray = context.resources.getStringArray(R.array.lang_array_id)
		context.resources.getStringArray(R.array.lang_array).mapIndexed { index, title ->
			langs.add(ContextMenus.MenuItem(title, null, langIdsArray[index], selected = filterLang == langIdsArray[index].toInt()))
		}

		items.add(ContextMenus(context.getString(R.string.select_country), langs, "lang"))

		return items
	}

}