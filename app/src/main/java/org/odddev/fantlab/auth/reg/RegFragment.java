package org.odddev.fantlab.auth.reg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthActivity;
import org.odddev.fantlab.auth.AuthRouter;
import org.odddev.fantlab.core.layers.presenter.PresenterManager;
import org.odddev.fantlab.databinding.RegFragmentBinding;

/**
 * @author kenrube
 * @date 18.09.16
 */

public class RegFragment extends Fragment implements IRegView {

    private static final int PRESENTER_ID = RegFragment.class.getSimpleName().hashCode();

    private RegPresenter mPresenter;
    private RegFragmentBinding mBinding;
    private AuthRouter mRouter;

    private RegParams mRegParams;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = PresenterManager.getPresenter(PRESENTER_ID, RegPresenter::new);
        mRouter = new AuthRouter((AuthActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = RegFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBinding.setActionsHandler(this);
        mRegParams = new RegParams(getContext());
        mBinding.setRegParams(mRegParams);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.attachView(this);
    }

    @Override
    public void onStop() {
        mPresenter.detachView(this);
        super.onStop();
    }

    public void register() {
        if (mRegParams.isValid()) {
            mPresenter.register(mRegParams.username, mRegParams.password, mRegParams.email);
        }
    }

    @Override
    public void showRegResult(boolean registered) {
        if (registered) {
            mRouter.routeToHome(true);
        } else {
            showError(getString(R.string.error_reg));
        }
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }
}
