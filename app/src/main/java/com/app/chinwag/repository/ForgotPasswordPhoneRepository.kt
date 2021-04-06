package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.forgotpasswordwithphone.ResetWithPhone
import io.reactivex.Single
import javax.inject.Inject

class ForgotPasswordPhoneRepository @Inject constructor(
        val networkService: NetworkService
) {

    fun getForgotPasswordPhoneResponse(mobileNumber: String): Single<TAListResponse<ResetWithPhone>> = networkService.callForgotPasswordWithPhone(mobileNumber)
}