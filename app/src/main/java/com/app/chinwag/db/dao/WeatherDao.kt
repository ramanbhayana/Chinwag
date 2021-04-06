package com.app.chinwag.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.chinwag.db.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherEntity(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather_table WHERE name=:cityName")
    fun getWeatherByCityName(cityName: String?): WeatherEntity

    @Query("Delete FROM weather_table WHERE jobid=:jobid")
    fun deleteWeatherData(jobid: Int?)
}
