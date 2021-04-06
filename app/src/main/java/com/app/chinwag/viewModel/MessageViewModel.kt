package com.app.chinwag.viewModel

import androidx.lifecycle.MutableLiveData
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.MessageRepository
import io.reactivex.disposables.CompositeDisposable

class MessageViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val messageRepository: MessageRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()

    override fun onCreate() {
        checkForInternetConnection()
    }

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }
}