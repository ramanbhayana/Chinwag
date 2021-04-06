package com.app.chinwag.dataclasses.generics

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @property success String?  1 -> If success 0 -> If false
 * @property message String
 * @property fields List of fields into the response data object
 * @property currPage String
 * @property prevPage String
 * @property nextPage String
 * @property accessToken String
 * @property isSuccess Boolean
 * @property isNetworkError Boolean
 * @property isAuthenticationError Boolean
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class Settings(
    @JsonProperty("fields")
    var fields: List<String>? = null,

    @JsonProperty("success")
    var success: String? = "0", // 0 -> Failure, 1->Success, 101-> Network error(showing network exception/

    @JsonProperty("message")
    var message: String = "",

    @JsonProperty("curr_page")
    var currPage: String? = "0",

    @JsonProperty("prev_page")
    var prevPage: String? = "0",

    @JsonProperty("next_page")
    var nextPage: String? = "0",

    @JsonProperty("access_token")
    var accessToken: String? = ""
) {

    val isSuccess: Boolean
        get() = success != null && success?.equals("1", ignoreCase = true) == true

    val isNetworkError: Boolean
        get() = success != null && success?.equals(NETWORK_ERROR, ignoreCase = true) == true

    val isAuthenticationError: Boolean
        get() = success != null && success?.equals(AUTHENTICATION_ERROR, ignoreCase = true) == true

    companion object {

        const val AUTHENTICATION_ERROR = "401"
        const val NETWORK_ERROR = "1001"
    }
}