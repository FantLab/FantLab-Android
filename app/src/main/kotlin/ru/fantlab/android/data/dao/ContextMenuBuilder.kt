package ru.fantlab.android.data.dao

import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus

class ContextMenuBuilder{
	companion object {

		fun buildForMarks(): ArrayList<ContextMenus> {
			val items = ArrayList<ContextMenus>()

			val actions =
					arrayListOf(
							ContextMenus.MenuItem(App.instance.getString(R.string.change_mark), R.drawable.ic_star, "revote"),
							ContextMenus.MenuItem(App.instance.getString(R.string.delete_mark), R.drawable.ic_delete, "delete")
					)
			items.add(ContextMenus(App.instance.getString(R.string.select_action), actions, "main"))

			return items
		}

		fun buildForResponses(): ArrayList<ContextMenus>{
			val items = ArrayList<ContextMenus>()

			val actions =
					arrayListOf(
							ContextMenus.MenuItem(App.instance.getString(R.string.vote), R.drawable.ic_star, "votes", true)
					)
			items.add(ContextMenus(App.instance.getString(R.string.select_action), actions, "main"))

			val marks =
					arrayListOf(
							ContextMenus.MenuItem("+1", R.drawable.ic_thumb_up, "vote"),
							ContextMenus.MenuItem("-1", R.drawable.ic_thumb_down, "vote")
					)
			items.add(ContextMenus(App.instance.getString(R.string.select_mark), marks, "votes"))

			return items
		}

		fun buildForProfile(): ArrayList<ContextMenus>{
			val items = ArrayList<ContextMenus>()

			val actions =
					arrayListOf(
							ContextMenus.MenuItem(App.instance.getString(R.string.show_profile), R.drawable.ic_profile, "profile"),
							ContextMenus.MenuItem(App.instance.getString(R.string.send_message), R.drawable.ic_message, "message", true)
					)
			items.add(ContextMenus(App.instance.getString(R.string.select_action), actions, "main"))

			return items
		}

		fun buildForResponseSorting(): ArrayList<ContextMenus>{
			val items = ArrayList<ContextMenus>()

			val actions =
					arrayListOf(
							ContextMenus.MenuItem(App.instance.getString(R.string.sort_date), R.drawable.ic_time_small, "BY_DATE"),
							ContextMenus.MenuItem(App.instance.getString(R.string.sort_rating), R.drawable.ic_thumb_up_small, "BY_RATING"),
							ContextMenus.MenuItem(App.instance.getString(R.string.sort_mark), R.drawable.ic_star_small, "BY_MARK")
					)
			items.add(ContextMenus(App.instance.getString(R.string.select_sort), actions, "main"))

			return items
		}

	}
}