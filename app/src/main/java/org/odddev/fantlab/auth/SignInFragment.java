package org.odddev.fantlab.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.odddev.fantlab.R;
import org.odddev.fantlab.core.layers.presenter.PresenterManager;
import org.odddev.fantlab.core.view.BaseBindingFragment;
import org.odddev.fantlab.databinding.SignInFragmentBinding;
import org.odddev.fantlab.profile.ProfileValidator;
import org.odddev.fantlab.profile.User;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */
// TODO: 23.08.16 refactor layout (styles etc)
public class SignInFragment extends BaseBindingFragment<SignInFragmentBinding>
        implements ISignInView {

    private SignInPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.sign_in_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = PresenterManager.getPresenter(this.hashCode(), SignInPresenter::new);
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

    @Override
    public void showValidator(ProfileValidator validator) {

    }

    @Override
    public void showError(int errorResource) {

    }

    @Override
    public void success() {

    }

    @Override
    public void showData(User data) {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showLoading() {

    }
}
