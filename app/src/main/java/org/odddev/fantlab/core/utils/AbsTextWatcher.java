package org.odddev.fantlab.core.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author kenrube
 * @date 17.09.16
 */

public abstract class AbsTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
