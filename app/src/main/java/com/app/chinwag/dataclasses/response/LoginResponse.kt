package com.app.chinwag.dataclasses.response

import android.telephony.PhoneNumberUtils
import com.app.chinwag.commonUtils.utility.extension.fromServerDatetoYYYYMMDD
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Data class is used to hold login response
 * @property address String?
 * @property emailVerified String?
 * @property deviceModel String?
 * @property city String?
 * @property userName String?
 * @property latitude String?
 * @property mobileNo String?
 * @property lastName String?
 * @property deviceType String?
 * @property deviceOs String?
 * @property zipCode String?
 * @property accessToken String?
 * @property addedAt String?
 * @property profileImage String?
 * @property updatedAt String?
 * @property userId String?
 * @property dob String?
 * @property deviceToken String?
 * @property stateId String?
 * @property firstName String?
 * @property email String?
 * @property longitude String?
 * @property status String?
 * @property loginType String?
 * @property socialLoginId String?
 * @property purchaseStatus String?
 * @property purchaseReceiptData String?
 * @property notification String?
 * @constructor
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginResponse(

    @JsonProperty("address")
    val address: String? = null,

    @JsonProperty("email_verified")
    val emailVerified: String? = null,

    @JsonProperty("device_model")
    val deviceModel: String? = null,

    @JsonProperty("city")
    val city: String? = null,

    @JsonProperty("user_name")
    val userName: String? = null,

    @JsonProperty("latitude")
    val latitude: String? = null,

    @JsonProperty("mobile_no")
    var mobileNo: String? = null,

    @JsonProperty("last_name")
    val lastName: String? = null,

    @JsonProperty("device_type")
    val deviceType: String? = null,

    @JsonProperty("device_os")
    val deviceOs: String? = null,

    @JsonProperty("zip_code")
    val zipCode: String? = null,

    @JsonProperty("access_token")
    val accessToken: String? = null,

    @JsonProperty("added_at")
    val addedAt: String? = null,

    @JsonProperty("profile_image")
    val profileImage: String? = null,

    @JsonProperty("updated_at")
    val updatedAt: String? = null,

    @JsonProperty("user_id")
    val userId: String? = null,

    @JsonProperty("dob")
    val dob: String? = null,

    @JsonProperty("device_token")
    val deviceToken: String? = null,

    @JsonProperty("state_name")
    val stateName: String? = null,

    @JsonProperty("first_name")
    val firstName: String? = null,

    @JsonProperty("email")
    val email: String? = null,

    @JsonProperty("longitude")
    val longitude: String? = null,

    @JsonProperty("status")
    val status: String? = null,

    @JsonProperty("social_login_type")
    val loginType: String? = "",

    @JsonProperty("social_login_id")
    val socialLoginId: String? = "",

    @JsonProperty("purchase_status")
    val purchaseStatus: String? = "",  //Yes/No

    @JsonProperty("purchase_receipt_data")
    val purchaseReceiptData: String? = "",

    @JsonProperty("push_notify")
    var notification: String? = "",

    @JsonProperty("log_status_updated")
    var logStatusUpdated: String? = "",

    @JsonProperty("terms_conditions_version")
    var terms_conditions_version: String? = "",

    @JsonProperty("privacy_policy_version")
    var privacy_policy_version: String? = ""
) {
    fun getFullName(): String {
        return "$firstName $lastName"
    }

    fun isSocialLogin(): Boolean {
        return !socialLoginId.isNullOrEmpty()
    }

    fun isNotificationOn(): Boolean {
        return notification.equals("Yes", true)
    }

    fun getDOBStr(): String {
        return dob.fromServerDatetoYYYYMMDD()
    }

    fun getFormattedPhoneNumber(): String {
        return if (mobileNo.isNullOrEmpty()) "" else PhoneNumberUtils.formatNumber(mobileNo, "US")
    }

}