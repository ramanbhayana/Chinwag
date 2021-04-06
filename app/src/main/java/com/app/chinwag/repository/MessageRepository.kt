package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import javax.inject.Inject

class MessageRepository @Inject constructor(
    val networkService: NetworkService
) {}