package com.app.chinwag.viewModel

import androidx.lifecycle.MutableLiveData
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.forgotpasswordwithphone.ResetWithPhone
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.ForgotPasswordPhoneRepository
import io.reactivex.disposables.CompositeDisposable

class ForgotPasswordPhoneViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        private val forgotPasswordPhoneRepository: ForgotPasswordPhoneRepository
)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    val forgotPasswordWithPhoneLiveData = MutableLiveData<TAListResponse<ResetWithPhone>>()

    override fun onCreate() {
        checkForInternetConnection()
    }

    fun getForgotPasswordWithPhone(mobileNumber: String) {
        compositeDisposable.addAll(
                forgotPasswordPhoneRepository.getForgotPasswordPhoneResponse(mobileNumber)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                                { response ->
                                    showDialog.postValue(false)
                                    forgotPasswordWithPhoneLiveData.postValue(response)
                                },
                                { error ->
                                    showDialog.postValue(false)
                                    messageString.postValue(Resource.error(error.message))
                                }
                        )
        )
    }

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }
}