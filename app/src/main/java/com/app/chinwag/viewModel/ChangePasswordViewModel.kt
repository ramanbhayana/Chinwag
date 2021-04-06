package com.app.chinwag.viewModel

import androidx.lifecycle.MutableLiveData
import com.app.chinwag.utility.validation.*
import com.app.chinwag.R
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.common.Resource
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.commonUtils.utility.PasswordStrength
import com.app.chinwag.commonUtils.utility.validation.createValidationResult
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.mvvm.BaseViewModel
import com.app.chinwag.repository.ChangePasswordRepository
import com.google.gson.JsonElement
import io.reactivex.disposables.CompositeDisposable

class ChangePasswordViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val changePasswordRepository: ChangePasswordRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

//    var validationObserver =  MutableLiveData<ValidationResult>()

    val checkForInternetConnectionLiveData = MutableLiveData<Boolean>()
    val changePasswordLiveData = MutableLiveData<TAListResponse<JsonElement>>()

    override fun onCreate() {
        checkForInternetConnection()
    }

    private fun checkForInternetConnection() {
        when {
            checkInternetConnection() -> checkForInternetConnectionLiveData.postValue(true)
            else -> checkForInternetConnectionLiveData.postValue(false)
        }
    }

    /**
     * Api call for change password
     */
    fun callChangePassword(oldPassword: String, newPassword: String) {
        compositeDisposable.addAll(
            changePasswordRepository.callChangePassword(oldPassword, newPassword)
                .subscribeOn(schedulerProvider.io())
                .subscribe({ response ->
                    showDialog.postValue(false)
                    changePasswordLiveData.postValue(response)
                },
                    { error ->
                        showDialog.postValue(false)
                        messageString.postValue(Resource.error(error.message))
                    })
        )
    }

    /**
     * Validate passwords
     * @param oldPassword String
     * @param newPassword String
     * @param confirmPassword String
     * @return Boolean
     */
    fun isValid(oldPassword: String, newPassword: String, confirmPassword: String): Boolean {
        return when {

            oldPassword.isEmpty() -> {
                validationObserver.value =
                    createValidationResult(OLD_PASSWORD_EMPTY, R.id.tietOldPassword)
                false
            }
            newPassword.isEmpty() -> {
                validationObserver.value =
                    createValidationResult(PASSWORD_EMPTY, R.id.tietNewPassword)
                false
            }
            PasswordStrength.calculateStrength(newPassword).value < PasswordStrength.STRONG.value -> {
                validationObserver.value =
                    createValidationResult(PASSWORD_INVALID, R.id.tietNewPassword)
                false
            }
            confirmPassword.isEmpty() -> {
                validationObserver.value =
                    createValidationResult(CONFORM_PASSWORD_EMPTY, R.id.tietConfirmPassword)
                false
            }

            oldPassword == newPassword -> {
                validationObserver.value =
                    createValidationResult(OLD_NEW_PASSWORD_MATCH, R.id.tietNewPassword)
                false
            }

            newPassword != confirmPassword -> {
                validationObserver.value =
                    createValidationResult(PASSWORD_NOT_MATCH, R.id.tietConfirmPassword)
                false
            }

            else -> true
        }
    }
}