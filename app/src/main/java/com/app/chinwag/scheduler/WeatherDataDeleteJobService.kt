package com.app.chinwag.scheduler

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import com.app.chinwag.db.WeatherDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@SuppressLint("NewApi")
class WeatherDataDeleteJobService() : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {

        val weatherDataRepository: WeatherDataRepository? =
            WeatherDataRepository.getInstance(this)

        CoroutineScope(IO).launch {
            deleteWeatherDataFromDb(
                params?.jobId ?: -1,
                weatherDataRepository
            )
        }
        return true
    }

    private fun deleteWeatherDataFromDb(
        jobId: Int,
        weatherDataRepository: WeatherDataRepository?
    ) {
        weatherDataRepository?.deleteWeatherEntity(jobid = jobId)
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}