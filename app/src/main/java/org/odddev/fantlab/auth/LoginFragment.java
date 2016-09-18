package org.odddev.fantlab.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.odddev.fantlab.core.layers.presenter.PresenterManager;
import org.odddev.fantlab.databinding.LoginFragmentBinding;

import timber.log.Timber;

/**
 * @author kenrube
 * @date 30.08.16
 */
// TODO: 23.08.16 refactor layout (styles etc)
public class LoginFragment extends Fragment implements ILoginView {

    private LoginPresenter mPresenter;
    private LoginFragmentBinding mBinding;

    private LoginParams mLoginParams;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = PresenterManager.getPresenter(this.hashCode(), LoginPresenter::new);
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
        mLoginParams = new LoginParams(getContext());
        mBinding.setLoginParams(mLoginParams);
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
        if (mLoginParams.isValid()) {
            //mPresenter.login(mLoginParams.login, mLoginParams.password);
            Snackbar.make(mBinding.getRoot(), "Very cool!", Snackbar.LENGTH_LONG).show();
        }
    }

    public void register() {
        Timber.d("Register");
    }

    public void entry() {
        Timber.d("Entry");
    }

    @Override
    public void showLoggedIn() {
        Timber.d("LoggedIn");
    }
}
