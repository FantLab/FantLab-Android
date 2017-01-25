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
            FIELD.BIRTH_DATE,
            FIELD.WEB_PAGE,
            FIELD.SKYPE,
            FIELD.ICQ
    })
    public @interface FIELD {

        int USERNAME = 0;
        int PASSWORD = 1;
        int EMAIL = 2;
        int BIRTH_DATE = 3;
        int WEB_PAGE = 4;
        int SKYPE = 5;
        int ICQ = 6;
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
        fieldErrors.put(field, getFieldError(field, value));
    }

    @Override
    protected boolean areFieldsValid() {
        boolean result = true;
        for (@FIELD int i = 0; i < 7; i++) {
            validate(i);
            if (fieldErrors.get(i) != null) result = false;
        }
        return result;
    }

    private String getFieldError(@FIELD int field, String value) {
        switch (field) {
            case FIELD.USERNAME:
                return value == null || value.trim().isEmpty()
                        ? resources.getString(R.string.auth_username_empty)
                        : null;
            case FIELD.PASSWORD:
                return value == null || value.trim().isEmpty()
                        ? resources.getString(R.string.auth_password_empty)
                        : null;
            case FIELD.EMAIL:
                return value == null || !Patterns.EMAIL_ADDRESS.matcher(value).matches()
                        ? resources.getString(R.string.register_email_incorrect)
                        : null;
            case FIELD.BIRTH_DATE:
                return null;
            case FIELD.WEB_PAGE:
                return value == null || !Patterns.WEB_URL.matcher(value).matches()
                        ? resources.getString(R.string.register_url_incorrect)
                        : null;
            case FIELD.SKYPE:
                return null;
            case FIELD.ICQ:
                return value == null || value.length() < 5 || value.length() > 9
                        ? resources.getString(R.string.register_icq_incorrect)
                        : null;
            default:
                return null;
        }
    }
}
