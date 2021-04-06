package com.app.chinwag.repository

import com.app.chinwag.api.network.NetworkService
import com.app.chinwag.dataclasses.WeatherDataClass
import com.app.chinwag.db.WeatherDataRepository
import com.app.chinwag.db.entity.WeatherEntity
import com.app.chinwag.BuildConfig
import io.reactivex.Single
import javax.inject.Inject

class CentralRepository @Inject constructor(
    val networkService: NetworkService,
    val WeatherDataRepository: WeatherDataRepository?
) {

    fun getWeatherByCity(cityName: String): Single<WeatherDataClass>
    = networkService.getWeatherByCity(cityName, BuildConfig.openweathermapapikey)

    fun insertWeatherEntityIntoDatabase(weatherEntity: WeatherEntity) {
        WeatherDataRepository?.insertWeatherEntity(weatherEntity)
    }

    fun getWeatherEntityFromDatabase(cityName: String): WeatherEntity?
    = WeatherDataRepository?.getWeatherEntity(cityName)
}