package org.odddev.fantlab.auth.reg

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import org.odddev.fantlab.R
import org.odddev.fantlab.auth.AuthActivity
import org.odddev.fantlab.auth.AuthRouter
import org.odddev.fantlab.core.utils.DateUtils
import org.odddev.fantlab.databinding.RegFragmentBinding
import java.util.*

/**
 * @author kenrube
 * *
 * @since 18.09.16
 */

class RegFragment : MvpAppCompatFragment(), IRegView, IRegActions, DatePickerDialog.OnDateSetListener {

	@InjectPresenter
	lateinit var presenter: RegPresenter

	private lateinit var binding: RegFragmentBinding
	private lateinit var router: AuthRouter

	private lateinit var regValidator: RegValidator

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		router = AuthRouter(activity as AuthActivity, R.id.container)
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = RegFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		binding.handler = this
		regValidator = RegValidator(context)
		binding.regValidator = regValidator

		setDefaultBirthDate()
	}

	override fun onStart() {
		super.onStart()
		presenter.attachView(this)
	}

	override fun onStop() {
		presenter.detachView(this)
		super.onStop()
	}

	fun register() {
		if (regValidator.areFieldsValid()) {
			presenter.register(regValidator)
		}
	}

	private fun setDefaultBirthDate() {
		val calendar = Calendar.getInstance()

		val year = calendar.get(Calendar.YEAR)
		val month = calendar.get(Calendar.MONTH)
		val day = calendar.get(Calendar.DAY_OF_MONTH)

		showBirthDate(day, month + 1, year - DEFAULT_AGE)
	}

	private fun showBirthDate(day: Int, month: Int, year: Int) {
		regValidator.fields.put(RegValidator.BIRTH_DATE,
				DateUtils.valuesToDateString(day, month, year))
	}

	override fun pickDate() {
		var calendar = DateUtils.dateStringToCalendar(
				regValidator.fields[RegValidator.BIRTH_DATE] as String)

		var year = calendar.get(Calendar.YEAR)
		val month = calendar.get(Calendar.MONTH)
		val day = calendar.get(Calendar.DAY_OF_MONTH)

		val dialog = DatePickerDialog(context, this, year, month, day)

		calendar = Calendar.getInstance()

		year = calendar.get(Calendar.YEAR)

		calendar.set(year - MAX_AGE, Calendar.JANUARY, JANUARY_FIRST_DAY)
		dialog.datePicker.minDate = calendar.timeInMillis

		calendar.set(
				year - MIN_AGE,
				Calendar.DECEMBER,
				DECEMBER_LAST_DAY,
				DAY_LAST_HOUR,
				HOUR_LAST_MINUTE,
				MINUTE_LAST_SECOND)
		dialog.datePicker.maxDate = calendar.timeInMillis

		dialog.show()
	}

	override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
		regValidator.birthDay = dayOfMonth
		regValidator.birthMonth = monthOfYear + 1
		regValidator.birthYear = year

		showBirthDate(dayOfMonth, monthOfYear + 1, year)
	}

	override fun chooseLocation() {
		binding.locationString.visibility = View.GONE
	}

	override fun enterLocationManually() {
		binding.locationString.visibility = View.VISIBLE
		binding.locationString.requestFocus()
	}

	override fun showRegResult(registered: Boolean) {
		if (registered) {
			router.routeToHome(true)
		} else {
			showError(getString(R.string.register_error))
		}
	}

	override fun showError(error: String) {
		Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
	}

	companion object {

		private val MIN_AGE = 5
		private val DEFAULT_AGE = 20
		private val MAX_AGE = 100
		private val JANUARY_FIRST_DAY = 1
		private val DECEMBER_LAST_DAY = 31
		private val DAY_LAST_HOUR = 23
		private val HOUR_LAST_MINUTE = 59
		private val MINUTE_LAST_SECOND = 59
	}
}
