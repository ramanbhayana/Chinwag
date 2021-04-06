package com.app.chinwag.view.notification

import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.commonUtils.utility.NotificationUtils
import com.app.chinwag.commonUtils.utility.extension.sharedPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(deviceToken: String) {
        super.onNewToken(deviceToken)

        sendRegistrationTokenToServer(deviceToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {


            NotificationUtils.createAppNotification(this, application as AppineersApplication, remoteMessage)
        }

    }

    private fun sendRegistrationTokenToServer(token: String) {
        sharedPreference.deviceToken = token
        if (sharedPreference.authToken.isNullOrEmpty())
            return
        (application as AppineersApplication).applicationComponent.getNetworkService().callUpdateDeviceToken(token)

    }

}
