package com.app.chinwag.dataclasses

import android.content.Context
import com.app.chinwag.commonUtils.utility.extension.getAppVersion
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class VersionConfigResponse(
    @JsonProperty("version_update_optional")
    val versionUpdateOptional: String? = null,

    @JsonProperty("iphone_version_number")
    val iphoneVersionNumber: String? = null,

    @JsonProperty("android_version_number")
    val androidVersionNumber: String? = null,

    @JsonProperty("version_update_check")
    val versionUpdateCheck: String? = null,

    @JsonProperty("version_check_message")
    val versionCheckMessage: String? = null,

    @JsonProperty("terms_conditions_updated")
    val termsConditionsUpdated: String? = null,

    @JsonProperty("privacy_policy_updated")
    val privacyPolicyUpdated: String? = null,

    @JsonProperty("log_status_updated")
    val logStatusUpdated: String = ""

) {
    fun shouldShowVersionDialog(context: Context) =
        (((androidVersionNumber?.compareTo(getAppVersion(context, true))
            ?: 0) > 0) && versionUpdateCheck.equals("1"))

    fun isOptionalUpdate() = versionUpdateOptional.equals("1")

    fun shouldShowTNCUpdated() = termsConditionsUpdated.equals("1")

    fun shouldShowPrivacyPolicyUpdated() = privacyPolicyUpdated.equals("1")

}