package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.google.gson.JsonElement
import io.reactivex.Single
import javax.inject.Inject

class ChangePasswordRepository @Inject constructor(
    val networkService: NetworkService
) {

    fun callChangePassword(oldPassword: String, newPassword: String)
    : Single<TAListResponse<JsonElement>> = networkService.callChangePassword(oldPassword,newPassword)
}