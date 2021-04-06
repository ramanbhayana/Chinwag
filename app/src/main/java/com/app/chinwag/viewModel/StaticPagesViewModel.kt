package com.app.chinwag.viewModel

import androidx.lifecycle.MutableLiveData
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.StaticPageResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.StaticPagesRepository
import com.google.gson.JsonElement
import io.reactivex.disposables.CompositeDisposable

class StaticPagesViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val staticPagesRepository: StaticPagesRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val updateTNCResponseLiveData = MutableLiveData<TAListResponse<JsonElement>>()
    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    val staticPageResponseLiveData = MutableLiveData<TAListResponse<StaticPageResponse>>()

    override fun onCreate() {
        checkForInternetConnection()
    }

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }


    fun callStaticPage(pageCode: String) {
        compositeDisposable.addAll(
            staticPagesRepository.getStaticPageData(pageCode)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        staticPageResponseLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    fun callUpdateTNCPrivacyPolicy(pageType: String) {
        compositeDisposable.addAll(
            staticPagesRepository.updateTNCPrivacyPolicy(pageType)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        updateTNCResponseLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }
}