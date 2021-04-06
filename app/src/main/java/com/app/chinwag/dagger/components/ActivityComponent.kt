package com.app.chinwag.dagger.components

import com.app.chinwag.dagger.ActivityScope
import com.app.chinwag.dagger.modules.ActivityModule
import com.app.chinwag.view.*
import com.app.chinwag.view.settings.staticpages.StaticPagesMultipleActivity
import com.app.chinwag.view.authentication.resetpassword.ResetPasswordActivity
import com.app.chinwag.view.authentication.forgotpassword.email.ForgotPasswordWithEmailActivity
import com.app.chinwag.view.authentication.forgotpassword.phone.ForgotPasswordWithPhoneActivity
import com.app.chinwag.view.onboarding.OnBoardingActivity
import com.app.chinwag.view.settings.feedback.SendFeedbackActivity
import com.app.chinwag.view.settings.changepassword.ChangePasswordActivity
import com.app.chinwag.view.settings.editprofile.EditProfileActivity

import com.app.chinwag.view.settings.changephonenumber.ChangePhoneNumberActivity
import com.app.chinwag.view.settings.changephonenumber.ChangePhoneNumberOTPActivity
import dagger.Component
import com.app.chinwag.view.authentication.login.loginwithemail.LoginWithEmailActivity
import com.app.chinwag.view.authentication.login.loginwithemailsocial.LoginWithEmailSocialActivity
import com.app.chinwag.view.authentication.login.loginwithphonenumber.LoginWithPhoneNumberActivity
import com.app.chinwag.view.authentication.login.loginwithphonenumbersocial.LoginWithPhoneNumberSocialActivity
import com.app.chinwag.view.authentication.otp.otpsignup.OTPSignUpActivity
import com.app.chinwag.view.authentication.signup.SignUpActivity
import com.app.chinwag.view.authentication.otp.otpforgotpassword.OTPForgotPasswordActivity
@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent{
    fun inject(mainActivity: MainActivity)
    fun inject(formActivity: FormActivity)
    fun inject(formFillActivity: FormFillActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(sampleActivity: SampleActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(staticPagesMultipleActivity: StaticPagesMultipleActivity)
    fun inject(forgotPasswordWithPhoneActivity: ForgotPasswordWithPhoneActivity)
    fun inject(forgotPasswordWithEmailActivity: ForgotPasswordWithEmailActivity)
    fun inject(resetPasswordActivity: ResetPasswordActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(signUpActivity: SignUpActivity)
    fun inject(otpSignUpActivity: OTPSignUpActivity)
    fun inject(loginWithPhoneNumberActivity: LoginWithPhoneNumberActivity)
    fun inject(loginWithEmailSocialActivity: LoginWithEmailSocialActivity)
    fun inject(loginWithEmailActivity: LoginWithEmailActivity)
    fun inject(loginWithPhoneNumberSocialActivity: LoginWithPhoneNumberSocialActivity)
    fun inject(sendFeedbackActivity: SendFeedbackActivity)
    fun inject(changePasswordActivity: ChangePasswordActivity)
    fun inject(changePhoneNumberActivity: ChangePhoneNumberActivity)
    fun inject(onBoardingActivity: OnBoardingActivity)
    fun inject(otpForgotPasswordActivity: OTPForgotPasswordActivity)
    fun inject(navigationSideMenu: NavigationSideMenu)
    fun inject(changePhoneNumberOTPActivity: ChangePhoneNumberOTPActivity)
    fun inject(editProfileActivity: EditProfileActivity)
}