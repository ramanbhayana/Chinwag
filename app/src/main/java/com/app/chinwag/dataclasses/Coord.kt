package com.app.chinwag.dataclasses

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Coord(

    @field:JsonProperty("lon")
    val lon: Double? = null,

    @field:JsonProperty("lat")
    val lat: Double? = null
) : Parcelable
