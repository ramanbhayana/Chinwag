package com.app.chinwag.view.authentication.forgotpassword.phone

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.chinwag.R
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.common.isValidMobileNumber
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.databinding.ActivityForgotPasswordWithPhoneBinding
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.view.authentication.otp.otpforgotpassword.OTPForgotPasswordActivity
import com.app.chinwag.viewModel.ForgotPasswordPhoneViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber

class ForgotPasswordWithPhoneActivity : BaseActivity<ForgotPasswordPhoneViewModel>() {
    private var dataBinding: ActivityForgotPasswordWithPhoneBinding? = null
    private var mobileNumber: String? = ""

    override fun setDataBindingLayout() {
        dataBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_password_with_phone)
        dataBinding?.lifecycleOwner = this
        dataBinding?.viewModel = viewModel
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setFireBaseAnalyticsData("id-forgotPasswordWithPhoneScreen","view_forgotPasswordWithPhoneScreen","view_forgotPasswordWithPhoneScreen")
        dataBinding?.let {
            with(it, {
                tietMobileNumber.doAfterTextChanged { text ->
                    mobileNumber = text?.toString()!!.trim()
                }

                ibtnBack.setOnClickListener {
                    logger.dumpCustomEvent(IConstants.EVENT_CLICK,"Back Button Click")
                    finish()
                }

                mbtnNext.setOnClickListener {
                    logger.dumpCustomEvent(IConstants.EVENT_CLICK,"Next Button Click")
                    validateAndSendRequest(tietMobileNumber)
                }

                tietMobileNumber.setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            validateAndSendRequest(tietMobileNumber)
                            true
                        }
                        else -> false
                    }
                }
            })
        }
    }

    //Method to validate data and call api request method
    private fun validateAndSendRequest(tietMobileNumber: TextInputEditText) {
        val validation = mobileNumber!!.isValidMobileNumber()
        when {
            validation -> {
                tietMobileNumber.error = null
                getForgotPasswordDetailsWithPhone(mobileNumber!!)
            }
            else -> {
                tietMobileNumber.error = getString(R.string.valid_mobile_number)
                viewModel.messageString.postValue(Resource.error(getString(R.string.valid_mobile_number)))
            }
        }
    }

    //Method to get forgot password response with phone from server
    private fun getForgotPasswordDetailsWithPhone(mobileNumber: String) {
        hideKeyboard()
        when {
            checkInternet() -> {
                CoroutineScope(IO).launch {
                    viewModel.showDialog.postValue(true)
                    viewModel.getForgotPasswordWithPhone(mobileNumber)
                }
            }
        }
    }

    @SuppressLint("observers")
    override fun setupObservers() {
        super.setupObservers()

        viewModel.forgotPasswordWithPhoneLiveData.observe(this, Observer { response ->

            viewModel.showDialog.postValue(false)
            if (response.settings?.isSuccess == true) {
                if (!response.data.isNullOrEmpty()) {
                       showMessage( response.data!![0].otp)
                       showMessage( response.data!![0].resetKey)

                    startActivity(
                        OTPForgotPasswordActivity.getStartIntent(
                            context = this@ForgotPasswordWithPhoneActivity,
                            phoneNumber = mobileNumber!!,
                            otp = response.data!![0].otp,
                            resetKey = response.data!![0].resetKey
                        )
                    )
                } else {
                    Timber.d(response.settings?.message)
                }
            } else {
                   showMessage( response.settings!!.message)
                Timber.d(response.settings?.message)
            }
        })
    }
}