package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import javax.inject.Inject

class ChangePhoneNumberRepository @Inject constructor(
    val networkService: NetworkService
) {
    fun checkUniqueUser(map: HashMap<String, String>) = networkService.callCheckUniqueUser(map)
}