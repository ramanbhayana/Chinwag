package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.LoginResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    val networkService: NetworkService
) {
    fun updateUserProfile(map: HashMap<String, RequestBody>, file: MultipartBody.Part?
    ): Single<TAListResponse<LoginResponse>> =
        networkService.callUpdateUserProfile(map,file)
}