package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.OTPResponse
import com.google.gson.JsonElement
import io.reactivex.Single
import javax.inject.Inject

class ChangePhoneNumberOTPRepository @Inject constructor(
    val networkService: NetworkService
) {

    fun checkUniqueUser(map: HashMap<String, String>): Single<TAListResponse<OTPResponse>> =
        networkService.callCheckUniqueUser(map)

    fun callChangePhoneNumber(number: String): Single<TAListResponse<JsonElement>> =
        networkService.callChangePhoneNumber(number)
}