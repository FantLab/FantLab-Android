package org.odddev.fantlab.core.layers.presenter;

import android.os.Bundle;

import java.util.HashMap;

/**
 * @author kenrube
 * @since 30.08.16
 */

public class PresenterManager {

    private static final int MAX_PRESENTERS = 10;

    private static final HashMap<Integer, Presenter> PRESENTERS = new HashMap<>();

    public interface PresenterFactory<T extends Presenter> {
        T createPresenter();
    }

    protected PresenterManager() {
    }

    public static <T extends Presenter> T getPresenter(int presenterId, PresenterFactory<T> factory) {
        return getPresenter(presenterId, null, factory);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Presenter> T getPresenter(int presenterId, Bundle arguments,
                                                       PresenterFactory<T> factory) {
        Presenter presenter = PRESENTERS.get(presenterId);
        if (presenter == null) {
            presenter = factory.createPresenter();
            if (presenter == null) {
                throw new RuntimeException("Presenter can't be null");
            }
            presenter.setArguments(arguments);
            presenter.onCreate();
            if (PRESENTERS.size() > MAX_PRESENTERS) {
                removeObsoletePresenters();
            }
            PRESENTERS.put(presenterId, presenter);
        }
        return (T) presenter;
    }

    public static void removeObsoletePresenters() {
        while (PRESENTERS.size() > MAX_PRESENTERS) {
            long oldestDetachTime = Long.MAX_VALUE;
            Integer idToRemove = null;
            for (int presenterId : PRESENTERS.keySet()) {
                Presenter presenter = PRESENTERS.get(presenterId);
                if (!presenter.isAttached() && presenter.getDetachedTime() < oldestDetachTime) {
                    idToRemove = presenterId;
                }
            }
            if (idToRemove != null) {
                PRESENTERS.get(idToRemove).onDestroy();
                PRESENTERS.remove(idToRemove);
            }
        }
    }
}
