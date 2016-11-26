package org.odddev.fantlab.core.layers.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.odddev.fantlab.core.layers.view.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kenrube
 * @since 30.08.16
 */

public class Presenter<V extends IView> {

    private List<V> views = new ArrayList<>();
    private Bundle arguments;
    private Long detachedTime = null;

    public void attachView(@NonNull V view) {
        views.add(view);
        detachedTime = null;
        onViewAttached(view);
    }

    public void detachView(@NonNull V view) {
        onViewDetached(view);
        detachedTime = System.currentTimeMillis();
        views.remove(view);
    }

    public boolean isAttached() {
        return views.size() > 0;
    }

    public List<V> getViews() {
        return views;
    }

    public Bundle getArguments() {
        return arguments;
    }

    protected void setArguments(Bundle args) {
        arguments = args;
    }

    protected void onCreate() {
    }

    protected void onViewAttached(@NonNull V view) {
    }

    protected void onViewDetached(@NonNull V view) {
    }

    protected void onDestroy() {
    }

    public Long getDetachedTime() {
        return detachedTime;
    }
}
