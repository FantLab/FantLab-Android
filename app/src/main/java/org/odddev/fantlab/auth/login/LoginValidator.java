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
                fieldErrors.put(FIELD.USERNAME, value == null || value.trim().isEmpty()
                        ? resources.getString(R.string.error_username_empty)
                        : null);
                break;
            }
            case FIELD.PASSWORD: {
                fieldErrors.put(FIELD.PASSWORD, value == null || value.trim().isEmpty()
                        ? resources.getString(R.string.error_password_empty)
                        : null);
                break;
            }
        }
    }

    @Override
    protected boolean areFieldsValid() {
        for (@FIELD int i = 0; i < 2; i++) {
            validate(i);
        }
        return fieldErrors.get(FIELD.USERNAME) == null
                && fieldErrors.get(FIELD.PASSWORD) == null;
    }
}
