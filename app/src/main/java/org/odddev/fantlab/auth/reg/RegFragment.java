package org.odddev.fantlab.auth.reg;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthActivity;
import org.odddev.fantlab.auth.AuthRouter;
import org.odddev.fantlab.core.layers.presenter.PresenterManager;
import org.odddev.fantlab.core.utils.DateUtils;
import org.odddev.fantlab.databinding.RegFragmentBinding;

import java.util.Calendar;

/**
 * @author kenrube
 * @date 18.09.16
 */

public class RegFragment extends Fragment implements IRegView, DatePickerDialog.OnDateSetListener {

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

    public void pickDate() {
/*
        Calendar curOrderDate = DateUtils.stringToCalendar(mBinding.getOrder().getPackageInfo().getExecDate(), DateUtils.DEFAULT_DATE_TIME_FORMAT);

        int year = curOrderDate.get(Calendar.YEAR);
        int month = curOrderDate.get(Calendar.MONTH);
        int day = curOrderDate.get(Calendar.DAY_OF_MONTH);

        Calendar minOrderDate = OrderUtils.getOrderMinDate();

        minOrderDate.set(Calendar.HOUR_OF_DAY, 0);
        minOrderDate.set(Calendar.MINUTE, 0);
        minOrderDate.set(Calendar.SECOND, 0);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(OrderUtils.getOrderMaxDate().getTimeInMillis());
        dialog.getDatePicker().setMinDate(minOrderDate.getTimeInMillis());
        dialog.show();
*/
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
/*
        Calendar curOrderDate = DateUtils.stringToCalendar(binding.getOrder().getPackageInfo().getExecDate(), DateUtils.DEFAULT_DATE_TIME_FORMAT);
        curOrderDate.set(year, monthOfYear, dayOfMonth);
*/
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
