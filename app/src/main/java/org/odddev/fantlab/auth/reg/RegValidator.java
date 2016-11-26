package org.odddev.fantlab.auth.reg;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;
import android.support.annotation.IntDef;
import android.util.Patterns;

import org.odddev.fantlab.R;

/**
 * @author kenrube
 * @since 18.09.16
 */

public class RegValidator extends BaseObservable {

    @IntDef({
            FIELD.USERNAME,
            FIELD.PASSWORD,
            FIELD.EMAIL})
    public @interface FIELD {

        int USERNAME = 0;
        int PASSWORD = 1;
        int EMAIL = 2;
    }

    private Resources mResources;

    public ObservableArrayMap<Integer, String> fields = new ObservableArrayMap<>();
    public ObservableArrayMap<Integer, String> fieldErrors = new ObservableArrayMap<>();

    int birthDay;
    int birthMonth;
    int birthYear;

    RegValidator(Context context) {
        mResources = context.getResources();
        fields.put(FIELD.USERNAME, "");
        fields.put(FIELD.PASSWORD, "");
        fields.put(FIELD.EMAIL, "");
        fields.addOnMapChangedCallback(onMapChangedCallback);
        fieldErrors.put(FIELD.USERNAME, null);
        fieldErrors.put(FIELD.PASSWORD, null);
        fieldErrors.put(FIELD.EMAIL, null);
    }

    private void validate(@FIELD int field) {
        switch (field) {
            case FIELD.USERNAME: {
                fieldErrors.put(FIELD.USERNAME, fields.get(FIELD.USERNAME).trim().isEmpty()
                        ? mResources.getString(R.string.error_username_empty)
                        : null);
                break;
            }
            case FIELD.PASSWORD: {
                fieldErrors.put(FIELD.PASSWORD, fields.get(FIELD.PASSWORD).trim().isEmpty()
                        ? mResources.getString(R.string.error_password_empty)
                        : null);
                break;
            }
            case FIELD.EMAIL: {
                fieldErrors.put(FIELD.EMAIL, !Patterns.EMAIL_ADDRESS.matcher(fields.get(FIELD.EMAIL)).matches()
                        ? mResources.getString(R.string.error_email_incorrect)
                        : null);
                break;
            }
        }
    }

    boolean areFieldsValid() {
        for (Integer field : fields.keySet()) {
            validate(field);
        }
        return fieldErrors.get(FIELD.USERNAME) == null
                && fieldErrors.get(FIELD.PASSWORD) == null
                && fieldErrors.get(FIELD.EMAIL) == null;
    }

    private ObservableMap.OnMapChangedCallback<ObservableMap<Integer, String>, Integer, String> onMapChangedCallback =
            new ObservableMap.OnMapChangedCallback<ObservableMap<Integer, String>, Integer, String>() {
                @Override
                public void onMapChanged(ObservableMap<Integer, String> integerStringObservableMap, Integer integer) {
                    validate(integer);
                }
            };
}
