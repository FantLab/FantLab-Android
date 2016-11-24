package org.odddev.fantlab.auth.login;

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
import org.odddev.fantlab.databinding.LoginFragmentBinding;

/**
 * @author kenrube
 * @date 30.08.16
 */
public class LoginFragment extends Fragment implements ILoginView {

    private static final int PRESENTER_ID = LoginPresenter.class.getSimpleName().hashCode();

    private LoginPresenter mPresenter;
    private LoginFragmentBinding mBinding;
    private AuthRouter mRouter;

    private LoginViewModel mLoginViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = PresenterManager.getPresenter(PRESENTER_ID, LoginPresenter::new);
        mRouter = new AuthRouter((AuthActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LoginFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBinding.setActionsHandler(this);
        mLoginViewModel = new LoginViewModel(getContext());
        mBinding.setLoginViewModel(mLoginViewModel);
        mBinding.setForgotPass(false);
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

    public void login() {
        if (mLoginViewModel.isValid()) {
            mPresenter.login(mLoginViewModel);
            mBinding.setForgotPass(false);
        }
    }

    public void register() {
        mRouter.routeToReg();
    }

    public void forgotPass() {
        // todo 3.1, 3.2, 3.3
    }

    public void entry() {
        mRouter.routeToHome(false);
    }

    @Override
    public void showLoginResult(boolean loggedIn) {
        if (loggedIn) {
            mRouter.routeToHome(true);
        } else {
            mBinding.setForgotPass(true);
            showError(getString(R.string.error_login));
        }
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }
    // todo 1.10
}
