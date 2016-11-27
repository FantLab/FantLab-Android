package org.odddev.fantlab.auth.reg;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.util.Patterns;

import org.odddev.fantlab.R;
import org.odddev.fantlab.core.validation.Validator;

/**
 * @author kenrube
 * @since 18.09.16
 */

public class RegValidator extends Validator {

    @IntDef({
            FIELD.USERNAME,
            FIELD.PASSWORD,
            FIELD.EMAIL,
            FIELD.BIRTH_DATE})
    public @interface FIELD {

        int USERNAME = 0;
        int PASSWORD = 1;
        int EMAIL = 2;
        int BIRTH_DATE = 3;
    }

    private Resources resources;

    int birthDay;
    int birthMonth;
    int birthYear;

    RegValidator(Context context) {
        super();
        resources = context.getResources();
    }

    @Override
    protected void validate(Integer field) {
        String value = fields.get(field);
        switch (field) {
            case FIELD.USERNAME: {
                fieldErrors.put(FIELD.USERNAME, value == null || value.trim().isEmpty()
                        ? resources.getString(R.string.auth_username_empty)
                        : null);
                break;
            }
            case FIELD.PASSWORD: {
                fieldErrors.put(FIELD.PASSWORD, value == null || value.trim().isEmpty()
                        ? resources.getString(R.string.auth_password_empty)
                        : null);
                break;
            }
            case FIELD.EMAIL: {
                fieldErrors.put(FIELD.EMAIL, value == null || !Patterns.EMAIL_ADDRESS.matcher(value).matches()
                        ? resources.getString(R.string.register_email_incorrect)
                        : null);
                break;
            }
            case FIELD.BIRTH_DATE: {
                fieldErrors.put(FIELD.BIRTH_DATE, null);
                break;
            }
        }
    }

    @Override
    protected boolean areFieldsValid() {
        boolean result = true;
        for (@FIELD int i = 0; i < 4; i++) {
            validate(i);
            if (fieldErrors.get(i) != null) result = false;
        }
        return result;
    }
}
