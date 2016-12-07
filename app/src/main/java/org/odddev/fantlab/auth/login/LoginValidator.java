package org.odddev.fantlab.auth.login;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IntDef;

import org.odddev.fantlab.R;
import org.odddev.fantlab.core.validation.Validator;

/**
 * @author kenrube
 * @since 17.09.16
 */

public class LoginValidator extends Validator {

    @IntDef({
            FIELD.USERNAME,
            FIELD.PASSWORD})
    public @interface FIELD {

        int USERNAME = 0;
        int PASSWORD = 1;
    }

    private Resources resources;

    LoginValidator(Context context) {
        super();
        resources = context.getResources();
    }

    @Override
    protected void validate(Integer field) {
        String value = fields.get(field);
        switch (field) {
            case FIELD.USERNAME: {
                fieldErrors.put(FIELD.USERNAME, getUsernameError(value));
                break;
            }
            case FIELD.PASSWORD: {
                fieldErrors.put(FIELD.PASSWORD, getPasswordError(value));
                break;
            }
        }
    }

    @Override
    protected boolean areFieldsValid() {
        boolean result = true;
        for (@FIELD int i = 0; i < 2; i++) {
            validate(i);
            if (fieldErrors.get(i) != null) result = false;
        }
        return result;
    }

    private String getUsernameError(String username) {
        return username == null || username.trim().isEmpty()
                ? resources.getString(R.string.auth_username_empty)
                : null;
    }

    private String getPasswordError(String password) {
        return password == null || password.trim().isEmpty()
                ? resources.getString(R.string.auth_password_empty)
                : null;
    }
}
