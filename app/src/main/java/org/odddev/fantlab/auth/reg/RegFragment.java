package org.odddev.fantlab.auth.reg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.odddev.fantlab.databinding.RegFragmentBinding;

/**
 * @author kenrube
 * @date 18.09.16
 */

public class RegFragment extends Fragment implements IRegView {

    private RegFragmentBinding mBinding;

    private RegParams mRegParams;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void register() {
        if (mRegParams.isValid()) {
            //mPresenter.login(mLoginParams.login, mLoginParams.password);
            Snackbar.make(mBinding.getRoot(), "Very cool!", Snackbar.LENGTH_LONG).show();
        }
    }
}
