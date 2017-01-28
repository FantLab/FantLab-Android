package org.odddev.fantlab.award;

import com.arellomobile.mvp.MvpView;

import java.util.List;

/**
 * @author kenrube
 * @since 11.12.16
 */

public interface IAwardsView extends MvpView {

    void showAwards(List<Award> awards);

    void showError(String message);
}
