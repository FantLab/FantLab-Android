package org.odddev.fantlab.core.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

/**
 * @author kenrube
 * @since 17.09.16
 */

public class BindingUtils {

    @BindingAdapter("onClick")
    public static void bindOnClick(View view, final Runnable runnable) {
        view.setOnClickListener(v -> runnable.run());
    }

    @BindingAdapter("error")
    public static void bindError(TextInputLayout textInputLayout, final String error) {
        textInputLayout.setErrorEnabled(error != null);
        textInputLayout.setError(error);
    }

    @BindingAdapter({"srcUri", "isCircle", "placeholder"})
    public static void bindSrcUri(ImageView imageView, final String uri, final boolean isCircle,
                                  final Drawable placeholder) {
        DrawableRequestBuilder<String> builder = Glide
                .with(imageView.getContext())
                .load(uri)
                .placeholder(placeholder)
                .error(placeholder);
        if (isCircle) {
            builder = builder.transform(new CircleTransform(imageView.getContext()));
        }
        builder.into(imageView);
    }

    @BindingConversion
    public static int convertConditionToVisibility(final boolean condition) {
        return condition ? View.VISIBLE : View.GONE;
    }
}
