package com.app.chinwag.viewModel

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.MutableLiveData
import com.app.chinwag.utility.validation.*
import com.app.chinwag.R
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.common.isValidMobileNumber
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.commonUtils.utility.extension.isValidMobileLenght
import com.app.chinwag.dataclasses.createValidationResult
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.OTPResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.ChangePhoneNumberRepository
import io.reactivex.disposables.CompositeDisposable

class ChangePhoneNumberViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val changePhoneNumberRepository: ChangePhoneNumberRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val changePhoneNumberLiveData = MutableLiveData<TAListResponse<OTPResponse>>()
    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()

    override fun onCreate() {
        checkForInternetConnection()
    }

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }

    fun checkUniqueUser(
        type: String,
        email: String = "",
        phone: String = "",
        userName: String = ""
    ) {
        val map = HashMap<String, String>()
        map["type"] = type // phone/email
        map["email"] = email
        map["mobile_number"] = PhoneNumberUtils.normalizeNumber(phone)
        map["user_name"] = userName

        compositeDisposable.addAll(
            changePhoneNumberRepository.checkUniqueUser(map)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        changePhoneNumberLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    /**
     * Validate inputs
     */
    fun isValid(phoneNumber: String): Boolean {
        return when {

            phoneNumber.isEmpty() -> {
                validationObserver.value =
                    createValidationResult(PHONE_NUMBER_EMPTY, R.id.tietNewPhoneNumber)
                false
            }

            !phoneNumber.isValidMobileNumber() -> {
                validationObserver.value =
                    createValidationResult(PHONE_NUMBER_INVALID, R.id.tietNewPhoneNumber)
                return false
            }

            !phoneNumber.isValidMobileLenght() -> {
                validationObserver.value = createValidationResult(
                    PHONE_NUMBER_INVALID_LENGHT, R.id.tietNewPhoneNumber
                )
                return false
            }
            else -> true
        }
    }
}