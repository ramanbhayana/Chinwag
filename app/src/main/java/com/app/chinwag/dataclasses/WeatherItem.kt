package com.app.chinwag.dataclasses

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherItem(

    @field:JsonProperty("icon")
    val icon: String? = null,

    @field:JsonProperty("description")
    val description: String? = null,

    @field:JsonProperty("main")
    val main: String? = null,

    @field:JsonProperty("id")
    val id: Int? = null
) : Parcelable
