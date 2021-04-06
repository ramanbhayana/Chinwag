package com.app.chinwag.view.authentication.login.loginwithemail

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.chinwag.R
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.commonUtils.common.isValidEmail
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.commonUtils.utility.dialog.DialogUtil
import com.app.chinwag.commonUtils.utility.extension.getTrimText
import com.app.chinwag.commonUtils.utility.extension.isValidPassword
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.databinding.ActivityLoginWithEmailBinding
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.view.authentication.forgotpassword.email.ForgotPasswordWithEmailActivity
import com.app.chinwag.view.authentication.signup.SignUpActivity
import com.app.chinwag.viewModel.LoginWithEmailViewModel
import com.google.android.material.textfield.TextInputEditText
import com.hb.logger.msc.MSCGenerator
import com.hb.logger.msc.core.GenConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginWithEmailActivity : BaseActivity<LoginWithEmailViewModel>() {

    private var binding: ActivityLoginWithEmailBinding? = null

    override fun setDataBindingLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_with_email)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        (application as AppineersApplication).setApplicationLoginType(IConstants.LOGIN_TYPE_EMAIL)
        setFireBaseAnalyticsData(
            "id-loginWithEmailScreen",
            "view_loginWithEmailScreen",
            "view_loginWithEmailScreen"
        )
        initListeners()
    }

    private fun initListeners() {
        binding?.let {
            with(it, {
                btnLogin.setOnClickListener {
                    logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Login Button Click")
                    MSCGenerator.addAction(
                        GenConstants.ENTITY_USER,
                        GenConstants.ENTITY_APP,
                        "user login"
                    )
                    hideKeyboard()
                    binding?.tietPassword?.let { it1 ->
                        binding?.tietEmail?.let { it2 ->
                            validateAndSendRequest(
                                it2,
                                it1
                            )
                        }
                    }
                }

                mbtnSkip.setOnClickListener {
                    logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Skip Button Click")
                    MSCGenerator.addAction(
                        GenConstants.ENTITY_USER,
                        GenConstants.ENTITY_APP,
                        "facebook login"
                    )
                    AppineersApplication.sharedPreference.isSkip = true
                    navigateToHomeScreen()
                }

                tvCreateNewAccount.setOnClickListener {
                    logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Sign up Button Click")
                    MSCGenerator.addAction(
                        GenConstants.ENTITY_USER,
                        GenConstants.ENTITY_APP,
                        "google login"
                    )
                    hideKeyboard()
                    startActivity(
                        Intent(
                            this@LoginWithEmailActivity,
                            SignUpActivity::class.java
                        )
                    )
                }

                tvForgotPassword.setOnClickListener {
                    hideKeyboard()
                    logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Forgot Password Button Click")
                    MSCGenerator.addAction(
                        GenConstants.ENTITY_USER,
                        GenConstants.ENTITY_APP,
                        "forgot password"
                    )
                    startActivity(
                        Intent(
                            this@LoginWithEmailActivity,
                            ForgotPasswordWithEmailActivity::class.java
                        )
                    )
                }

                tietPassword.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                    hideKeyboard()
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        logger.dumpCustomEvent(IConstants.EVENT_CLICK, "Login Button Click")
                        MSCGenerator.addAction(
                            GenConstants.ENTITY_USER,
                            GenConstants.ENTITY_APP,
                            "user login"
                        )
                        binding?.tietPassword?.let { it1 ->
                            binding?.tietEmail?.let { it2 ->
                                validateAndSendRequest(
                                    it2,
                                    it1
                                )
                            }
                        }
                        return@OnEditorActionListener true
                    }
                    false
                })
            })
        }
    }

    /**
     * Show email not verified dialog
     */
    private fun showResendEmailDialog(message: String) {
        DialogUtil.alert(
            context = this@LoginWithEmailActivity,
            msg = message + "\n" + getString(R.string.msg_resend_email),
            positiveBtnText = getString(R.string.resend),
            negativeBtnText = getString(R.string.cancel),
            il = object : DialogUtil.IL {
                override fun onSuccess() {
                    binding?.tietEmail?.getTrimText()
                        ?.let {
                            viewModel.showDialog.postValue(true)
                            viewModel.callResendLink(email = it)
                        }
                }

                override fun onCancel(isNeutral: Boolean) {

                }
            },
            isCancelable = false
        )
    }

    //Method to validate data and call api request method
    private fun validateAndSendRequest(
        tietUsername: TextInputEditText,
        tietPassword: TextInputEditText
    ) {
        val email = tietUsername.text.toString()
        val password = tietPassword.text.toString()
        val validateEmail = email.isValidEmail()
        val validatePassword = password.isValidPassword()
        when {
            !validateEmail -> {
                tietUsername.error = getString(R.string.alert_enter_valid_email)
                showMessage(
                    getString(R.string.alert_enter_valid_email)
                )
            }

            !validatePassword -> {
                tietPassword.error = getString(R.string.alert_valid_password)
                showMessage(getString(R.string.alert_valid_password))
            }

            checkInternet() -> {
                tietUsername.error = null
                tietPassword.error = null
                callLoginWithEmail(email = email, password = password)
            }
        }
    }

    //Method to get forgot password response with emailId
    private fun callLoginWithEmail(email: String, password: String) {
        hideKeyboard()
        viewModel.showDialog.postValue(true)
        /* when {
             internetConnection -> {*/
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.callLoginWithEmail(email = email, password = password)
        }
        /* }
         else -> show(this, getString(R.string.network_connection_error))*/
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loginEmailMutableLiveData.observe(this, Observer { response ->
            viewModel.showDialog.postValue(false)
            if (response.settings?.isSuccess == true) {
                MSCGenerator.addAction(
                    GenConstants.ENTITY_APP,
                    GenConstants.ENTITY_USER,
                    "logged in success"
                )
                if (!response?.data.isNullOrEmpty()) {
                    viewModel.saveUserDetails(response?.data!![0])
                    navigateToHomeScreen()
                }
            } else if (response.settings?.success.equals("3")) {
                MSCGenerator.addAction(
                    GenConstants.ENTITY_APP,
                    GenConstants.ENTITY_USER,
                    "resend email"
                )
                showResendEmailDialog(response?.settings?.message ?: "")
            } else {
                MSCGenerator.addAction(
                    GenConstants.ENTITY_APP,
                    GenConstants.ENTITY_USER,
                    "logged in failed"
                )
                showMessage(response.settings!!.message)
                Timber.d(response.settings?.message)
            }

        })

        viewModel.resendLinkMutableLiveData.observe(this, Observer { response ->
            viewModel.showDialog.postValue(false)
            if (response.settings?.isSuccess == true) {
                MSCGenerator.addAction(
                    GenConstants.ENTITY_APP,
                    GenConstants.ENTITY_USER,
                    "resend email success"
                )
                showMessage(response.settings!!.message, IConstants.SNAKBAR_TYPE_SUCCESS)
            } else {
                MSCGenerator.addAction(
                    GenConstants.ENTITY_APP,
                    GenConstants.ENTITY_USER,
                    "resend email failed"
                )
                showMessage(response.settings!!.message)
                Timber.d(response.settings?.message)
            }
        })
    }
}