package com.app.chinwag.view

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.app.chinwag.BuildConfig
import com.app.chinwag.R
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.application.AppineersApplication.Companion.sharedPreference
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.commonUtils.utility.dialog.AppUpdateDialog
import com.app.chinwag.commonUtils.utility.extension.showSnackBar
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.databinding.ActivityHomeBinding
import com.app.chinwag.dataclasses.VersionConfigResponse
import com.app.chinwag.dataclasses.response.StaticPage
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.view.friends.FriendsFragment
import com.app.chinwag.view.home.HomeFragment
import com.app.chinwag.view.message.MessagesFragment
import com.app.chinwag.view.profile.ProfileFragment
import com.app.chinwag.view.settings.SettingsFragment
import com.app.chinwag.view.settings.staticpages.StaticPagesMultipleActivity
import com.app.chinwag.viewModel.HomeViewModel
import com.hb.logger.Logger
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : BaseActivity<HomeViewModel>() {
    private lateinit var binding: ActivityHomeBinding

    override fun setDataBindingLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setFireBaseAnalyticsData("id-homeScreen", "view_homeScreen", "view_homeScreen")
        binding.apply {
            bottomNavigation.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.action_home -> {
                        logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Home Button CLick")
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is HomeFragment) {
                            true
                        } else {
                            setCurrentFragment(HomeFragment())
                            true
                        }
                    }
                    R.id.action_friends -> {
                        logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Friends Tab CLick")
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is FriendsFragment) {
                            true
                        } else {
                            setCurrentFragment(FriendsFragment())
                            true
                        }
                    }
                    R.id.action_message -> {
                        logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Message Tab Click")
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is MessagesFragment) {
                            true
                        } else {
                            setCurrentFragment(MessagesFragment())
                            true
                        }
                    }
                    R.id.action_profile -> {
                        logger.dumpCustomEvent(IConstants.EVENT_CLICK, "My Profile Tab Click")
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is ProfileFragment) {
                            true
                        } else {
                            setCurrentFragment(ProfileFragment())
                            true
                        }
                    }
                    R.id.action_settings -> {
                        logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Settings Tab Click")
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is SettingsFragment) {
                            true
                        } else {
                            setCurrentFragment(SettingsFragment())
                            true
                        }
                    }
                    else -> false
                }
            }
        }

        addObservers()
        when {
            checkInternet() -> viewModel.callGetConfigParameters()
        }

        binding.bottomNavigation.selectedItemId = R.id.action_home
    }

    /**
     * Show dialog for new application version is available
     * @param version VersionConfigResponse
     */
    private fun showNewVersionAvailableDialog(version: VersionConfigResponse) {
        AppUpdateDialog(this, version).show(supportFragmentManager, "update dialog")
    }

    private fun addObservers() {
        viewModel.configParamsPhoneLiveData.observe(this, Observer {
            if (it.settings?.isSuccess == true) {
                if (!it.data.isNullOrEmpty()) {
                    val config = it.data!![0]
                    if (config.shouldShowVersionDialog(this@HomeActivity)) {
                        showNewVersionAvailableDialog(config)
                    }
                    var pageCodeList: ArrayList<StaticPage> = ArrayList()
                    if (config.shouldShowTNCUpdated()) {
                        pageCodeList.add(
                            StaticPage(
                                pageCode = IConstants.STATIC_PAGE_TERMS_CONDITION,
                                forceUpdate = true
                            )
                        )
                    }
                    if (config.shouldShowPrivacyPolicyUpdated()) {
                        pageCodeList.add(
                            StaticPage(
                                pageCode = IConstants.STATIC_PAGE_PRIVACY_POLICY,
                                forceUpdate = true
                            )
                        )
                    }
                    if (!pageCodeList.isNullOrEmpty()) {
                        startActivity(
                            StaticPagesMultipleActivity.getStartIntent(
                                mContext = this@HomeActivity,
                                pageCodeList = pageCodeList
                            )
                        )
                    }


                    if (!BuildConfig.DEBUG) {
                        Log.e(
                            "LOGGING_API_CALL",
                            config.logStatusUpdated.toLowerCase(Locale.getDefault())
                        )
                        (application as AppineersApplication).isLogStatusUpdated.value =
                            (config.logStatusUpdated.toLowerCase(
                                Locale.getDefault()
                            )) == "active"
                        sharedPreference.logStatusUpdated =
                            config.logStatusUpdated.toLowerCase(Locale.getDefault())
                        if (sharedPreference.logStatusUpdated.equals("inactive", true)) {
                            Logger.clearAllLogs()
                            Logger.disableLogger()
                        } else if (sharedPreference.logStatusUpdated.equals("active", true)) {
                            Logger.enableLogger()
                        }
                    }
                }
            } else if (!handleApiError(it.settings)) {
                it?.settings?.message?.showSnackBar(this@HomeActivity)
            }
        })
    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameContainer, fragment)
            commit()
        }
}