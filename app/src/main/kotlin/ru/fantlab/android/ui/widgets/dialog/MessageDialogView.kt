package ru.fantlab.android.ui.widgets.dialog

import android.os.Bundle
import ru.fantlab.android.ui.base.mvp.BaseBottomSheetDialog

/**
 * Created by Kosh on 16 Sep 2016, 2:15 PM
 */

class MessageDialogView : BaseBottomSheetDialog() {

	interface MessageDialogViewActionCallback {

		fun onMessageDialogActionClicked(isOk: Boolean, bundle: Bundle?)

		fun onDialogDismissed()
	}

	// todo реализовать
}