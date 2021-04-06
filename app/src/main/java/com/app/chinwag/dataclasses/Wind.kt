package com.app.chinwag.dataclasses

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Wind(

    @field:JsonProperty("deg")
    val deg: Int? = null,

    @field:JsonProperty("speed")
    val speed: Double? = null
) : Parcelable