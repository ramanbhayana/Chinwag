package com.app.chinwag.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.commonUtils.utility.getDeviceName
import com.app.chinwag.commonUtils.utility.getDeviceOSVersion
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.objectclasses.KotlinBaseMockObjectsClass
import com.app.chinwag.utils.mock
import com.app.chinwag.utils.whenever
import io.reactivex.Single
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class LoginWithPhoneNumberSocialRepositoryTest : KotlinBaseMockObjectsClass() {

    @Rule
    @JvmField
    val run = InstantTaskExecutorRule()

    private val mockloginWithPhoneNumberSocialRepositoryTest = mock<LoginWithPhoneNumberSocialRepository>()
    private val loginWithPhoneNumberSocialRepository by lazy {
        LoginWithPhoneNumberSocialRepository(mockNetworkService)
    }

    @Before
    fun setUp() {
        Mockito.reset(mockNetworkService, mockApplication)
    }

    @Test
    fun verifyConstructorParameter(){
        assertEquals(mockNetworkService, loginWithPhoneNumberSocialRepository.networkService)
    }

    @Test
    fun callLoginWithPhoneNumber(){
        val map = HashMap<String, String>()
        map["mobile_number"] = "8000154545"
        map["password"] = "Test@123"
        map["device_type"] = IConstants.DEVICE_TYPE_ANDROID
        map["device_model"] = getDeviceName()
        map["device_os"] = getDeviceOSVersion()
        map["device_token"] = "bzV6dFNLJjE2MTE1NTkxNTM="
        whenever(mockNetworkService.callLoginWithPhone(map = map))
            .thenReturn(Single.just(TAListResponse()))
        whenever(mockloginWithPhoneNumberSocialRepositoryTest.callLoginWithPhoneNumber(map = map))
            .thenReturn(Single.just(TAListResponse()))
        loginWithPhoneNumberSocialRepository.callLoginWithPhoneNumber(map = map)
            .test().assertComplete()
    }

 @Test
    fun callLoginWithPhoneNumberSocail(){
        val map = HashMap<String, String>()
     map["social_login_type"] = "Google"
     map["social_login_id"] = "Xyz@123"
     map["device_type"] = IConstants.DEVICE_TYPE_ANDROID
        map["device_model"] = getDeviceName()
        map["device_os"] = getDeviceOSVersion()
        map["device_token"] = "bzV6dFNLJjE2MTE1NTkxNTM="
        whenever(mockNetworkService.loginWithSocial(map = map))
            .thenReturn(Single.just(TAListResponse()))
        whenever(mockloginWithPhoneNumberSocialRepositoryTest.callLoginWithPhoneNumberSocial(map = map))
            .thenReturn(Single.just(TAListResponse()))
        loginWithPhoneNumberSocialRepository.callLoginWithPhoneNumberSocial(map = map)
            .test().assertComplete()
    }


}