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

class HomeRepositoryTest : KotlinBaseMockObjectsClass() {

    @Rule
    @JvmField
    val run = InstantTaskExecutorRule()

    private val mockHomeRepositoryTest = mock<HomeRepository>()
    private val homeRepository by lazy {
        HomeRepository(mockNetworkService)
    }

    @Before
    fun setUp() {
        Mockito.reset(mockNetworkService, mockApplication)
    }

    @Test
    fun verifyConstructorParameter(){
        assertEquals(mockNetworkService, homeRepository.networkService)
    }

    @Test
    fun callLogout(){
        whenever(mockNetworkService.callConfigParameters())
            .thenReturn(Single.just(TAListResponse()))
        whenever(mockHomeRepositoryTest.callConfigParameters())
            .thenReturn(Single.just(TAListResponse()))
        homeRepository.callConfigParameters()
            .test().assertComplete()
    }

}