package com.app.chinwag.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.LoginResponse
import com.app.chinwag.mainactivitytest.errorMsg
import com.app.chinwag.mainactivitytest.getErrorResponse
import com.app.chinwag.objectclasses.KotlinBaseMockObjectsClass
import com.app.chinwag.repository.LoginWithEmailSocialRepository
import com.app.chinwag.utils.mock
import com.app.chinwag.utils.whenever
import com.google.gson.JsonElement
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class LoginWithEmailSocialViewModelTest : KotlinBaseMockObjectsClass(){

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val mockLoginWithEmailSocialRepositoryTest = mock<LoginWithEmailSocialRepository>()
    private val mockLoginSocialObserver = mock<Observer<TAListResponse<LoginResponse>>>()
    private val mockLoginEmailObserver = mock<Observer<TAListResponse<LoginResponse>>>()
    private val mockResendLinkObserver = mock<Observer<TAListResponse<JsonElement>>>()
    private val showingDialogObserver = mock<Observer<Boolean>>()
    private val messageStringObserver = mock<Observer<Resource<String>>>()

    //for social login
    private val socialLoginResponse = TAListResponse<LoginResponse>()
    private val emptyListResponseForSocial : ArrayList<LoginResponse> = arrayListOf()

    //for email login
    private val emailLoginResponse = TAListResponse<LoginResponse>()
    private val emptyListResponseForEmail : ArrayList<LoginResponse> = arrayListOf()

    //for resend link
    private val resendLinkResponse = TAListResponse<JsonElement>()
    private val emptyListResponseForLink : ArrayList<JsonElement> = arrayListOf()

    private val viewModel by lazy {
        LoginWithEmailSocialViewModel(
            testSchedulerProvider,
            mockCompositeDisposable,
            mockNetworkHelper,
            mockLoginWithEmailSocialRepositoryTest
        )
    }

    @Before
    fun setUp() {
        Mockito.reset(
            mockCompositeDisposable,
            mockNetworkHelper,
            mockLoginWithEmailSocialRepositoryTest,
            showingDialogObserver,
            messageStringObserver
        )
    }

    @Test
    fun verifySocialLoginError(){

        val map = HashMap<String, String>()
        map["social_login_type"] = "SOCIAL LOGIN TYPE"
        map["social_login_id"] = "SOCIAL LOGIN ID"
        map["device_type"] = "ANDROID"
        map["device_model"] = "LENOVO MOTO C PLUS"
        map["device_os"] = "Lollipop 5.1"
        map["device_token"] = "DEVICE TOKEN"

        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.loginWithSocial(map))
            .thenReturn(Single.error(getErrorResponse()))
        whenever(mockLoginWithEmailSocialRepositoryTest.callLoginWithEmailSocial(ArgumentMatchers.anyMap<String, String>() as HashMap<String, String>))
            .thenReturn(Single.error(getErrorResponse()))

        viewModel.showDialog.observeForever(showingDialogObserver)
        viewModel.messageString.observeForever(messageStringObserver)

        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showingDialogObserver, Mockito.times(0)).onChanged(capture())
            Mockito.verify(messageStringObserver, Mockito.times(0)).onChanged(
                Resource.error(
                    errorMsg
                )
            )
        }
    }

    @Test
    fun verifyEmailLoginError(){

        val map = HashMap<String, String>()
        map["email"] = "akshayko81194@gmal"
        map["password"] = "Pass123@"
        map["device_type"] = "ANDROID"
        map["device_model"] = "LENOVO MOTO C PLUS"
        map["device_os"] = "Lollipop 5.1"
        map["device_token"] = "DEVICE TOKEN"

        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.loginWithEmail(map))
            .thenReturn(Single.error(getErrorResponse()))
        whenever(mockLoginWithEmailSocialRepositoryTest.callLoginWithEmail(ArgumentMatchers.anyMap<String, String>() as HashMap<String, String>))
            .thenReturn(Single.error(getErrorResponse()))

        viewModel.showDialog.observeForever(showingDialogObserver)
        viewModel.messageString.observeForever(messageStringObserver)

        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showingDialogObserver, Mockito.times(0)).onChanged(capture())
            Mockito.verify(messageStringObserver, Mockito.times(0)).onChanged(
                Resource.error(
                    errorMsg
                )
            )
        }
    }

    @Test
    fun verifyResendLinkError(){

        val map = HashMap<String, String>()
        map["email"] = "akshayko81194@gmal"

        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.callSendVerificationLink(map))
            .thenReturn(Single.error(getErrorResponse()))
        whenever(mockLoginWithEmailSocialRepositoryTest.callResendLink(ArgumentMatchers.anyMap<String, String>() as HashMap<String, String>))
            .thenReturn(Single.error(getErrorResponse()))

        viewModel.showDialog.observeForever(showingDialogObserver)
        viewModel.messageString.observeForever(messageStringObserver)

        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showingDialogObserver, Mockito.times(0)).onChanged(capture())
            Mockito.verify(messageStringObserver, Mockito.times(0)).onChanged(
                Resource.error(
                    errorMsg
                )
            )
        }
    }

    @Test
    fun verifySocialLoginSuccess(){

        val map = HashMap<String, String>()
        map["social_login_type"] = "SOCIAL LOGIN TYPE"
        map["social_login_id"] = "SOCIAL LOGIN ID"
        map["device_type"] = "ANDROID"
        map["device_model"] = "LENOVO MOTO C PLUS"
        map["device_os"] = "Lollipop 5.1"
        map["device_token"] = "DEVICE TOKEN"

        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.loginWithSocial(map))
            .thenReturn(Single.just(TAListResponse<LoginResponse>()))
        whenever(mockLoginWithEmailSocialRepositoryTest.callLoginWithEmailSocial(ArgumentMatchers.anyMap<String, String>() as HashMap<String, String>))
            .thenReturn(Single.just(getSocialLoginSuccessResponse()))

        viewModel.showDialog.observeForever(showingDialogObserver)
        viewModel.loginSocialMutableLiveData.observeForever(mockLoginSocialObserver)
        viewModel.callLoginWithEmailSocial("email","userId")

        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showingDialogObserver, Mockito.times(1)).onChanged(capture())
            Mockito.verify(mockLoginSocialObserver, Mockito.times(1)).onChanged(
                socialLoginResponse
            )
        }
    }

    private fun getSocialLoginSuccessResponse() : TAListResponse<LoginResponse>?{
        socialLoginResponse.data = emptyListResponseForSocial
        return socialLoginResponse
    }


    @Test
    fun verifyEmailLoginSuccess(){

        val map = HashMap<String, String>()
        map["email"] = "akshayko81194@gmal"
        map["password"] = "Pass123@"
        map["device_type"] = "ANDROID"
        map["device_model"] = "LENOVO MOTO C PLUS"
        map["device_os"] = "Lollipop 5.1"
        map["device_token"] = "DEVICE TOKEN"

        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.loginWithEmail(map))
            .thenReturn(Single.just(TAListResponse<LoginResponse>()))
        whenever(mockLoginWithEmailSocialRepositoryTest.callLoginWithEmail(ArgumentMatchers.anyMap<String, String>() as HashMap<String, String>))
            .thenReturn(Single.just(getEmailLoginSuccessResponse()))

        viewModel.showDialog.observeForever(showingDialogObserver)
        viewModel.loginEmailMutableLiveData.observeForever(mockLoginEmailObserver)
        viewModel.callLoginWithEmail("email","password")

        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showingDialogObserver, Mockito.times(1)).onChanged(capture())
            Mockito.verify(mockLoginEmailObserver, Mockito.times(1)).onChanged(
                emailLoginResponse
            )
        }
    }

    private fun getEmailLoginSuccessResponse() : TAListResponse<LoginResponse>?{
        emailLoginResponse.data = emptyListResponseForEmail
        return emailLoginResponse
    }


    @Test
    fun verifyResendLinkSuccess(){

        val map = HashMap<String, String>()
        map["email"] = "akshayko81194@gmal"

        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.callSendVerificationLink(map))
            .thenReturn(Single.just(TAListResponse<JsonElement>()))
        whenever(mockLoginWithEmailSocialRepositoryTest.callResendLink(ArgumentMatchers.anyMap<String, String>() as HashMap<String, String>))
            .thenReturn(Single.just(getResendLinkSuccessResponse()))

        viewModel.showDialog.observeForever(showingDialogObserver)
        viewModel.resendLinkMutableLiveData.observeForever(mockResendLinkObserver)
        viewModel.callResendLink("akshaykondekar81194@gmail.com")

        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showingDialogObserver, Mockito.times(1)).onChanged(capture())
            Mockito.verify(mockResendLinkObserver, Mockito.times(1)).onChanged(
                resendLinkResponse
            )
        }
    }

    private fun getResendLinkSuccessResponse() : TAListResponse<JsonElement>?{
        resendLinkResponse.data = emptyListResponseForLink
        return resendLinkResponse
    }
}