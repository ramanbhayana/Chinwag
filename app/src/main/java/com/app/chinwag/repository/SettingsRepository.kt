package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.google.gson.JsonElement
import io.reactivex.Single
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    val networkService: NetworkService
) {
    fun callLogout(): Single<TAListResponse<JsonElement>> = networkService.callLogOut()

    fun callDeleteAccount(): Single<TAListResponse<JsonElement>> =
        networkService.callDeleteAccount()

    fun callGoAdFree(map: HashMap<String, String>): Single<TAListResponse<JsonElement>> =
        networkService.callGoAdFree(map = map)
}