package com.app.chinwag.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.viewModel.MainActivityViewModel
import com.app.chinwag.R
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<MainActivityViewModel>() {
    var dataBinding: ActivityMainBinding? = null

    override fun setDataBindingLayout() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        dataBinding?.mainActivityViewModel = viewModel
        dataBinding?.lifecycleOwner = this
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }



    override fun setupView(savedInstanceState: Bundle?) {
        setFireBaseAnalyticsData("id-loginScreen","view_loginScreen","view_loginScreen")
        supportActionBar?.title = "Login"
        btn.setOnClickListener {
            logger.dumpCustomEvent(IConstants.EVENT_CLICK,"Login Button Click")
            val intent = Intent(this, FormFillActivity::class.java)
            startActivity(intent)
        }
    }

}