package com.app.chinwag.viewModel

import android.app.Application
import android.telephony.PhoneNumberUtils
import androidx.lifecycle.MutableLiveData
import com.app.chinwag.utility.validation.*
import com.app.chinwag.R
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.api.network.WebServiceUtils
import com.app.chinwag.application.AppineersApplication.Companion.sharedPreference
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.commonUtils.utility.extension.isValidInputPhone
import com.app.chinwag.commonUtils.utility.extension.isValidInputText
import com.app.chinwag.commonUtils.utility.extension.toMMDDYYYStr
import com.app.chinwag.commonUtils.utility.getDeviceName
import com.app.chinwag.commonUtils.utility.getDeviceOSVersion
import com.app.chinwag.dataclasses.generics.LocationAddressModel
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.request.SignUpRequestModel
import com.app.chinwag.dataclasses.response.LoginResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.UserProfileRepository
import com.app.chinwag.view.authentication.signup.signupconfig.SignUpConfigItem
import io.reactivex.disposables.CompositeDisposable
import okhttp3.RequestBody
import java.util.*
import kotlin.collections.HashMap

class UserProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val application: Application,
    private val userProfileRepository: UserProfileRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {
        checkForInternetConnection()
    }

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    val updateUserLiveData = MutableLiveData<TAListResponse<LoginResponse>>()

    fun updateUserProfile(request: SignUpRequestModel) {
        val map = HashMap<String, RequestBody>()
        map["user_name"] = WebServiceUtils.getStringRequestBody(request.userName)
        map["first_name"] = WebServiceUtils.getStringRequestBody(request.firstName)
        map["last_name"] = WebServiceUtils.getStringRequestBody(request.lastName)
        map["dob"] = WebServiceUtils.getStringRequestBody(request.dob)
        map["address"] = WebServiceUtils.getStringRequestBody(request.address)
        map["city"] = WebServiceUtils.getStringRequestBody(request.city)
        map["latitude"] = WebServiceUtils.getStringRequestBody(request.latitude)
        map["longitude"] = WebServiceUtils.getStringRequestBody(request.longitude)
        map["state_name"] = WebServiceUtils.getStringRequestBody(request.state)
        map["zipcode"] = WebServiceUtils.getStringRequestBody(request.zipCode)
        map["mobile_number"] =
            WebServiceUtils.getStringRequestBody(PhoneNumberUtils.normalizeNumber(request.mobileNumber))
        map["device_type"] = WebServiceUtils.getStringRequestBody(IConstants.DEVICE_TYPE_ANDROID)
        map["device_model"] = WebServiceUtils.getStringRequestBody(getDeviceName())
        map["device_os"] = WebServiceUtils.getStringRequestBody(getDeviceOSVersion())
        map["device_token"] = WebServiceUtils.getStringRequestBody(
            sharedPreference.deviceToken
                ?: ""
        )
        compositeDisposable.addAll(
            userProfileRepository.updateUserProfile(
                map,
                file = if (request.profileImage.isEmpty()) null else WebServiceUtils.getStringMultipartBodyPart(
                    "user_profile",
                    request.profileImage
                )
            )
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { response ->
                        showDialog.postValue(false)
                        updateUserLiveData.postValue(response)
                    },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    }
                )
        )
    }

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }

    fun getParseAddress(placeAddress: String?): LocationAddressModel {
        val splitAddress = placeAddress?.split(",")?.reversed()
        return LocationAddressModel().apply {
            fullAddress = placeAddress ?: ""
            splitAddress?.forEachIndexed { index, str ->
                if (index == 0) country = str.trim()
                if (index == 1) {
                    val stateSplit = str.trim().split(" ")
                    if (stateSplit.isNotEmpty()) state = stateSplit[0].trim()
                    if (stateSplit.size > 1) zipCode = stateSplit[1].trim()
                }
                if (index == 2) city = str.trim()
                if (index == 3) address = str.trim()
            }
        }
    }

    fun getDateFromPicker(year: Int, month: Int, dayOfMonth: Int): String {
        val calender = Calendar.getInstance()
        calender.set(Calendar.YEAR, year)
        calender.set(Calendar.MONTH, month)
        calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        return calender.time.toMMDDYYYStr()
    }

    /**
     * Validate sign up inputs
     */
    fun isValid(request: SignUpRequestModel): Boolean {
        when {

            //User Name
            !isValidInputText(
                text = request.userName, emptyFailType = USER_NAME_EMPTY,
                invalidFailType = USER_NAME_INVALID, viewId = R.id.tietUserName,
//                minimumLength = application.resources.getInteger(R.integer.user_name_min_length),
//                maximumLength = application.resources.getInteger(R.integer.user_name_max_length),
                validationObserver = validationObserver, config = SignUpConfigItem("1", "1")
            ) -> {

                return false
            }

            //First Name
            !isValidInputText(
                text = request.firstName, emptyFailType = FIRST_NAME_EMPTY,
                invalidFailType = FIRST_NAME_INVALID, viewId = R.id.tietFirstName,
//                minimumLength = application.resources.getInteger(R.integer.first_name_min_length),
//                maximumLength = application.resources.getInteger(R.integer.first_name_max_length),
                validationObserver = validationObserver, config = SignUpConfigItem("1", "0")
            ) -> {

                return false
            }

            //Last Name
            !isValidInputText(
                text = request.lastName, emptyFailType = LAST_NAME_EMPTY,
                invalidFailType = LAST_NAME_INVALID, viewId = R.id.tietLastName,
//                minimumLength = application.resources.getInteger(R.integer.first_name_min_length),
//                maximumLength = application.resources.getInteger(R.integer.first_name_max_length),
                validationObserver = validationObserver, config = SignUpConfigItem("1", "0")
            ) -> {

                return false
            }

            //Phone number
            !isValidInputPhone(
                phoneNumber = request.mobileNumber,
                validationObserver = validationObserver, config = SignUpConfigItem("1", "0")
            ) -> {
                return false
            }

            //Street Address
            !isValidInputText(
                text = request.address, emptyFailType = ADDRESS_EMPTY, viewId = R.id.tietAddress,
                validationObserver = validationObserver, config = SignUpConfigItem("1", "0")
            ) -> {
                return false
            }

            //City
            !isValidInputText(
                text = request.city, emptyFailType = CITY_EMPTY, viewId = R.id.tietCity,
                validationObserver = validationObserver, config = SignUpConfigItem("1", "0")
            ) -> {
                return false
            }

            //State
            !isValidInputText(
                text = request.state, emptyFailType = STATE_EMPTY, viewId = R.id.tietState,
                validationObserver = validationObserver, config = SignUpConfigItem("1", "0")
            ) -> {
                return false
            }

            //Zip Code
            !isValidInputText(
                text = request.zipCode, emptyFailType = ZIP_CODE_EMPTY,
                invalidFailType = ZIP_CODE_INVALID, viewId = R.id.tietZipCode,
//                minimumLength = application.resources.getInteger(R.integer.zip_code_min_length),
//                maximumLength = application.resources.getInteger(R.integer.zip_code_max_length),
                validationObserver = validationObserver, config = SignUpConfigItem("1", "0")
            ) -> {

                return false
            }

            //DOB
            !isValidInputText(
                text = request.dob, emptyFailType = DOB_EMPTY, viewId = R.id.tietDOB,
                validationObserver = validationObserver, config = SignUpConfigItem("1", "0")
            ) -> {
                return false
            }

            else -> return true
        }
    }

    fun getEditProfileRequest(
        userProfileImage: String = "", userName: String = "", firstName: String = "",
        lastName: String = "", dob: String = "", phoneNumber: String = "",
        address: String = "", latitude: String = "",
        longitude: String = "", city: String = "",
        state: String = "", zip: String = ""
    ): SignUpRequestModel {
        val request = SignUpRequestModel()
        request.profileImage = userProfileImage
        request.userName = userName
        request.firstName = firstName
        request.lastName = lastName
        request.mobileNumber = phoneNumber
        request.dob = dob
        request.address = address
        request.latitude = latitude
        request.longitude = longitude
        request.city = city
        request.state = state
        request.zipCode = zip
        return request
    }

}