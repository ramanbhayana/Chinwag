package com.app.chinwag.viewModel

import androidx.lifecycle.MutableLiveData
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.commonUtils.utility.getDeviceName
import com.app.chinwag.commonUtils.utility.getDeviceOSVersion
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.LoginResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.LoginWithPhoneNumberSocialRepository
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import kotlin.collections.HashMap

class LoginWithPhoneNumberSocialViewModel (
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val loginWithPhoneNumberSocialRepository: LoginWithPhoneNumberSocialRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    var deviceToken : String = ""

    override fun onCreate() {
        checkForInternetConnection()
        deviceToken = AppineersApplication.sharedPreference.deviceToken ?: ""
    }

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    var phoneNumberLoginMutableLiveData = MutableLiveData<TAListResponse<LoginResponse>>()
    var loginSocialMutableLiveData = MutableLiveData<TAListResponse<LoginResponse>>()

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }

    /**
     * Api call for login using phone number
     */
    fun callLoginWithPhoneNumber(
        phoneNumber: String,
        password: String
    ) {
        val map = HashMap<String, String>()
        map["mobile_number"] = phoneNumber
        map["password"] = password
        map["device_type"] = IConstants.DEVICE_TYPE_ANDROID
        map["device_model"] = getDeviceName()
        map["device_os"] = getDeviceOSVersion()
        map["device_token"] = deviceToken
        compositeDisposable.addAll(
            loginWithPhoneNumberSocialRepository.callLoginWithPhoneNumber(map)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        phoneNumberLoginMutableLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    /**
     * Api call for login using social account
     */
    fun callLoginWithSocial(socialType: String, socialId: String) {
        val map = HashMap<String, String>()
        map["social_login_type"] = socialType
        map["social_login_id"] = socialId
        map["device_type"] = IConstants.DEVICE_TYPE_ANDROID
        map["device_model"] = getDeviceName()
        map["device_os"] = getDeviceOSVersion()
        map["device_token"] = deviceToken
        compositeDisposable.addAll(
            loginWithPhoneNumberSocialRepository.callLoginWithPhoneNumberSocial(map)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        loginSocialMutableLiveData.postValue(response)
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
        //Logger.setUserInfo(loginResponse?.email ?: "")
        AppineersApplication.sharedPreference.isSkip = false
        AppineersApplication.sharedPreference.userDetail = loginResponse
        AppineersApplication.sharedPreference.isLogin = true
        AppineersApplication.sharedPreference.authToken = loginResponse?.accessToken ?: ""
        AppineersApplication.sharedPreference.isAdRemoved = loginResponse?.purchaseStatus.equals("Yes")
        AppineersApplication.sharedPreference.logStatusUpdated =
            loginResponse?.logStatusUpdated?.toLowerCase(Locale.getDefault()) ?: "inactive"
    }
}