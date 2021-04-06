package com.app.chinwag.viewModel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.app.chinwag.R
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.common.timeToMinuteSecond
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.dataclasses.createValidationResult
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.forgotpasswordwithphone.ResetWithPhone
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.mvvm.utility.OTP_EMPTY
import com.app.chinwag.mvvm.utility.OTP_INVALID
import com.app.chinwag.repository.OTPForgotPasswordRepository
import io.reactivex.disposables.CompositeDisposable

class OTPForgotPasswordViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val otpForgotPasswordRepository: OTPForgotPasswordRepository,
    val app: Application
): BaseViewModel(schedulerProvider,compositeDisposable,networkHelper) {

    val timeLiveData = MutableLiveData<String>()
    val enableRetryLiveData = MutableLiveData<Boolean>()
    val resendOtpLiveData = MutableLiveData<TAListResponse<ResetWithPhone>>()
    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    var countDownTimer: CountDownTimer? = null

    override fun onCreate() {
        checkForInternetConnection()
    }

    private fun checkForInternetConnection() = when {
        checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
        else -> checkForInternetConnectionLiveData.postValue(false)
    }

    fun getTimerValue(): MutableLiveData<String> {
        return timeLiveData
    }

    fun getEnableRetrySettings(): MutableLiveData<Boolean> {
        return enableRetryLiveData
    }

    fun startTimer() {
        countDownTimer = getCountTimer()
        countDownTimer?.start()
    }

    fun cancelTimer(markFinish: Boolean = false) {
        countDownTimer?.cancel()
        if (markFinish)
            countDownTimer?.onFinish()

    }

    fun isValid( otp: String , sendOtp: String): Boolean {
      return  when {
          otp.isEmpty() -> {
              validationObserver.value = createValidationResult(failType = OTP_EMPTY)
              false
          }
          otp != sendOtp -> {
               validationObserver.value = createValidationResult(failType = OTP_INVALID)
               return true
          }
          else-> true
      }
    }
    /**
     * count down timer function
     */
    private fun getCountTimer(): CountDownTimer? {
        return object : CountDownTimer(30000L, 1000) {
            override fun onFinish() {
                timeLiveData.value = ""
                enableRetryLiveData.value = true
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLiveData.value = String.format(
                    app.getString(
                        R.string.lbl_resend_otp_in_minute,
                        millisUntilFinished.timeToMinuteSecond()
                    )
                )
            }

        }

    }

    fun getOTPForgotPasswordWithPhone(mobileNumber: String) {
        compositeDisposable.addAll(
            otpForgotPasswordRepository.getOTPForgotPasswordPhoneResponse(mobileNumber)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        resendOtpLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))

                    }
                )
        )

    }

}