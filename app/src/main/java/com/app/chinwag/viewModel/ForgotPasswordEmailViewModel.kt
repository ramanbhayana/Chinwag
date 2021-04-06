package com.app.chinwag.viewModel

import androidx.lifecycle.MutableLiveData
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.ForgotPasswordEmailRepository
import com.google.gson.JsonElement
import io.reactivex.disposables.CompositeDisposable

class ForgotPasswordEmailViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        private val forgotPasswordEmailRepository: ForgotPasswordEmailRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {
        checkForInternetConnection()
    }

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    val forgotPasswordWithEmailLiveData = MutableLiveData<TAListResponse<JsonElement>>()

    fun getForgotPasswordWithEmail(email: String) {
        compositeDisposable.addAll(
                forgotPasswordEmailRepository.getForgotPasswordEmailResponse(email)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                                { response ->
                                    showDialog.postValue(false)
                                    forgotPasswordWithEmailLiveData.postValue(response)
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