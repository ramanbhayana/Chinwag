package com.app.chinwag.api.network

import com.app.chinwag.dataclasses.VersionConfigResponse
import com.app.chinwag.dataclasses.WeatherDataClass
import com.app.chinwag.dataclasses.generics.TAGenericResponse
import com.app.chinwag.dataclasses.generics.TAListResponse
import com.app.chinwag.dataclasses.response.FeedbackResponse
import com.app.chinwag.dataclasses.response.LoginResponse
import com.app.chinwag.dataclasses.response.StaticPageResponse
import com.app.chinwag.dataclasses.response.OTPResponse
import com.app.chinwag.dataclasses.response.forgotpasswordwithphone.ResetWithPhone
import com.google.gson.JsonElement
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface NetworkService {
    @GET("weather")
    fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") openWeatherKey: String
    ): Single<WeatherDataClass>

    @POST("forgot_password_phone")
    @FormUrlEncoded
    fun callForgotPasswordWithPhone(@Field("mobile_number") mobileNumber: String?)
            : Single<TAListResponse<ResetWithPhone>>

    @POST("forgot_password")
    @FormUrlEncoded
    fun callForgotPasswordWithEmail(@Field("email") email: String?)
            : Single<TAListResponse<JsonElement>>

    @POST("reset_password_phone")
    @FormUrlEncoded
    fun callResetPassword(
        @Field("new_password") newPassword: String?,
        @Field("mobile_number") mobileNumber: String?,
        @Field("reset_key") resetKey: String?
    ): Single<TAListResponse<JsonElement>>

    @POST("static_pages")
    @FormUrlEncoded
    fun callGetStaticPageData(@Field("page_code") pageCode: String)
            : Single<TAListResponse<StaticPageResponse>>

    @POST("update_page_version")
    @FormUrlEncoded
    fun callUpdateTNCPrivacyPolicy(@Field("page_type") pageType: String)
            : Single<TAListResponse<JsonElement>>

    @POST("callSignUpWithPhone")
    @Multipart
    fun callSignUpWithPhone(@PartMap fieldMap: HashMap<String, RequestBody>,
                            @Part file: MultipartBody.Part? = null): Single<TAListResponse<LoginResponse>>

    @POST("user_sign_up_email")
    @Multipart
    fun callSignUpWithEmail(@PartMap fieldMap: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part? = null): Single<TAListResponse<JsonElement>>

    @POST("social_sign_up")
    @Multipart
    fun callSignUpWithSocial(@PartMap fieldMap: HashMap<String, RequestBody>,
                             @Part file: MultipartBody.Part? = null): Single<TAListResponse<LoginResponse>>

    /**
     * Api call for check unique number. This api will take following input parameter in hash map
     * @param type  phone/email    phone -> If phone number unique check, email -> If email unique check
     * @param email (optional) check email unique
     * @param mobile_number (optional) check phone unique
     * @param map HashMap<String, String>
     * @return Call<WSListResponse<OTPResponse>>
     */
    @POST("check_unique_user")
    @FormUrlEncoded
    fun callCheckUniqueUser(@FieldMap map: HashMap<String, String>): Single<TAListResponse<OTPResponse>>

    @FormUrlEncoded
    @POST("user_login_phone")
    fun callLoginWithPhone(@FieldMap map: HashMap<String, String>): Single<TAListResponse<LoginResponse>>

    @POST("send_verification_link")
    @FormUrlEncoded
    fun callSendVerificationLink(@FieldMap map: HashMap<String, String>): Single<TAListResponse<JsonElement>>

    @FormUrlEncoded
    @POST("user_login_email")
    fun loginWithEmail(@FieldMap map: HashMap<String, String>): Single<TAListResponse<LoginResponse>>

    @FormUrlEncoded
    @POST("social_login")
    fun loginWithSocial(@FieldMap map: HashMap<String, String>): Single<TAListResponse<LoginResponse>>

    @POST("delete_account")
    fun callDeleteAccount(): Single<TAListResponse<JsonElement>>

    @POST("logout")
    fun callLogOut(): Single<TAListResponse<JsonElement>>

    @POST("update_device_token")
    @FormUrlEncoded
    fun callUpdateDeviceToken(@Field("device_token") deviceToken: String): Single<TAGenericResponse<JsonElement>>

    @POST("post_a_feedback")
    @Multipart
    fun callSendFeedback(
        @PartMap map: HashMap<String, RequestBody>,
        @Part files: List<MultipartBody.Part>?
    ): Single<TAListResponse<FeedbackResponse>>

    @POST("change_password")
    @FormUrlEncoded
    fun callChangePassword(
        @Field("old_password") old_password: String?,
        @Field("new_password") mobileNumber: String?
    ): Single<TAListResponse<JsonElement>>

    @POST("change_mobile_number")
    @FormUrlEncoded
    fun callChangePhoneNumber(@Field("new_mobile_number") old_password: String?): Single<TAListResponse<JsonElement>>

    @Multipart
    @POST("edit_profile")
    fun callUpdateUserProfile(
        @PartMap map: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part? = null
    ): Single<TAListResponse<LoginResponse>>

    @POST("get_config_paramaters")
    fun callConfigParameters(): Single<TAListResponse<VersionConfigResponse>>


    @POST("go_ad_free")
    @FormUrlEncoded
    fun callGoAdFree(@FieldMap map: HashMap<String, String>): Single<TAListResponse<JsonElement>>
}