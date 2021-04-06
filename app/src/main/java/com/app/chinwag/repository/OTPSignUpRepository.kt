package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.LoginResponse
import com.app.chinwag.dataclasses.response.OTPResponse
import io.reactivex.Single
import okhttp3.RequestBody
import javax.inject.Inject

    class OTPSignUpRepository @Inject constructor(
    val networkService: NetworkService
) {

    fun callSignUpWithPhone(map: HashMap<String, RequestBody>): Single<TAListResponse<LoginResponse>> =
        networkService.callSignUpWithPhone(fieldMap = map)

    fun callSignUpWithSocial(map: HashMap<String, RequestBody>): Single<TAListResponse<LoginResponse>> =
        networkService.callSignUpWithSocial(fieldMap = map)

    fun callCheckUniqueUser(map: HashMap<String, String>): Single<TAListResponse<OTPResponse>> =
        networkService.callCheckUniqueUser(map = map)


}