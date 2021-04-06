package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.VersionConfigResponse
import com.app.chinwag.dataclasses.generics.TAListResponse
import io.reactivex.Single
import javax.inject.Inject

class HomeRepository @Inject constructor(
    val networkService: NetworkService
) {
    fun callConfigParameters(): Single<TAListResponse<VersionConfigResponse>> =
        networkService.callConfigParameters()
}