package org.odddev.fantlab.core.validator;

import android.support.annotation.StringRes;

import org.odddev.fantlab.R;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public enum Errors {

    ERROR_EMPTY(R.string.error_string_empty),
    ERROR_LOGIN_BUSY(R.string.error_login_busy),
    ERROR_INCORRECT_EMAIL(R.string.error_email_incorrect),
    ERROR_INCORRECT_ANSWER(R.string.error_answer_incorrect);

    @StringRes
    int textResource;

    Errors(@StringRes int textResource) {
        this.textResource = textResource;
    }
}
