package com.app.chinwag.mvvm

import com.app.chinwag.view.authentication.signup.signupconfig.SignUpConfig
import com.app.chinwag.view.authentication.signup.signupconfig.SignUpConfigItem


/**
 * This class is used to provide application configuration setting like SignUp Screen configuration
 */
class AppConfig {

    companion object {

        const val  BANNER_AD = true  // Application has banner ad or not
        const val  INTERSTITIAL_AD = true // Application has interstitial ad or not
        const val  IS_SKIP = true // Application has skip functionality or not

        /**
         * This function will return sign up screen configuration setting
         * @return SignUpConfig
         */
        fun getSignUpConfiguration(): SignUpConfig {

            return SignUpConfig().apply {
                profilepictureoptional = "1" // 0 -> Optional , 1 -> Mandatory
                skip = "1"  // 1 -> show , 0 -> hide
                username = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "1" // 1 -> Optional , 0 -> Mandatory
                }
                firstname = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "0" // 1 -> Optional , 0 -> Mandatory
                }

                lastname = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "0" // 1 -> Optional , 0 -> Mandatory
                }

                // Email always mandatory
//                email = SignUpConfigItem().apply {
//                    visible = "1"  // 1 -> Show , 0 -> Hide
//                    optional = "1" // 1 -> Optional , 0 -> Mandatory
//                }

                phonenumber = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "0" // 1 -> Optional , 0 -> Mandatory
                }

                dateofbirth = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "0" // 1 -> Optional , 0 -> Mandatory
                }

                streetaddress = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "0" // 1 -> Optional , 0 -> Mandatory
                }

                state = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "0" // 1 -> Optional , 0 -> Mandatory
                }

                city = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "0" // 1 -> Optional , 0 -> Mandatory
                }

                zip = SignUpConfigItem().apply {
                    visible = "1"  // 1 -> Show , 0 -> Hide
                    optional = "0" // 1 -> Optional , 0 -> Mandatory
                }
            }
        }
    }
}
