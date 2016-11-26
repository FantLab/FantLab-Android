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
 * @since 18.09.16
 */

public class RegFragment extends Fragment implements IRegView, IRegActions,
        DatePickerDialog.OnDateSetListener {

    private static final int PRESENTER_ID = RegFragment.class.getSimpleName().hashCode();

    private static final int MIN_AGE = 5;
    private static final int DEFAULT_AGE = 20;
    private static final int MAX_AGE = 100;
    private static final int JANUARY_FIRST_DAY = 1;
    private static final int DECEMBER_LAST_DAY = 31;
    private static final int DAY_LAST_HOUR = 23;
    private static final int HOUR_LAST_MINUTE = 59;
    private static final int MINUTE_LAST_SECOND = 59;

    private RegPresenter presenter;
    private RegFragmentBinding binding;
    private AuthRouter router;

    private RegValidator regValidator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = PresenterManager.getPresenter(PRESENTER_ID, RegPresenter::new);
        router = new AuthRouter((AuthActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RegFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.setHandler(this);
        regValidator = new RegValidator(getContext());
        binding.setRegValidator(regValidator);

        setDefaultBirthDate();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onStop() {
        presenter.detachView(this);
        super.onStop();
    }

    public void register() {
        if (regValidator.areFieldsValid()) {
            presenter.register(regValidator);
        }
    }

    private void setDefaultBirthDate() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        showBirthDate(day, month + 1, year - DEFAULT_AGE);
    }

    private void showBirthDate(int day, int month, int year) {
        binding.birthdate.setText(DateUtils.valuesToDateString(day, month, year));
    }

    @Override
    public void pickDate() {
        Calendar calendar = DateUtils.dateStringToCalendar(binding.birthdate.getText().toString());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), this, year, month, day);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        calendar.set(year - MAX_AGE, Calendar.JANUARY, JANUARY_FIRST_DAY);
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        calendar.set(
                year - MIN_AGE,
                Calendar.DECEMBER,
                DECEMBER_LAST_DAY,
                DAY_LAST_HOUR,
                HOUR_LAST_MINUTE,
                MINUTE_LAST_SECOND);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        regValidator.birthDay = dayOfMonth;
        regValidator.birthMonth = monthOfYear + 1;
        regValidator.birthYear = year;

        showBirthDate(dayOfMonth, monthOfYear + 1, year);
    }

    @Override
    public void showRegResult(boolean registered) {
        if (registered) {
            router.routeToHome(true);
        } else {
            showError(getString(R.string.error_reg));
        }
    }

    @Override
    public void showError(String error) {
        Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }
}
