package com.app.chinwag.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

class SignupTest : KotlinBaseMockObjectsClass() {

    @Rule
    @JvmField
    val run = InstantTaskExecutorRule()

    private val mockSettingsRepositoryTest = mock<SettingsRepository>()
    private val settingsRepository by lazy {
        SettingsRepository(mockNetworkService)
    }

    @Before
    fun setUp() {
        Mockito.reset(mockNetworkService, mockApplication)
    }

    @Test
    fun verifyConstructorParameter(){
        assertEquals(mockNetworkService, settingsRepository.networkService)
    }

    @Test
    fun callSignUpFail(){
        whenever(mockNetworkService.callLogOut())
            .thenReturn(Single.just(TAListResponse()))
        whenever(mockSettingsRepositoryTest.callLogout())
            .thenReturn(Single.just(TAListResponse()))
        settingsRepository.callLogout()
            .test().assertComplete()
    }
 @Test
    fun callSignUpAPI(){
        whenever(mockNetworkService.callDeleteAccount())
            .thenReturn(Single.just(TAListResponse()))
        whenever(mockSettingsRepositoryTest.callDeleteAccount())
            .thenReturn(Single.just(TAListResponse()))
        settingsRepository.callDeleteAccount()
            .test().assertComplete()
    }
}