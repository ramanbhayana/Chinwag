package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.LoginResponse
import io.reactivex.Single
import javax.inject.Inject

class LoginWithPhoneNumberRepository @Inject constructor(
    val networkService: NetworkService
) {
    fun callLoginWithPhoneNumber(map: HashMap<String, String>): Single<TAListResponse<LoginResponse>> =
        networkService.callLoginWithPhone(map = map)

}
