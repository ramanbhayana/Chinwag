package com.app.chinwag.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.application.AppineersApplication.Companion.sharedPreference
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.commonUtils.utility.IConstants.Companion.COUNT_DOWN_TIMER
import com.app.chinwag.commonUtils.utility.extension.timeToMinuteSecond
import com.app.chinwag.commonUtils.utility.extension.toFieldRequestBodyMap
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.request.SignUpRequestModel
import com.app.chinwag.dataclasses.response.LoginResponse
import com.app.chinwag.dataclasses.response.OTPResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.OTPSignUpRepository
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class OTPSignUpViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val otpSignUpRepository: OTPSignUpRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {
        checkForInternetConnection()
    }

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    var otpLiveData = MutableLiveData<TAListResponse<OTPResponse>>()
    var signUpLiveData = MutableLiveData<TAListResponse<LoginResponse>>()
    val signUpRequestModel = SignUpRequestModel()
    private val timeLiveData = MutableLiveData<String>()
    private var countDownTimer: CountDownTimer? = null  // Count down timer for 60 sec to enable retry button
    private val enableRetryLiveData = MutableLiveData<Boolean>()


    /**
     * Get timer live data to update timer value in view
     * @return MutableLiveData<String>
     */
    fun getTimerValue(): MutableLiveData<String> {
        return timeLiveData
    }

    fun getEnableRetrySetting(): MutableLiveData<Boolean> {
        return enableRetryLiveData
    }

    /**
     * Start count down timer from 60 second
     */
    fun startTimer() {
        countDownTimer = getCountDownTimer()
        countDownTimer?.start()
    }

    /**
     * Cancel count  down timer
     */
    fun cancelTimer() {
        countDownTimer?.cancel()
    }

    /**
     * Retrun CountDowmTimer object
     * @return CountDownTimer
     */
    private fun getCountDownTimer(): CountDownTimer {
        return object : CountDownTimer(COUNT_DOWN_TIMER, 1000) {
            override fun onFinish() {
                timeLiveData.value = ""
                enableRetryLiveData.value = true
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLiveData.value = String.format("Resend OTP in 60 seconds", millisUntilFinished.timeToMinuteSecond())
                enableRetryLiveData.value = false
            }
        }
    }


    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }

    fun callSignUpWithSocial() {
        compositeDisposable.addAll(
            otpSignUpRepository.callSignUpWithSocial(signUpRequestModel.toFieldRequestBodyMap())
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        signUpLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    fun callSignUpWithPhone() {
        compositeDisposable.addAll(
            otpSignUpRepository.callSignUpWithPhone(signUpRequestModel.toFieldRequestBodyMap())
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        signUpLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    /**
     * Api call for send OTP to user phone number
     * @param mobileNumber String User's phone number on which otp will received
     */

    fun callResendOtp(mobileNumber: String) {
        val map = HashMap<String, String>()
        map["type"] = "phone" // phone/email
        map["email"] = ""
        map["mobile_number"] = mobileNumber
        map["user_name"] = ""

        compositeDisposable.addAll(
            otpSignUpRepository.callCheckUniqueUser(map = map)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        otpLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    /**
     * Save logged in user information in shared preference
     */
    fun saveUserDetails(loginResponse: LoginResponse?) {
        sharedPreference.isSkip = false
        sharedPreference.userDetail = loginResponse
        sharedPreference.isLogin = true
        sharedPreference.authToken = loginResponse?.accessToken ?: ""
        sharedPreference.isAdRemoved = loginResponse?.purchaseStatus.equals("Yes")
        sharedPreference.logStatusUpdated = loginResponse?.logStatusUpdated?.toLowerCase(Locale.getDefault()) ?: "inactive"
    }
}