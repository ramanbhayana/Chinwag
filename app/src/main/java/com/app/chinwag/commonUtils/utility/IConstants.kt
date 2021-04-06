package com.app.chinwag.commonUtils.utility

import android.os.Environment

/**
 * This class contains all the constants value that is used in application
 * All values defined here, it can not be changed in application lifecycle
 */
interface IConstants {
    @Suppress("DEPRECATION")
    companion object {
        const val CITY_SEARCH: Int=10
        const val ADDRESS_SEARCH: Int=11
        const val SNAKBAR_TYPE_ERROR = 1
        const val SNAKBAR_TYPE_SUCCESS = 2
        const val SNAKBAR_TYPE_MESSAGE = 3
        const val MEDIA_TYPE_IMAGE = 1
        private val storageDir = Environment.getExternalStorageDirectory().toString() + "/TheAppineers/"
        val IMAGES_FOLDER_PATH = "$storageDir/Images"
        const val IMAGE_DIRECTORY_NAME = ".theappineers"
        const val FOLDER_NAME = "TheAppineers"
        const val SP_NOTIFICATION_CHANNEL_DEFAULT = "sp_notification_channel_default"
        const val PARAM_NOTIFICATION_TYPE = "type"

        const val SPLASH_TIME = 2000L//splash time 3second
        const val SNAKE_BAR_SHOW_TIME = 3000L
        val INCLUDE_HEADER = true

        const val REQUEST_SEARCH_PLACE = 115
        const val REQUEST_CODE_GALLERY = 500
        const val REQUEST_CODE_CAMERA = 600
        const val REQUEST_CODE_CROP_RESULT = 106
        const val CAMERA_REQUEST_CODE = 103
        const val REQUEST_CODE_CAPTURE_IMAGE = 107

        const val LOCATION_REQUEST_CODE = 108


        val STATIC_PAGE_ABOUT_US: String = "aboutus"
        val STATIC_PAGE_TERMS_CONDITION: String = "termsconditions"
        val STATIC_PAGE_PRIVACY_POLICY: String = "privacypolicy"

        const val BUNDLE_CROP_URI = "bundle_crop_uri"
        const val BUNDLE_IS_CROP_CANCEL = "is_crop_cancel"
        const val BUNDLE_IMG_URI = "bundle_img_uri"
        const val BUNDLE_CROP_SQUARE = "bundle_crop_square"
        const val BUNDLE_CROP_MIN_SIZE = "bundle_crop_min_size"


        const val SOCIAL_TYPE_FB = "facebook"
        const val SOCIAL_TYPE_GOOGLE = "google"
        const val SOCIAL_TYPE_APPLE = "apple"
        const val DEVICE_TYPE_ANDROID = "android"
        const val REQUEST_CODE_FACEBOOK_LOGIN = 1010
        const val REQUEST_CODE_GOOGLE_LOGIN = 1011
        const val REQUEST_CODE_APPLE_LOGIN = 1012
        const val LOGIN_TYPE_EMAIL = "email"
        const val LOGIN_TYPE_EMAIL_SOCIAL = "email_social"
        const val LOGIN_TYPE_PHONE = "phone"
        const val LOGIN_TYPE_PHONE_SOCIAL = "phone_social"
        const val COUNT_DOWN_TIMER = 30000L // 30 second
        const val ENABLED = "Enabled"
        const val DISABLED = "Disabled"
        var AUTOCOMPLETE_REQUEST_CODE = 1
        val MULTI_IMAGE_REQUEST_CODE = 303
        const val EVENT_CLICK = "Click Event"
        const val BUNDLE_SELECTED_IMAGE = "bundle_selected_image"
    }
}
