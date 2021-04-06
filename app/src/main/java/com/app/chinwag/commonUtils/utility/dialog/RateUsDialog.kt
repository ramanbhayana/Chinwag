package com.app.chinwag.commonUtils.utility.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.app.chinwag.BuildConfig
import com.app.chinwag.R
import com.app.chinwag.commonUtils.utility.extension.sharedPreference
//import com.hb.logger.Logger
import java.util.*
import java.util.concurrent.TimeUnit


class RateUsDialog(
        private val activity: Activity
) : DialogFragment() {

    val logger by lazy {
//        Logger(this::class.java.simpleName)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        val viewLayout = inflater.inflate(R.layout.dialog_app_rating, container, false)
        val btnRateNow = viewLayout.findViewById<TextView>(R.id.tvRateNow)
        val btnCancel = viewLayout.findViewById<TextView>(R.id.tvCancel)
        val btnLater = viewLayout.findViewById<TextView>(R.id.tvLater)

        btnRateNow.setOnClickListener {
            sharedPreference.isAppRatingDone = true
            redirectToPlayStore()
            this.dismiss()
        }

        btnCancel.setOnClickListener {
            resetRatingTimer()
            this.dismiss()
        }

        btnLater.setOnClickListener {
            resetRatingTimer()
            this.dismiss()
        }

        return viewLayout
    }

    private fun resetRatingTimer() {
        sharedPreference.ratingInitTime = System.currentTimeMillis()
        sharedPreference.appLaunchCount = 0
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun redirectToPlayStore() {
        try {
            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}"))
            startActivity(playStoreIntent)
        } catch (anfe: ActivityNotFoundException) {
            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"))
            if (playStoreIntent.resolveActivity(activity.packageManager) != null) {
                startActivity(playStoreIntent)
            } else {
//                logger.dumpCustomEvent("Rate app", "Play store provider not found")
            }
        }

    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    companion object {
        fun checkRatingDialogCriteria(): Boolean {
            if (!sharedPreference.isAppRatingDone) {
                if (sharedPreference.appLaunchCount > 5) {
                    return true
                }

                val current = Calendar.getInstance()
                val difference = current.timeInMillis - sharedPreference.ratingInitTime
                if (TimeUnit.MILLISECONDS.toDays(difference) > 3) {
                    return true
                }
                return false
            }
            return false
        }
    }
}