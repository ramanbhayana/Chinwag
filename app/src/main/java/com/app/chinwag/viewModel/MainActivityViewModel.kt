package com.app.chinwag.viewModel

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.app.chinwag.mvvm.BaseViewModel
import com.bumptech.glide.Glide
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.common.CommonUtils
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.dataclasses.WeatherDataClass
import com.app.chinwag.db.entity.WeatherEntity
import com.app.chinwag.BuildConfig
import com.app.chinwag.R
import com.app.chinwag.repository.CentralRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

import kotlin.math.roundToInt

class MainActivityViewModel(
    ezVSchedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    ezVNetworkHelper: NetworkHelper,
    private val centralRepository: CentralRepository
) : BaseViewModel(ezVSchedulerProvider, compositeDisposable, ezVNetworkHelper) {
    override fun onCreate() {
    }

    companion object {
        // Making  Java interoperability
        @JvmStatic
        @BindingAdapter("icon")
        fun loadImage(view: ImageView, icon: String?) {
            if (!icon.isNullOrEmpty()) {
                Glide.with(view.context).load("${BuildConfig.Icon_Base_Url}${icon}.png")
                    .fitCenter().placeholder(R.mipmap.ic_launcher).into(view)
            }
        }
    }

    // Live Data instances
    val weatherLiveData = MutableLiveData<WeatherEntity>()
    val temperature = MutableLiveData<CharSequence>()
    val temperatureDesc = MutableLiveData<CharSequence>()
    val sunrise = MutableLiveData<CharSequence>()
    val sunset = MutableLiveData<CharSequence>()
    val humidity = MutableLiveData<CharSequence>()
    val windSpeed = MutableLiveData<CharSequence>()
    val generateJobIdLiveData = MutableLiveData<WeatherDataClass>()
    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    var jobId: Int = -1

    fun getWeatherByCityName(cityName: String) {

        if (checkInternetConnectionWithMessage()) {
            compositeDisposable.addAll(
                centralRepository.getWeatherByCity(cityName)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        {
                            showDialog.postValue(false)
                            generateJobIdLiveData.postValue(it)
                        }, {
                            showDialog.postValue(false)
                            messageString.postValue(Resource.error("Oops!! Something went wrong"))
                        }
                    ))
        }

    }

    fun checkForInternetConnection() {
        if (checkInternetConnection()) {
            checkForInternetConnectionLiveData.postValue(true)
        } else {
            checkForInternetConnectionLiveData.postValue(false)
        }
    }

    fun getWeatherData(cityName: String) {
        if (centralRepository.getWeatherEntityFromDatabase(cityName) != null) {
            weatherLiveData.postValue(centralRepository.getWeatherEntityFromDatabase(cityName))
        } else {
            getWeatherByCityName(cityName)
        }
    }

    fun getWeatherEntityData(it: WeatherDataClass?): WeatherEntity? {
        val weatherEntity = WeatherEntity(
            jobId,
            it?.name ?: "",
            it?.weather?.get(0)?.icon ?: "",
            it?.weather?.get(0)?.main ?: "",
            it?.main?.temp ?: 0.0,
            it?.main?.tempMin ?: 0.0,
            it?.main?.humidity ?: 0,
            it?.main?.pressure ?: 0,
            it?.main?.tempMax ?: 0.0,
            it?.sys?.sunrise ?: 0,
            it?.sys?.sunset ?: 0,
            it?.wind?.deg ?: 0,
            it?.wind?.speed ?: 0.0
        )

        // Background call to insert data into DB

        CoroutineScope(IO).launch {
            insertDataIntoDb(weatherEntity)
        }
        return weatherEntity
    }

    private fun insertDataIntoDb(weatherEntity: WeatherEntity) {
        centralRepository.insertWeatherEntityIntoDatabase(weatherEntity)
    }


    // Function to add subscript text

    fun getSubScriptTextForDegree(source: String): CharSequence? {
        val mSource = "${source}o"
        val index: Int = mSource.indexOf("o")
        val cs = SpannableStringBuilder(mSource)
        cs.setSpan(SuperscriptSpan(), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        cs.setSpan(RelativeSizeSpan(0.75f), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return cs
    }

    fun insertAndSetWeatherData(it: WeatherDataClass?) {
        weatherLiveData.postValue(getWeatherEntityData(it))
    }

    fun setLiveData(it: WeatherEntity) {
        temperature.value =
            getSubScriptTextForDegree(
                (it.temp - 272.15).roundToInt().toString()
            )

        temperatureDesc.value = TextUtils.concat(
            it.main,
            " ",
            getSubScriptTextForDegree(
                (it.tempMin - 272.15).roundToInt().toString()
            ),
            "C / ",
            getSubScriptTextForDegree(
                (it.tempMax - 272.15).roundToInt().toString()
            ),
            "C"
        )

        sunrise.value = "Sunrise ${
        CommonUtils.getDateOrTimeFromSpecificFormat(
            "HH:mm",
            it.sunrise.toLong()
        )
        }"
        sunset.value = "Sunset ${
        CommonUtils.getDateOrTimeFromSpecificFormat(
            "HH:mm",
            it.sunset.toLong()
        )
        }"
        humidity.value = "Humidity ${it.humidity}%"
        windSpeed.value = "Wind Speed ${it.speed} km/h"
        getSubScriptTextForDegree(it.deg.toString())

    }

}