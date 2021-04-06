package com.app.chinwag.application

//import com.hb.logger.Logger

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDexApplication
import com.app.chinwag.api.network.InternetAvailabilityChecker
import com.app.chinwag.commonUtils.common.LoadingDialog
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.dagger.components.ApplicationComponent
import com.app.chinwag.dagger.components.DaggerApplicationComponent
import com.app.chinwag.dagger.modules.ApplicationModule
import com.app.chinwag.db.AppPrefrrences
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.scheduler.ExportLogService
import com.app.chinwag.view.authentication.login.loginwithemailsocial.LoginWithEmailSocialActivity
import com.app.chinwag.viewModel.MainActivityViewModel
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.hb.logger.Logger
import java.io.File
import java.lang.ref.WeakReference


class AppineersApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {

    lateinit var applicationComponent: ApplicationComponent
    var isLogStatusUpdated = MutableLiveData<Boolean>()
    val isProfileUpdated = MutableLiveData<Boolean>()
    val isAdRemoved = MutableLiveData<Boolean>()
    var weakActivity: WeakReference<BaseActivity<MainActivityViewModel>>? = null

    companion object {
        lateinit var sharedPreference: AppPrefrrences
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreference = AppPrefrrences(this)
        sharedPreference.ratingInitTime = System.currentTimeMillis()
        InternetAvailabilityChecker.init(this)
        registerActivityLifecycleCallbacks(this)
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)

        MobileAds.initialize(this)

        Logger.initializeSession(this, object : Logger.UploadFileListener {
            override fun onDataReceived(file: File) {
                //Call Export Log Service
                Toast.makeText(this@AppineersApplication, "Sending logs.. Check notification..", Toast.LENGTH_SHORT).show()
                val exportService = Intent(this@AppineersApplication, ExportLogService::class.java)
                exportService.putExtra(ExportLogService.KEY_FILE_URI, file)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(exportService)
                } else {
                    startService(exportService)
                }
            }

        })

        Logger.setExceptionCallbackListener(object : Logger.ExceptionCallback {
            @SuppressLint("LogNotTimber")
            override fun onExceptionThrown(exception: Throwable) {
                Log.e("Crash ", "happen")
                Toast.makeText(this@AppineersApplication, "Crash Happen", Toast.LENGTH_SHORT).show()
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        })
    }


    override fun onActivityPaused(activity: Activity) {
        LoadingDialog.unbind()
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        LoadingDialog.bindWith(activity)
    }


    fun setCurrentActivity(activity: BaseActivity<MainActivityViewModel>) {
        weakActivity = WeakReference(activity)
    }

    fun getCurrentActivity(): BaseActivity<MainActivityViewModel>? {
        return weakActivity?.get()
    }

    fun setApplicationLoginType(type: String) {
        sharedPreference.appLoginType = type
    }

    fun getApplicationLoginType(): String {
        return sharedPreference.appLoginType ?: IConstants.LOGIN_TYPE_EMAIL
    }

    /**
     * This will provide application login activity
     * @return Class<*>
     */
    fun getLoginActivity(): Class<*> {
        //return LoginWithEmailActivity::class.java
        //return LoginWithPhoneNumberActivity::class.java
        return LoginWithEmailSocialActivity::class.java
        //return LoginWithPhoneNumberSocialActivity::class.java
    }
}