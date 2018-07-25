package ru.fantlab.android.data.dao

import android.content.Context
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus

class ContextMenuBuilder{
	companion object {

		fun buildForMarks(context: Context): ArrayList<ContextMenus> {
			val items = ArrayList<ContextMenus>()

			val actions =
					arrayListOf(
							ContextMenus.MenuItem(context.getString(R.string.change_mark), R.drawable.ic_star, "revote"),
							ContextMenus.MenuItem(context.getString(R.string.delete_mark), R.drawable.ic_delete, "delete")
					)
			items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

			val marks =
					arrayListOf(
							ContextMenus.MenuItem("1", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("2", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("3", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("4", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("5", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("6", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("7", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("8", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("9", R.drawable.ic_star, "mark"),
							ContextMenus.MenuItem("10", R.drawable.ic_star, "mark")
					)
			items.add(ContextMenus(context.getString(R.string.select_mark), marks, "revote"))

			return items
		}

		fun buildForResponses(context: Context): ArrayList<ContextMenus>{
			val items = ArrayList<ContextMenus>()

			val actions =
					arrayListOf(
							ContextMenus.MenuItem(context.getString(R.string.show_profile), R.drawable.ic_profile, "profile"),
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

		fun buildForProfile(context: Context): ArrayList<ContextMenus>{
			val items = ArrayList<ContextMenus>()

			val actions =
					arrayListOf(
							ContextMenus.MenuItem(context.getString(R.string.show_profile), R.drawable.ic_profile, "profile"),
							ContextMenus.MenuItem(context.getString(R.string.send_message), R.drawable.ic_message, "message", true)
					)
			items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

			return items
		}

	}
}