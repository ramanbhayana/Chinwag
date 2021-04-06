package com.app.chinwag.mvvm

import android.annotation.SuppressLint
import android.app.Activity
import android.app.job.JobScheduler
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.commonUtils.common.LoadingDialog
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.dagger.components.DaggerActivityComponent
import com.app.chinwag.dagger.modules.ActivityModule
import com.app.chinwag.dataclasses.LocationAddressModel
import com.app.chinwag.view.HomeActivity
import com.google.android.libraries.places.api.model.AddressComponents
import com.app.chinwag.R
import com.app.chinwag.commonUtils.utility.dialog.DialogUtil
import com.app.chinwag.commonUtils.utility.extension.sharedPreference
import com.app.chinwag.commonUtils.utility.extension.showSnackBar
import com.app.chinwag.dataclasses.generics.Settings
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.hb.logger.Logger
import com.hb.logger.msc.MSCGenerator
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: VM

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    val logger by lazy {
        Logger(this::class.java.simpleName)
    }

//    private var mIdlingResource: SimpleIdlingResource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildActivityComponent())
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setDataBindingLayout()
        setupObservers()
        setupView(savedInstanceState)
        viewModel.onCreate()

        MSCGenerator.addLineComment(this::class.java.simpleName)
    }

    private fun buildActivityComponent() =
        DaggerActivityComponent
            .builder()
            .applicationComponent((application as AppineersApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()


    protected open fun setupObservers() {
        viewModel.messageString.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.showDialog.observe(this, Observer {
            if (it) showLoadingDialog() else hideLoadingDialog()
        })

        //(application as AppineersApplication).isConnected.observe(this, { internetConnection = it })
    }

    fun checkInternet(): Boolean {
        if (isNetworkAvailable()) {
            return true
        } else {
            showMessage(getString(R.string.network_connection_error))
            return false
        }
    }

    fun showLoadingDialog() {
        LoadingDialog.showDialog()
    }

    fun hideLoadingDialog() {
        LoadingDialog.dismissDialog()
    }

    open fun goBack() = onBackPressed()

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    fun showMessage(message: CharSequence) {
        message.toString().showSnackBar(context = this)
    }

    fun showMessage(message: CharSequence, type: Int) {
        message.toString().showSnackBar(context = this, type = type)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("NewApi")
    open fun isJobServiceOn(JOB_ID: Int): Boolean {
        val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        var hasBeenScheduled = false
        for (jobInfo in scheduler.allPendingJobs) {
            if (jobInfo.id == JOB_ID) {
                hasBeenScheduled = true
                break
            }
        }
        return hasBeenScheduled
    }

    /** ad load listener*/
    private val adListener = object : AdListener() {
        override fun onAdClosed() {
            super.onAdClosed()
        }

        override fun onAdFailedToLoad(le: LoadAdError?) {
            super.onAdFailedToLoad(le)
        }

        override fun onAdOpened() {
            super.onAdOpened()
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
        }

        override fun onAdClicked() {
            super.onAdClicked()
        }

    }

    /**
     * This method will show banner ads
     * @param adView AdView
     */
    fun showAdMob(adView: AdView) {
        if (sharedPreference.isAdRemoved || !AppConfig.BANNER_AD) {
            adView.visibility = View.GONE
        } else {
            val adRequest = AdRequest.Builder().build()
            adView.adListener = adListener
            adView.loadAd(adRequest)
        }
    }

    /**
     * Initialize interstitial ads
     */
    fun loadInterstitialAds() {
        val adManagerAdRequest = AdManagerAdRequest.Builder().build()
        AdManagerInterstitialAd.load(
            this,
            getString(R.string.admob_interstitial_unit_id_test),
            adManagerAdRequest,
            object :
                AdManagerInterstitialAdLoadCallback() {
                override fun onAdLoaded(manager: AdManagerInterstitialAd) {
                    super.onAdLoaded(manager)
                    showInterstitialAd(manager)
                    logger.dumpCustomEvent("InterstitialAd", "Ad Loaded.")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    logger.dumpCustomEvent("InterstitialAd", "Load error - ${p0.responseInfo}")
                }

            })
    }

    private fun showInterstitialAd(manager: AdManagerInterstitialAd) {
        manager.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(ae: AdError?) {
                super.onAdFailedToShowFullScreenContent(ae)
                logger.dumpCustomEvent("InterstitialAd", "Load error ${ae?.message}")
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                logger.dumpCustomEvent("InterstitialAd", "Ad showed on full screen")
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                logger.dumpCustomEvent("InterstitialAd", "Ad Dismissed.")
            }

        }

        manager.show(this)
    }

    /**
     * Only called from test, creates and returns a new [SimpleIdlingResource].
     */
//    @VisibleForTesting
//    open fun getIdlingResource(): IdlingResource? {
//        if (mIdlingResource == null) {
//            mIdlingResource = SimpleIdlingResource()
//        }
//        return mIdlingResource
//    }

    protected abstract fun setDataBindingLayout()

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)

    protected abstract fun setupView(savedInstanceState: Bundle?)

    fun getParseAddressComponents(addressComponents: AddressComponents): LocationAddressModel {
        return LocationAddressModel().apply {
            for (addressComponent in addressComponents.asList()) {
                for (type in addressComponent.types) {
                    if (type == "street_number") {
                        address = addressComponent.name
                    } else if (type == "route") {
                        address = addressComponent.name
                    } else if (type == "locality") {
                        city = addressComponent.name
                    } else if (type == "administrative_area_level_1") {
                        state = addressComponent.shortName.toString()
                    } else if (type == "postal_code") {
                        zipCode = addressComponent.name
                    } else if (type == "country") {
                        country = addressComponent.name.toString()
                    }
                }
            }
        }
    }

    /**
     * Navigate user to Login Screen if user not logged in or want to logout
     */
    fun navigateToLoginScreen(isLogOut: Boolean) {
        if (isLogOut) {
            sharedPreference.userDetail = null
            sharedPreference.isLogin = false
            sharedPreference.authToken = ""
            Logger.setUserInfo("")
        }
        val intent =
            Intent(this@BaseActivity, (application as AppineersApplication).getLoginActivity())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Navigate user to Home Screen after successfully login/signup
     */
    fun navigateToHomeScreen() {
        val intent = Intent(this@BaseActivity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    /**
     * If user login in another device, then show session expire dialog to user and navigate to login screen
     */
    fun showSessionExpireDialog() {
        DialogUtil.alert(
            context = this@BaseActivity,
            msg = getString(R.string.msg_looged_in_from_other_device),
            positiveBtnText = getString(R.string.ok),
            negativeBtnText = "",
            il = object : DialogUtil.IL {
                override fun onSuccess() {
                    navigateToLoginScreen(true)
                }

                override fun onCancel(isNeutral: Boolean) {
                    navigateToLoginScreen(true)
                }
            },
            isCancelable = false
        )
    }

    /**
     * Handle api error
     * @param settings [ERROR : Settings]
     * @param showError Boolean
     * @param showSessionExpire Boolean
     * @return Boolean
     */
    fun handleApiError(
        settings: Settings?,
        showError: Boolean = true,
        showSessionExpire: Boolean = true
    ): Boolean {
        hideLoadingDialog()
        return when {
            settings?.success == Settings.AUTHENTICATION_ERROR -> {
                if (showSessionExpire) showSessionExpireDialog()
                true
            }
            settings?.success == Settings.NETWORK_ERROR -> {
                settings.message.showSnackBar(this@BaseActivity)
                true
            }
            settings?.success == "0" -> false
            else -> {
                if (showError) settings?.message?.showSnackBar(this@BaseActivity)
                true
            }
        }
    }

    /**
     * Log all firebase events
     * @param id String Id of event
     * @param name String Name of event
     * @param contentType String content type
     */
    fun setFireBaseAnalyticsData(id: String, name: String, contentType: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        logger.dumpCustomEvent(name, contentType)
    }

    private fun isNetworkAvailable(): Boolean {
        val conMgr = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        return netInfo != null
    }

}