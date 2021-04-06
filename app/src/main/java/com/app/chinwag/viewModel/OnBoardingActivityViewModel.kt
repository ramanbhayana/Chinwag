package com.app.chinwag.viewModel

import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.mvvm.BaseViewModel
import io.reactivex.disposables.CompositeDisposable

class OnBoardingActivityViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper){

    override fun onCreate() {

    }

}