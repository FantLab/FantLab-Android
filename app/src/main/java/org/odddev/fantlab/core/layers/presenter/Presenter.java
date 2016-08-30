package org.odddev.fantlab.core.layers.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.odddev.fantlab.core.layers.view.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public class Presenter<V extends IView> {

    private List<V> mViews = new ArrayList<>();
    private Bundle mArguments;
    private Long mDetachedTime = null;

    public void attachView(@NonNull V view) {
        mViews.add(view);
        mDetachedTime = null;
        onViewAttached(view);
    }

    public void detachView(@NonNull V view) {
        onViewDetached(view);
        mDetachedTime = System.currentTimeMillis();
        mViews.remove(view);
    }

    public boolean isAttached() {
        return mViews.size() > 0;
    }

    public List<V> getViews() {
        return mViews;
    }

    public Bundle getArguments() {
        return mArguments;
    }

    protected void setArguments(Bundle args) {
        mArguments = args;
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
        return mDetachedTime;
    }
}
