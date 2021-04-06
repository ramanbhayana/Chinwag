package com.app.chinwag.dagger.components

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.dagger.ApplicationContext
import com.app.chinwag.dagger.modules.ApplicationModule
import com.app.chinwag.db.WeatherDataRepository
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: AppineersApplication)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    fun getNetworkService(): NetworkService

    fun getNetworkHelper(): NetworkHelper

    fun getSchedulerProvider(): SchedulerProvider

    fun getCompositeDisposable(): CompositeDisposable

    fun getAlertDialogBuilder(): AlertDialog.Builder

    fun getLayoutInflater(): LayoutInflater

    fun getweatherDataRepository(): WeatherDataRepository?
}