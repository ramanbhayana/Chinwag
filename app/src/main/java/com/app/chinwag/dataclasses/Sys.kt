package com.app.chinwag.dataclasses

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Sys(

    @field:JsonProperty("country")
    val country: String? = null,

    @field:JsonProperty("sunrise")
    val sunrise: Int? = null,

    @field:JsonProperty("sunset")
    val sunset: Int? = null,

    @field:JsonProperty("id")
    val id: Int? = null,

    @field:JsonProperty("type")
    val type: Int? = null,

    @field:JsonProperty("message")
    val message: Double? = null
) : Parcelable