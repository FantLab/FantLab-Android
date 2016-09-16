package org.odddev.fantlab.core.validator;

import android.support.annotation.StringRes;

import org.odddev.fantlab.R;

/**
 * @author kenrube
 * @date 30.08.16
 */

public enum Errors {

    ERROR_EMPTY(R.string.error_string_empty),
    ERROR_LOGIN_BUSY(R.string.error_username_busy),
    ERROR_INCORRECT_EMAIL(R.string.error_email_incorrect),
    ERROR_INCORRECT_URL(R.string.error_url_incorrect),
    ERROR_INCORRECT_ANSWER(R.string.error_answer_incorrect);

    @StringRes
    public int textResource;

    Errors(@StringRes int textResource) {
        this.textResource = textResource;
    }
}
