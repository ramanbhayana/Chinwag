package com.app.chinwag.dataclasses

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Main(

    @field:JsonProperty("temp")
    val temp: Double? = null,

    @field:JsonProperty("temp_min")
    val tempMin: Double? = null,

    @field:JsonProperty("humidity")
    val humidity: Int? = null,

    @field:JsonProperty("pressure")
    val pressure: Int? = null,

    @field:JsonProperty("temp_max")
    val tempMax: Double? = null
) : Parcelable