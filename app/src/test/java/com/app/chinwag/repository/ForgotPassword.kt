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
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class ForgotPassword : KotlinBaseMockObjectsClass() {

    @Rule
    @JvmField
    val run = InstantTaskExecutorRule()

    private val mockResetPasswordRepositoryTest = mock<ResetPasswordRepository>()
    private val resetPasswordRepository by lazy {
        ResetPasswordRepository(mockNetworkService)
    }

    @Before
    fun setUp() {
        Mockito.reset(mockNetworkService, mockApplication)
    }

    @Test
    fun verifyConstructorParameter(){
        assertEquals(mockNetworkService, resetPasswordRepository.networkService)
    }

    @Test
    fun verifyForgotPassword(){
        whenever(mockNetworkService.callResetPassword("Pass@123","9545954400","bzV6dFNLJjE2MTE1NTkxNTM="))
            .thenReturn(Single.just(TAListResponse()))
        whenever(mockResetPasswordRepositoryTest.callResetPassword(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(Single.just(TAListResponse()))
        resetPasswordRepository.callResetPassword("Pass@123","9545954400","bzV6dFNLJjE2MTE1NTkxNTM=")
            .test().assertComplete()
    }
}