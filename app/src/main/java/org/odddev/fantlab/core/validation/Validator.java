package org.odddev.fantlab.core.validation;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;

/**
 * @author kenrube
 * @since 26.11.16
 */

public abstract class Validator extends BaseObservable {

    public ObservableArrayMap<Integer, String> fields = new ObservableArrayMap<>();
    public ObservableArrayMap<Integer, String> fieldErrors = new ObservableArrayMap<>();

    protected Validator() {
        fields.addOnMapChangedCallback(
                new ObservableMap.OnMapChangedCallback<ObservableMap<Integer, String>, Integer, String>() {
                    @Override
                    public void onMapChanged(ObservableMap<Integer, String> integerStringObservableMap, Integer integer) {
                        validate(integer);
                    }
                });
    }

    protected abstract void validate(Integer field);

    protected abstract boolean areFieldsValid();
}
