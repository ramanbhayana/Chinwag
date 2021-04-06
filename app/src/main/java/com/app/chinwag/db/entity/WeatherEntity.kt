package com.app.chinwag.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey
    @ColumnInfo(name = "jobid")
    val jobid: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "icon")
    val icon: String,

    @ColumnInfo(name = "main")
    val main: String,

    @ColumnInfo(name = "temp")
    val temp: Double,

    @ColumnInfo(name = "temp_min")
    val tempMin: Double,

    @ColumnInfo(name = "humidity")
    val humidity: Int,

    @ColumnInfo(name = "pressure")
    val pressure: Int,

    @ColumnInfo(name = "temp_max")
    val tempMax: Double,

    @ColumnInfo(name = "sunrise")
    val sunrise: Int,

    @ColumnInfo(name = "sunset")
    val sunset: Int,

    @ColumnInfo(name = "deg")
    val deg: Int,

    @ColumnInfo(name = "speed")
    val speed: Double
)