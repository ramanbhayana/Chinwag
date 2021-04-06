package com.app.chinwag.viewModel

import androidx.lifecycle.MutableLiveData
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.ResetPasswordRepository
import com.google.gson.JsonElement
import io.reactivex.disposables.CompositeDisposable

class ResetPasswordViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val resetPasswordRepository: ResetPasswordRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    val resetPasswordLiveData = MutableLiveData<TAListResponse<JsonElement>>()

    override fun onCreate() {
        checkForInternetConnection()
    }

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }

    fun callResetPassword(newPassword: String, mobileNumber: String, resetKey: String) {
        compositeDisposable.addAll(
            resetPasswordRepository.callResetPassword(newPassword, mobileNumber, resetKey)
                .subscribeOn(schedulerProvider.io())
                    .subscribe({ response ->
                        showDialog.postValue(false)
                        resetPasswordLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    })
        )
    }
}