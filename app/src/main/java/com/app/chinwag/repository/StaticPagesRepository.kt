package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.StaticPageResponse
import com.google.gson.JsonElement
import io.reactivex.Single
import javax.inject.Inject

class StaticPagesRepository @Inject constructor(
    val networkService: NetworkService
) {
    fun getStaticPageData(code: String): Single<TAListResponse<StaticPageResponse>> =
        networkService.callGetStaticPageData(code)

    fun updateTNCPrivacyPolicy(type: String): Single<TAListResponse<JsonElement>> =
        networkService.callUpdateTNCPrivacyPolicy(type)
}