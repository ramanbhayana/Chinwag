package com.app.chinwag.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.viewModel.MainActivityViewModel
import com.app.chinwag.R
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.application.AppineersApplication.Companion.sharedPreference
import com.app.chinwag.databinding.ActivityMainBinding
import com.app.chinwag.view.onboarding.OnBoardingActivity
import com.hb.logger.Logger

@Suppress("DEPRECATION")
class SplashActivity : BaseActivity<MainActivityViewModel>() {
    var dataBinding: ActivityMainBinding? = null

    override fun setDataBindingLayout() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        dataBinding?.mainActivityViewModel = viewModel
        dataBinding?.lifecycleOwner = this
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)

    }

    override fun setupView(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            //iv_info.scaleX = -1f
            Handler().postDelayed({
                finish()
                startActivity(Intent(this@SplashActivity, getLaunchClass()))
            }, 500)
        }, 1000)

    }

    /**
     * Choose activity to open
     *
     * @return if user already login, then open home activity, else open login activity
     */
    private fun getLaunchClass(): Class<*> {
        return if (sharedPreference.isLogin) {
            // Return Home activity
//            Logger.setUserInfo(sharedPreference.userDetail?.email ?: "")
            HomeActivity::class.java
        } else {
            //Return Login Activity
            if (true) {
                (application as AppineersApplication).getLoginActivity()
            } else {
                OnBoardingActivity::class.java
            }
        }
    }

}