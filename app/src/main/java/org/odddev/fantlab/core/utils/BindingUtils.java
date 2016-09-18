package org.odddev.fantlab.core.utils;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.TextInputLayout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author kenrube
 * @date 17.09.16
 */

public class BindingUtils {

    @BindingAdapter("app:onClick")
    public static void bindOnClick(View view, final Runnable runnable) {
        view.setOnClickListener(v -> runnable.run());
    }

    @BindingAdapter("app:textWatcher")
    public static void bindTextWatcher(TextView textView, final TextWatcher newValue) {
        textView.addTextChangedListener(newValue);
    }

    @BindingAdapter("app:error")
    public static void bindError(TextInputLayout textInputLayout, final String error) {
        textInputLayout.setErrorEnabled(error != null);
        textInputLayout.setError(error);
    }

    @BindingAdapter("app:color")
    public static void bindColor(ProgressBar progressBar, final @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else {
            progressBar.setProgressTintList(ColorStateList.valueOf(color));
        }
    }

    @BindingConversion
    public static int convertConditionToVisibility(final boolean condition) {
        return condition ? View.VISIBLE : View.GONE;
    }
}
