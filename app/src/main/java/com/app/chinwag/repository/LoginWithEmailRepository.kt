package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.LoginResponse
import com.google.gson.JsonElement
import io.reactivex.Single
import javax.inject.Inject

class LoginWithEmailRepository @Inject constructor(val networkService: NetworkService) {

    fun callLoginWithEmail(map: HashMap<String, String>): Single<TAListResponse<LoginResponse>> =
        networkService.loginWithEmail(map = map)

    fun callResendLink(map: HashMap<String, String>): Single<TAListResponse<JsonElement>> =
        networkService.callSendVerificationLink(map = map)
}
