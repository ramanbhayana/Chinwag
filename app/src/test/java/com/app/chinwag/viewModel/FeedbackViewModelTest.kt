package com.app.chinwag.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.utility.extension.getStringRequestBody
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.FeedbackResponse
import com.app.chinwag.mainactivitytest.errorMsg
import com.app.chinwag.mainactivitytest.getErrorResponse
import com.app.chinwag.objectclasses.KotlinBaseMockObjectsClass
import com.app.chinwag.repository.FeedbackRepository
import com.app.chinwag.utils.mock
import com.app.chinwag.utils.whenever
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class FeedbackViewModelTest : KotlinBaseMockObjectsClass(){

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val mockFeedbackRepositoryTest = mock<FeedbackRepository>()
    private val feedbackObserver = mock<Observer<TAListResponse<FeedbackResponse>>>()
    private val showingDialogObserver = mock<Observer<Boolean>>()
    private val messageStringObserver = mock<Observer<Resource<String>>>()

    private val feedbackResponse = TAListResponse<FeedbackResponse>()
    private val emptyListResponse : ArrayList<FeedbackResponse> = arrayListOf()

    private val viewModel by lazy {
        FeedbackViewModel(
            testSchedulerProvider,
            mockCompositeDisposable,
            mockNetworkHelper,
            mockFeedbackRepositoryTest
        )
    }

    @Before
    fun setUp() {
        Mockito.reset(
            mockCompositeDisposable,
            mockNetworkHelper,
            mockFeedbackRepositoryTest,
            showingDialogObserver,
            messageStringObserver
        )
    }

    @Test
    fun verifyFeedbackError(){
        val map = HashMap<String, RequestBody>()
        map["feedback"] = getStringRequestBody("Test feedback Message")
        map["images_count"] = getStringRequestBody("2")
        map["device_type"] = getStringRequestBody("ANDROID")
        map["device_model"] = getStringRequestBody("LENOVO MOTO C PLUS")
        map["device_os"] = getStringRequestBody("VERSION 5252")

        val files = ArrayList<MultipartBody.Part>()

        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.callSendFeedback(map, files))
            .thenReturn(Single.error(getErrorResponse()))
        whenever(mockFeedbackRepositoryTest.sendFeedback(ArgumentMatchers.anyMap<String, RequestBody>() as HashMap<String, RequestBody>, ArgumentMatchers.anyList()))
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
    fun verifyFeedbackSuccess(){
        val map = HashMap<String, RequestBody>()
        map["feedback"] = getStringRequestBody("Test feedback Message")
        map["images_count"] = getStringRequestBody("2")
        map["device_type"] = getStringRequestBody("ANDROID")
        map["device_model"] = getStringRequestBody("LENOVO MOTO C PLUS")
        map["device_os"] = getStringRequestBody("VERSION 5252")

        val files = ArrayList<MultipartBody.Part>()

        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.callSendFeedback(map, files))
            .thenReturn(Single.just(TAListResponse<FeedbackResponse>()))
        whenever(mockFeedbackRepositoryTest.sendFeedback(ArgumentMatchers.anyMap<String, RequestBody>() as HashMap<String, RequestBody>, ArgumentMatchers.anyList()))
            .thenReturn(Single.just(getFeedbackSuccessResponse()))

        viewModel.showDialog.observeForever(showingDialogObserver)
        viewModel.feedbackLiveData.observeForever(feedbackObserver)
        viewModel.callReportProblem("Test feedback message")

        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showingDialogObserver, Mockito.times(1)).onChanged(capture())
            Mockito.verify(feedbackObserver, Mockito.times(1)).onChanged(
                feedbackResponse
            )
        }
    }

    private fun getFeedbackSuccessResponse() : TAListResponse<FeedbackResponse>{
        feedbackResponse.data = emptyListResponse
        return feedbackResponse
    }
}