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
import com.app.chinwag.repository.LoginWithEmailSocialRepository
import com.google.gson.JsonElement
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import kotlin.collections.HashMap

class LoginWithEmailSocialViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val loginWithEmailSocialRepository: LoginWithEmailSocialRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    var deviceToken : String = ""

    override fun onCreate() {
        checkForInternetConnection()
        deviceToken = AppineersApplication.sharedPreference.deviceToken ?: ""
    }

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    var loginSocialMutableLiveData = MutableLiveData<TAListResponse<LoginResponse>>()
    var loginEmailMutableLiveData = MutableLiveData<TAListResponse<LoginResponse>>()
    var resendLinkMutableLiveData = MutableLiveData<TAListResponse<JsonElement>>()


    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }

    /**
     * Api call for login using phone number
     */
    fun callLoginWithEmailSocial(socialType: String, socialId: String) {
        val map = HashMap<String, String>()
        map["social_login_type"] = socialType
        map["social_login_id"] = socialId
        map["device_type"] = IConstants.DEVICE_TYPE_ANDROID
        map["device_model"] = getDeviceName()
        map["device_os"] = getDeviceOSVersion()
        map["device_token"] = deviceToken
        compositeDisposable.addAll(
            loginWithEmailSocialRepository.callLoginWithEmailSocial(map)
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
     * Api call for login using phone number
     */
    fun callLoginWithEmail(email: String, password: String) {
        val map = HashMap<String, String>()
        map["email"] = email
        map["password"] = password
        map["device_type"] = IConstants.DEVICE_TYPE_ANDROID
        map["device_model"] = getDeviceName()
        map["device_os"] = getDeviceOSVersion()
        map["device_token"] = deviceToken
        compositeDisposable.addAll(
            loginWithEmailSocialRepository.callLoginWithEmail(map)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        loginEmailMutableLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    /**
     * Api call for send verification link
     */

    fun callResendLink(email: String = "") {
        val map = HashMap<String, String>()
        map["email"] = email
        compositeDisposable.addAll(
            loginWithEmailSocialRepository.callResendLink(map)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        resendLinkMutableLiveData.postValue(response)
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
        AppineersApplication.sharedPreference.isSkip = false
        AppineersApplication.sharedPreference.userDetail = loginResponse
        AppineersApplication.sharedPreference.isLogin = true
        AppineersApplication.sharedPreference.authToken = loginResponse?.accessToken ?: ""
        AppineersApplication.sharedPreference.isAdRemoved =
            loginResponse?.purchaseStatus.equals("Yes")
        AppineersApplication.sharedPreference.logStatusUpdated =
            loginResponse?.logStatusUpdated?.toLowerCase(Locale.getDefault()) ?: "inactive"
    }
}