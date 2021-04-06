package com.app.chinwag.api.network

interface InternetConnectivityListener {

    fun onInternetConnectivityChanged(isConnected: Boolean)
}