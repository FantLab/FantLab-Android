package org.odddev.fantlab.core.utils;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.TextInputLayout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    public static void bindTextWatcher(TextView textView, final TextWatcher textWatcher) {
        textView.addTextChangedListener(textWatcher);
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

    @BindingAdapter({"bind:roundSrcUri", "bind:placeholder"})
    public static void bindRoundSrcUri(ImageView imageView, final String uri,
                                       final Drawable placeholder) {
        Glide
                .with(imageView.getContext())
                .load(uri)
                .placeholder(placeholder)
                .error(placeholder)
                .transform(new CircleTransform(imageView.getContext()))
                .into(imageView);
    }

    @BindingAdapter({"bind:srcUri", "bind:placeholder"})
    public static void bindSrcUri(ImageView imageView, final String uri,
                                  final Drawable placeholder) {
        Glide
                .with(imageView.getContext())
                .load(uri)
                .placeholder(placeholder)
                .error(placeholder)
                .into(imageView);
    }

    @BindingConversion
    public static int convertConditionToVisibility(final boolean condition) {
        return condition ? View.VISIBLE : View.GONE;
    }
}
