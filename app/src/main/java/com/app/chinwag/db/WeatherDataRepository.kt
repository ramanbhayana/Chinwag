package com.app.chinwag.db

import android.content.Context
import com.app.chinwag.db.dao.WeatherDao
import com.app.chinwag.db.entity.WeatherEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WeatherDataRepository(
    var mDao: WeatherDao? = null,
    var mIoExecutor: ExecutorService? = null
) {

    companion object {
        @Volatile
        private var sInstance: WeatherDataRepository? = null

        fun getInstance(application: Context): WeatherDataRepository? {
            if (sInstance == null) {
                synchronized(WeatherDataRepository::class.java) {
                    if (sInstance == null) {
                        val database: WeatherDataBase? =
                            WeatherDataBase.getInstance(application)
                        sInstance = WeatherDataRepository(
                            database?.weatherDao(),
                            Executors.newSingleThreadExecutor()
                        )
                    }
                }
            }
            return sInstance
        }
    }


    //  delete weather data method of DAO

    fun deleteWeatherEntity(jobid: Int) {
        mIoExecutor?.execute { mDao?.deleteWeatherData(jobid = jobid) }
    }

    // get weather data method of DAO

    fun getWeatherEntity(cityname: String): WeatherEntity? {
        return mDao?.getWeatherByCityName(cityname.toLowerCase().capitalize())
    }

    // insert method of DAO

    fun insertWeatherEntity(weatherEntity: WeatherEntity) {
        mDao?.insertWeatherEntity(weatherEntity)
    }


}