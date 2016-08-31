package org.odddev.fantlab.core.validator;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public abstract class Validator<M> {

    public static final int NO_ERROR = -1;

    protected M mData;
    protected boolean mFormIsValid;

    protected abstract void validationRun();

    protected abstract boolean isValid();

    protected Validator(M data) {
        mData = data;
    }

    protected M getData() {
        return mData;
    }

    protected int checkString(String value) {
        if (value == null) {
            return Errors.ERROR_EMPTY.textResource;
        }
        return TextUtils.isEmpty(value.trim()) ? Errors.ERROR_EMPTY.textResource : NO_ERROR;
    }

    protected int checkEmail(String email) {
        int value = checkString(email);
        if (value != NO_ERROR) {
            return value;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() ?
                NO_ERROR : Errors.ERROR_INCORRECT_EMAIL.textResource;
    }
}
