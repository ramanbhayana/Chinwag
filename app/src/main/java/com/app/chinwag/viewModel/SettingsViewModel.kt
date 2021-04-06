package com.app.chinwag.viewModel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.SkuDetails
import com.app.chinwag.BuildConfig
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.application.AppineersApplication.Companion.sharedPreference
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.mvvm.AppConfig
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.SettingsRepository
import com.app.chinwag.mvvm.SettingViewConfig
import com.app.chinwag.repository.inappbilling.BillingRepository
import com.google.gson.JsonElement
import io.reactivex.disposables.CompositeDisposable

class SettingsViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val billingRepository: BillingRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val logoutLiveData = MutableLiveData<TAListResponse<JsonElement>>()
    val deleteAccountLiveData = MutableLiveData<TAListResponse<JsonElement>>()
    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    var goAdFreeLiveData = MutableLiveData<TAListResponse<JsonElement>>()
    var addFreeSKU = MutableLiveData<SkuDetails>()
    var orderReceiptJson = MutableLiveData<String>()
    var settingConfig: SettingViewConfig


    override fun onCreate() {
        checkForInternetConnection()
    }

    init {
        billingRepository.startDataSourceConnections()
        addFreeSKU = billingRepository.addFreeSKU
        orderReceiptJson = billingRepository.orderReceiptJson
        settingConfig = setUpSettingConfig()
    }

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }

    fun callLogout() {
        compositeDisposable.addAll(
            settingsRepository.callLogout()
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        logoutLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    fun callDeleteAccount() {
        compositeDisposable.addAll(
            settingsRepository.callDeleteAccount()
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        deleteAccountLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }


    private fun setUpSettingConfig(): SettingViewConfig {
        val user = sharedPreference.userDetail
        return SettingViewConfig().apply {
            showNotification = !sharedPreference.isSkip
            showRemoveAdd =
                ((AppConfig.BANNER_AD || AppConfig.INTERSTITIAL_AD) && !sharedPreference.isAdRemoved && !sharedPreference.isSkip)
            showEditProfile = !sharedPreference.isSkip
            showChangePassword = !(user?.isSocialLogin() == true || sharedPreference.isSkip)
            showChangePhone = ((AppineersApplication()).getApplicationLoginType()
                .equals(IConstants.LOGIN_TYPE_PHONE, true) ||
                    (AppineersApplication()).getApplicationLoginType().equals(
                        IConstants.LOGIN_TYPE_PHONE_SOCIAL,
                        true
                    )) && !sharedPreference.isSkip
            showDeleteAccount = !sharedPreference.isSkip
            showSendFeedback = !sharedPreference.isSkip
            showLogOut = !sharedPreference.isSkip
            appVersion = "Version: " + BuildConfig.VERSION_NAME
        }
    }

    fun makePurchase(activity: Activity?, skuDetails: SkuDetails?) {
        if (activity != null && skuDetails != null) {
            billingRepository.launchBillingFlow(activity, skuDetails = skuDetails)
        }
    }


    fun callGoAdFree(receiptData: String) {
        val map = HashMap<String, String>()
        map["one_time_transaction_data"] = receiptData
        compositeDisposable.addAll(
            settingsRepository.callGoAdFree(map)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        goAdFreeLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    /**
     * Clear all user saved data
     */
    fun performLogout() {
        sharedPreference.userDetail = null
        sharedPreference.isLogin = false
        sharedPreference.authToken = ""
    }

}