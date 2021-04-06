package com.app.chinwag.viewModel

import androidx.lifecycle.MutableLiveData
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.application.AppineersApplication.Companion.sharedPreference
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.commonUtils.utility.getDeviceName
import com.app.chinwag.commonUtils.utility.getDeviceOSVersion
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.LoginResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.LoginWithPhoneNumberRepository
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import kotlin.collections.HashMap

class LoginWithPhoneNumberViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val loginWithPhoneNumberRepository: LoginWithPhoneNumberRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    var deviceToken : String = ""

    override fun onCreate() {
        checkForInternetConnection()
        deviceToken = sharedPreference.deviceToken ?: ""
    }

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    var phoneNumberLoginMutableLiveData = MutableLiveData<TAListResponse<LoginResponse>>()


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
            loginWithPhoneNumberRepository.callLoginWithPhoneNumber(map)
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
     * Save logged in user information in shared preference
     */
    fun saveUserDetails(loginResponse: LoginResponse?) {
        //Logger.setUserInfo(loginResponse?.email ?: "")
        sharedPreference.isSkip = false
        sharedPreference.userDetail = loginResponse
        sharedPreference.isLogin = true
        sharedPreference.authToken = loginResponse?.accessToken ?: ""
        sharedPreference.isAdRemoved = loginResponse?.purchaseStatus.equals("Yes")
        sharedPreference.logStatusUpdated =
            loginResponse?.logStatusUpdated?.toLowerCase(Locale.getDefault()) ?: "inactive"
    }
}