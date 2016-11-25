package org.odddev.fantlab.auth.login;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;
import android.support.annotation.IntDef;

import org.odddev.fantlab.R;

/**
 * @author kenrube
 * @since 17.09.16
 */

public class LoginValidator extends BaseObservable {

    @IntDef({
            FIELD.USERNAME,
            FIELD.PASSWORD})
    public @interface FIELD {

        int USERNAME = 0;
        int PASSWORD = 1;
    }

    private Resources mResources;

    public ObservableArrayMap<Integer, String> fields = new ObservableArrayMap<>();
    public ObservableArrayMap<Integer, String> fieldErrors = new ObservableArrayMap<>();

    LoginValidator(Context context) {
        mResources = context.getResources();
        fields.put(FIELD.USERNAME, "");
        fields.put(FIELD.PASSWORD, "");
        fields.addOnMapChangedCallback(onMapChangedCallback);
        fieldErrors.put(FIELD.USERNAME, null);
        fieldErrors.put(FIELD.PASSWORD, null);
    }

    private void validate(@FIELD int field) {
        switch (field) {
            case FIELD.USERNAME: {
                fieldErrors.put(FIELD.USERNAME, fields.get(FIELD.USERNAME).isEmpty()
                        ? mResources.getString(R.string.error_username_empty)
                        : null);
                break;
            }
            case FIELD.PASSWORD: {
                fieldErrors.put(FIELD.PASSWORD, fields.get(FIELD.PASSWORD).isEmpty()
                        ? mResources.getString(R.string.error_password_empty)
                        : null);
                break;
            }
        }
    }

    boolean isValid() {
        for (Integer field : fields.keySet()) {
            validate(field);
        }
        return fieldErrors.get(FIELD.USERNAME) == null
                && fieldErrors.get(FIELD.PASSWORD) == null;
    }

    private ObservableMap.OnMapChangedCallback<ObservableMap<Integer, String>, Integer, String> onMapChangedCallback =
            new ObservableMap.OnMapChangedCallback<ObservableMap<Integer, String>, Integer, String>() {
                @Override
                public void onMapChanged(ObservableMap<Integer, String> integerStringObservableMap, Integer integer) {
                    validate(integer);
                }
            };
}
