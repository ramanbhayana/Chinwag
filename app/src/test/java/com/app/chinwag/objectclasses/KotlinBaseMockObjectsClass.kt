package com.app.chinwag.objectclasses

import com.app.chinwag.utils.TestSchedulerProvider
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.db.WeatherDataRepository
import com.app.chinwag.utils.mock
import io.reactivex.disposables.CompositeDisposable

open class KotlinBaseMockObjectsClass {
    val testSchedulerProvider = TestSchedulerProvider()
    val mockCompositeDisposable = mock<CompositeDisposable>()
    val mockNetworkService = mock<NetworkService>()
    val mockApplication = mock<AppineersApplication>()
    val mockNetworkHelper = mock<NetworkHelper>()
    val mockDataRepository = mock<WeatherDataRepository>()
}