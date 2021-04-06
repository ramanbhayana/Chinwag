package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import javax.inject.Inject

class FriendRepository @Inject constructor(
    val networkService: NetworkService
) {}